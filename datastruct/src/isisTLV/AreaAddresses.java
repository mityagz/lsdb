package isisTLV;

/**
 * Created by mitya on 12/5/20.
 */
public class AreaAddresses {
    private Integer TLV = 1;
    private String AreaAddress;

    public AreaAddresses(String areaAddress) {
        this.AreaAddress = areaAddress;
    }

    public void setAreaAddress(String AreaAddress) {
        this.AreaAddress = AreaAddress;
    }

    public String getAreaAddress() {
        return AreaAddress;
    }

    public Integer getTLVId() {
        return TLV;
    }
}
