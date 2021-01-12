package lsdbJunos;

/**
 * Created by mitya on 12/5/20.
 */
public class Packet {
    private String LSP_ID;
    private Integer Length;
    private Integer Lifetime;
    private String Checksum;
    private String Sequence;
    private String Attributes;
    private String NLPID;
    private Integer Fixed_length;
    private Integer Version;
    private Integer Sysid_length;
    private Integer Packet_type;
    private Integer Packet_version;
    private Integer Max_area;

    public Packet(String lsp_id, Integer length, Integer lifetime) {
        this.LSP_ID = lsp_id;
        this.Length = length;
        this.Lifetime = lifetime;
    }

    public void setChecksum(String checksum) {
        this.Checksum = checksum;
    }

    public void setSequence(String sequence) {
        this.Sequence = sequence;
    }

    public void setAttributes(String attributes) {
        this.Attributes = attributes;
    }

    public void setNLPID(String nlpid) {
        this.NLPID = nlpid;
    }

    public void setFixed_length(Integer fixed_length) {
        this.Fixed_length = fixed_length;
    }

    public void setVersion(Integer version) {
        this.Version = version;
    }

    public void setSysid_length(Integer sysid_length) {
        this.Sysid_length = sysid_length;
    }

    public void setPacket_version(Integer packet_version) {
        this.Packet_version = packet_version;
    }

    public void setPacket_type(Integer packet_type) {
        this.Packet_type = packet_type;
    }

    public void setMax_area(Integer max_area) {
        this.Max_area = max_area;
    }

    public String toString() {
        return "Packet:\n" + LSP_ID + "\n" +
        "Length: " + Length + "\n" +
        "Lifetime: " + Lifetime + "\n" +
        "Checksum: " +  Checksum + "\n" +
        "Sequence: " + Sequence + "\n" +
        "Attributes: " + Attributes + "\n" +
        "NLPID: " + NLPID + "\n" +
        "Fixed_length: " + Fixed_length + "\n" +
        "Version: " + Version + "\n" +
        "Sysid_length: " + Sysid_length + "\n" +
        "Packet_type: " + Packet_type + "\n" +
        "Packet_version: " + Packet_version + "\n" +
        "Max_area: " + Max_area + "\n";
    }
}
