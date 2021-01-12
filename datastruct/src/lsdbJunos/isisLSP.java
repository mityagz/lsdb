package lsdbJunos;

import isisTLV.*;

import java.util.ArrayList;

/**
 * Created by mitya on 12/4/20.
 */
public class isisLSP {
    private String LSP_ID;
    private String Sequence;
    private String Checksum;
    private Integer Lifetime;
    private ArrayList<isNeighbor> isNeighborList;
    private ArrayList<IPPrefix> ipPrefixList;
    private ArrayList<IPPrefix> ipPrefixList6;
    private Header header;
    private Packet packet;
    // Extended attr
    private AreaAddresses AreaAddress;
    private LSPBufferSize lspBufferSize;
    private Hostname hostname;
    private TE_RouterID te_routerID;
    private IP_Address ip_address;
    private ArrayList<IP_Interface_Address> ip_interface_addresses;
    private ArrayList<ProtocolSupported> protocolSupporteds;
    private ArrayList<Extended_IS_Neighbor> extended_is_neighbors;
    private ArrayList<Extended_IP_Reachability> extended_ip_reachabilities;
    private ArrayList<Extended_IP_Reachability> extended_ip_reachabilitiesV6;

    public isisLSP(String lsp_id, String sequence, String checksum, Integer lifetime) {
        this.LSP_ID = lsp_id;
        this.Sequence = sequence;
        this.Checksum = checksum;
        this.Lifetime = lifetime;
        isNeighborList = new ArrayList<isNeighbor>();
        ipPrefixList = new ArrayList<IPPrefix>();
        ipPrefixList6 = new ArrayList<IPPrefix>();
    }
    public void setLSP_ID(String lsp_id) { this.LSP_ID = lsp_id; }
    public void setSequence(String sequence) { this.Sequence = sequence; }
    public void setIsNeighborList(String  isNeighbor, Integer metric) {
        isNeighbor neighbor = new isNeighbor(isNeighbor, metric);
        isNeighborList.add(neighbor);
    }
    public void setIpPrefixList(String is_prefix, Integer metric) {
        IPPrefix prefix = new IPPrefix(is_prefix, metric);
        ipPrefixList.add(prefix);
    }
    public void setIpPrefix6List(String is_prefix, Integer metric) {
        IPPrefix prefix = new IPPrefix(is_prefix, metric);
        ipPrefixList6.add(prefix);
    }

    // TLV:
    // ###############################################################
    public AreaAddresses getTLV_AreaAddresses() {
        if(AreaAddress == null)
            setTLV_AreaAddresses(new AreaAddresses("Pseudo-node"));
        return AreaAddress;
    }

    public void setTLV_AreaAddresses(AreaAddresses areaAddress) {
        this.AreaAddress = areaAddress;
    }

    public LSPBufferSize getLspBufferSize() {
        if(lspBufferSize == null)
            setLspBufferSize(new LSPBufferSize(0));
        return lspBufferSize;
    }

    public void setLspBufferSize(LSPBufferSize lspBufferSize) {
        this.lspBufferSize = lspBufferSize;
    }

    public void setHostname(Hostname hostname) {
        this.hostname = hostname;
    }

    public Hostname getHostname() {
        if(hostname == null)
            setHostname(new Hostname("Pseudo-node"));
        return hostname;
    }

    public void setTe_routerID(TE_RouterID te_routerID) {
        this.te_routerID = te_routerID;
    }

    public TE_RouterID getTe_routerID() {
        if(te_routerID == null)
            setTe_routerID(new TE_RouterID("Pseudo-node"));
        return te_routerID;
    }

    public void setSpeaks(String speaks) {
        if(protocolSupporteds == null)
            protocolSupporteds = new ArrayList<ProtocolSupported>();
        protocolSupporteds.add(new ProtocolSupported(speaks));
    }

    public ArrayList<ProtocolSupported> getProtocolSupported() {
        if(protocolSupporteds == null) {
            protocolSupporteds = new ArrayList<ProtocolSupported>();
            protocolSupporteds.add(new ProtocolSupported("Pseudo-node"));
            protocolSupporteds.add(new ProtocolSupported("Pseudo-node"));
        }
        return protocolSupporteds;
    }

    public void setIp_address(IP_Address ip_address) {
        this.ip_address = ip_address;
    }

    public IP_Address getIP_Address() {
        if(ip_address == null)
            ip_address = new IP_Address("Pseudo-node");
        return ip_address;
    }
    // #################################################################

    public void setHeader(Header header) {
        this.header = header;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public String getLSP_ID(){
        return LSP_ID;
    }

    public String getSequence() {
        return Sequence;
    }

    public String getChecksum() {
        return Checksum;
    }

    public Integer getLifetime() {
        return Lifetime;
    }

    public ArrayList<isNeighbor> getIsNeighborList() {
        return isNeighborList;
    }

    public ArrayList<IPPrefix> getIpPrefixList() {
        return ipPrefixList;
    }

    public ArrayList<IPPrefix> getIpPrefixList6() {
        return ipPrefixList6;
    }

    public Header getHeader() {
        return header;
    }

    public Packet getPacket() {
        return packet;
    }
}