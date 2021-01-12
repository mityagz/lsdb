package isisTLV;

/**
 * Created by mitya on 12/5/20.
 */
public class Hostname {
    private Integer TLV = 137;
    private String hostname;

    public Hostname(String hostname) {
        this.hostname = hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }
}
