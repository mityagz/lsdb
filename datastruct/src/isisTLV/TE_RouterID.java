package isisTLV;

/**
 * Created by mitya on 1/11/21.
 */
public class TE_RouterID {
    private Integer TLV = 134;
    private String ipRouterID;

    public TE_RouterID(String ipRouterID) {
        this.ipRouterID = ipRouterID;
    }

    public void setIpRouterID(String ipRouterID) {
        this.ipRouterID = ipRouterID;
    }

    public String getIpRouterID() {
        return ipRouterID;
    }
}
