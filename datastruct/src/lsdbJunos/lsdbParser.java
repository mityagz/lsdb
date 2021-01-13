package lsdbJunos;

import com.sonalake.utah.Parser;
import com.sonalake.utah.config.Config;
import com.sonalake.utah.config.ConfigLoader;
import isisTLV.*;
import net.juniper.netconf.Device;
import org.apache.commons.cli.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.PKIXRevocationChecker;
import java.util.*;
import org.apache.commons.*;

/**
 * Created by mitya on 12/4/20.
 */
public class lsdbParser {
    private String configRes0 = "conf/juniper.lsdb.config.xml";
    private String configRes1 = "conf/juniper.lsdb.config1.xml";
    private String configRes2 = "conf/juniper.lsdb.config2.xml";
    private String lsdb_out;
    Map<String, Boolean> marked = new HashMap<String, Boolean>();
    HashMap<String, isisLSP> LSP = new HashMap<String, isisLSP>();

    private void d(String ip_addr, String user, String pass) throws IOException, SAXException {
        Device device = null;
        if(ip_addr.contains("0.0.0.0")) return;
        if(marked.containsKey(ip_addr)) return;
        marked.put(ip_addr, true);

        try {
            device = new Device(ip_addr, user, pass, null);
            System.out.println("Trying login to " + ip_addr);
            } catch (Exception e) {
                e.printStackTrace();
            }

            device.connect();
            boolean isLocked = device.lockConfig();
            if (!isLocked) {
                System.out.println("Could not lock configuration. Exit now.");
                return;
            }
            System.out.println("Login to " + ip_addr+ " is succeeded");
            lsdb_out = device.runCliCommand("show isis database extensive");
            System.out.println("Execute 'show isis database extensive'");
            lsdb_parse("", lsdb_out, "");
            System.out.println("Building graph  ");
            device.unlockConfig();
            device.close();
    }

