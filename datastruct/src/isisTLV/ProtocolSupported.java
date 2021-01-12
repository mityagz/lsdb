package isisTLV;

/**
 * Created by mitya on 12/8/20.
 */
public class ProtocolSupported {
    private Integer TLV = 129;
    private String proto;

    public ProtocolSupported(String proto) {
        this.proto = proto;
    }

    public String getProto() {
        return proto;
    }
}
