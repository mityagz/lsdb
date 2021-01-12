package lsdbJunos;

/**
 * Created by mitya on 12/4/20.
 */
public class isNeighbor {
    private String isNeighborName;
    private Integer metric;
    private String Two_way_fragment;
    private String Two_way_first_fragment;

    public isNeighbor(String neib, Integer metric) {
        this.isNeighborName = neib;
        this.metric = metric;
    }

    public String toString() {
        return isNeighborName + " : " + metric;
    }

    public String getIsNeighborName() {
        return isNeighborName;
    }

    public Integer getMetric() {
        return metric;
    }
}