    //private Map<String, isisLSP> lsdb_parse(String ip, String in_parse, Map<String, isisLSP> np01, String systemName) {
    private void lsdb_parse(String ip, String in_parse,     String systemName) {
        URL cURL = Thread.currentThread().getContextClassLoader().getResource(configRes0);
        List<Map<String, String>> lsdbValues = new ArrayList<Map<String, String>>();
        boolean inside = false;
        boolean is_header = false;
        boolean is_packet = false;
        boolean is_tlv = false;
        boolean is_aging_time_expiried = false;

        try {
            Config cfg = new ConfigLoader().loadConfig(cURL);
            InputStream is = new ByteArrayInputStream(in_parse.getBytes(Charset.forName("UTF-8")));
            BufferedReader ir = new BufferedReader(new InputStreamReader(is));
            Parser parser = Parser.parse(cfg, ir);
            String LSP_ID = null;
            String Sequence;
            String Checksum;
            Integer Lifetime;
            isisLSP lsp = null;
            Header lsp_header = null;
            Packet lsp_packet = null;
            while (true) {
                Map<String, String> record = parser.next();
                if(record == null)
                    break;
                else {
                    is_aging_time_expiried = false;
                    if(record.get("Checksum") != null && record.get("LSP_ID") != null && record.get("Lifetime") != null && record.get("Sequence") != null && inside == false) {
                        LSP_ID = record.get("LSP_ID");
                        Sequence = record.get("Sequence");
                        Checksum = record.get("Checksum");
                        Lifetime = Integer.parseInt(record.get("Lifetime"));
                        lsp = new isisLSP(LSP_ID, Sequence, Checksum, Lifetime);
                        inside = true;
                        //System.out.println("Head of LSP: " + LSP_ID);
                        //System.out.println(record);
                    } else if(inside) {
                        if(record.get("lsdb_delim") != null) {
                            inside = false;
                            is_tlv = false;
                            //System.out.println("End of TLV!");
                            LSP.put(lsp.getLSP_ID(), lsp);
                        } else if (record.isEmpty()) {
                            //else if (record.get("empty_line") != null) {
                            //System.out.println("Empty line!");
                            if(is_header) {
                                is_header = false;
                                //System.out.println("End of header!");
                            }
                            if(is_packet) {
                                is_packet = false;
                                //System.out.println("End of packet!");
                            }
                            //inside = false;
                            //LSP.put(lsp.getLSP_ID(), lsp);
                            //System.out.println("End of LSP: " + LSP_ID + "\n");
                        } else if(record.get("IS_neighbor") != null) {
                            lsp.setIsNeighborList(record.get("IS_neighbor"), Integer.parseInt(record.get("IS_neighbor_metric")));
                        } else if(record.get("ip_prefix") != null) {
                            lsp.setIpPrefixList(record.get("ip_prefix"), Integer.parseInt(record.get("ip_prefix_metric")));
                        } else if(record.get("ip6_prefix") != null) {
                            lsp.setIpPrefixList(record.get("ip6_prefix"), Integer.parseInt(record.get("ip6_prefix_metric")));
                        } else if(record.get("header_lsp_id") != null) {
                            //System.out.println("Header! " + record.get("header_lsp_id"));
                            is_header = true;
                            lsp_header = new Header(record.get("header_lsp_id"), Integer.parseInt(record.get("header_length")));
                            lsp.setHeader(lsp_header);
                        } else if(record.get("packet_lsp_id") != null) {
                            //System.out.println("Packet! " + record.get("packet_lsp_id"));
                            is_packet = true;
                            lsp_packet = new Packet(record.get("packet_lsp_id"), Integer.parseInt(record.get("packet_length")), Integer.parseInt(record.get("packet_lifetime")));
                            lsp.setPacket(lsp_packet);
                        } else if(record.get("TLV") != null) {
                            //System.out.println("Begin of TLV!");
                            is_tlv = true;
                        } else if(is_header) {
                            if(record.get("header_allocate_length") != null) {
                                lsp_header.setAllocated_length(Integer.parseInt(record.get("header_allocate_length")));
                                lsp_header.setRouter_ID(record.get("header_router_id"));
                            }
                            if(record.get("header_remaining_lifetime") != null) {
                                lsp_header.setRemaining_lifetime(Integer.parseInt(record.get("header_remaining_lifetime")));
                                lsp_header.setLevel(Integer.parseInt(record.get("header_level")));
                                lsp_header.setInterface(Integer.parseInt(record.get("header_interface")));
                            }
                            if(record.get("header_estimated_free_bytes") != null) {
                                lsp_header.setEstimated_free_bytes(Integer.parseInt(record.get("header_estimated_free_bytes")));
                                lsp_header.setActual_free_bytes(Integer.parseInt(record.get("header_actual_free_bytes")));
                            }
                            if(record.get("header_aging_timer_expires") != null) {
                                lsp_header.setAging_timer_expires_in(Integer.parseInt(record.get("header_aging_timer_expires")));
                                is_aging_time_expiried = true;
                            }
                            if(record.get("header_protocols") != null) {
                                //System.out.println("header_protocols: " + record.get("header_protocols"));
                                lsp_header.setProtocols();
                                is_header = false;
                            } else {
                                if(is_aging_time_expiried)
                                is_header = false;
                            }
                        } else if(is_packet) {
                            if(record.get("packet_checksum") != null) {
                                lsp_packet.setChecksum(record.get("packet_checksum"));
                                lsp_packet.setSequence(record.get("packet_sequence"));
                                lsp_packet.setAttributes(record.get("packet_attributes"));
                            }
                            if(record.get("packet_nlpid") != null) {
                                lsp_packet.setNLPID(record.get("packet_fixed_length"));
                                lsp_packet.setFixed_length(Integer.parseInt(record.get("packet_fixed_length")));
                                lsp_packet.setVersion(Integer.parseInt(record.get("packet_version")));
                                lsp_packet.setSysid_length(Integer.parseInt(record.get("packet_sysid_length")));
                            }
                            if(record.get("packet_type") != null) {
                                lsp_packet.setPacket_type(Integer.parseInt(record.get("packet_type")));
                                lsp_packet.setPacket_version(Integer.parseInt(record.get("packet_version")));
                                lsp_packet.setMax_area(Integer.parseInt(record.get("packet_max_aria")));
                                is_packet = false;
                            }
                        } else if(is_tlv) {
                            if(record.get("tlv_area_address") != null) {
                                //System.out.println("tlv_area_address: " + record.get("tlv_area_address"));
                                lsp.setTLV_AreaAddresses(new AreaAddresses(record.get("tlv_area_address")));
                            }
                            if(record.get("tlv_lsp_buffer_size") != null) {
                                //System.out.println("tlv_lsp_buffer_size: " + record.get("tlv_lsp_buffer_size"));
                                lsp.setLspBufferSize(new LSPBufferSize(Integer.parseInt(record.get("tlv_lsp_buffer_size"))));
                            }
                            if(record.get("tlv_speaks") != null) {
                                //System.out.println("tlv_speaks: " + record.get("tlv_speaks"));
                                lsp.setSpeaks(record.get("tlv_speaks"));
                            }
                            if(record.get("tlv_router_id") != null) {
                                //System.out.println("tlv_router_id: " + record.get("tlv_router_id"));
                                lsp.setTe_routerID(new TE_RouterID(record.get("tlv_router_id")));
                            }
                            if(record.get("tlv_ip_address") != null) {
                                //System.out.println("tlv_ip_address: " + record.get("tlv_ip_address"));
                                lsp.setIp_address(new IP_Address(record.get("tlv_ip_address")));
                            }
                            if(record.get("tlv_hostname") != null) {
                                //System.out.println("tlv_hostname: " + record.get("tlv_hostname"));
                                lsp.setHostname(new Hostname(record.get("tlv_hostname")));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show_lsp() {
        Set<String> lsp_id = LSP.keySet();
        for(String id : lsp_id) {
            System.out.println(id);
            System.out.println("lsp_id: " + LSP.get(id).getLSP_ID());
            System.out.println("sequence: " + LSP.get(id).getSequence());
            System.out.println("checksum: " + LSP.get(id).getChecksum());
            System.out.println("lifetime: " + LSP.get(id).getLifetime());
            ArrayList<isNeighbor> neighborList = LSP.get(id).getIsNeighborList();
            for (int i = 0; i < neighborList.size(); i++) {
                System.out.println("                " + "neighbor: " + neighborList.get(i).toString());
            }
            //System.out.println("-------------------------------------------------------");
            ArrayList<IPPrefix> ipPrefixes = LSP.get(id).getIpPrefixList();
            for (int i = 0; i < ipPrefixes.size(); i++) {
                System.out.println("                " + "ip prefix: " + ipPrefixes.get(i).toString());
            }
            ArrayList<IPPrefix> ipPrefixes6 = LSP.get(id).getIpPrefixList6();
            for (int i = 0; 0 < ipPrefixes6.size(); i++) {
                System.out.println("                " + "ip6 prefix: " + ipPrefixes6.get(i).toString());
            }
            System.out.println(LSP.get(id).getHeader());
            System.out.println();
            System.out.println(LSP.get(id).getPacket());
            // # TLVs:
            System.out.println("TLVs:\n---------------------------------------");
            System.out.println("AreaAddress: " + LSP.get(id).getTLV_AreaAddresses().getAreaAddress());
            System.out.println("LSP Buffer size: " + LSP.get(id).getLspBufferSize().getLSPBufferSize());
            System.out.println("Hostname: " + LSP.get(id).getHostname().getHostname());
            System.out.println("TE_RouterID: " + LSP.get(id).getTe_routerID().getIpRouterID());
            System.out.println("Protocol supported 0: " + LSP.get(id).getProtocolSupported().get(0).getProto());
            System.out.println("Protocol supported 1: " + LSP.get(id).getProtocolSupported().get(1).getProto());
            System.out.println("IP Address: " + LSP.get(id).getIP_Address().getIp_address());
            System.out.println();
        }
    }

    public HashMap<String, isisLSP> getLsp() {
        return LSP;
    }

    public static void main(String [] args) throws IOException, SAXException {
        Options options = new Options();
        Option hosts = new Option("h", "hosts");
        hosts.setRequired(false);
        options.addOption(hosts);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(args[0], options);
            System.exit(1);
        }

        if(args.length != 3)
            usage(args);

        String ip = args[0];
        String user = args[1];
        String pass = args[2];

        lsdbParser lsdbparser = new lsdbParser();
        try {
            lsdbparser.d(ip, user, pass);
            Topo t = new Topo(lsdbparser.getLsp());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private static void usage(String []a) {
        System.out.println("Usage: java -jar ./path/to/lsdb.jar ip_isis_lsdb_router username password");
        System.exit(1);
    }
}