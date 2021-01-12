package lsdbJunos;

/**
 * Created by mitya on 12/4/20.
 */
public class IPPrefix {
    private String prefix;
    private Integer metric;
    private String type;
    private String status;

    public IPPrefix(String prefix, Integer metric) {
        this.prefix = prefix;
        this.metric = metric;
    }

    public String toString() {
        return prefix + " : " + metric;
    }
}
