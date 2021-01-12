package lsdbJunos;

import java.util.HashMap;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

/**
 * Created by mitya on 1/11/21.
 */
public class Topo {
    protected boolean loop = true;
    int edge_count = 0;
    Graph graph = new SingleGraph("Topo");
    HashMap<String, isisLSP> tp;
    HashMap<String, Integer> nodeIndex;

    private String styleSheet =
            "graph {" +
                    "	canvas-color: white;" +
                    "		fill-mode: gradient-radial;" +
                    "		fill-color: white, #EEEEEE;" +
                    "		padding: 60px;" +
                    "	}" +
                    "node {" +
                    "	size-mode: dyn-size;" +
                    "	shape: circle;" +
                    "	size: 20px;" +
                    "	fill-mode: plain;" +
                    "	fill-color: #CCC;" +
                    "	stroke-mode: plain;" +
                    "	stroke-color: black;" +
                    "	stroke-width: 1px;" +
                    "}";

    public Topo(HashMap<String, isisLSP> tp) {
        this.tp = tp;
        HashMap<String, Integer> nodeIndex = new HashMap<String, Integer>();
        Integer nIndex = 0;

        System.setProperty("org.graphstream.ui", "swing");

        graph.addAttribute("ui.stylesheet", styleSheet);

        for(String node : tp.keySet()) {
            Node n = graph.addNode(node);
            n.addAttribute("ui.label", node);
            nodeIndex.put(node, nIndex);
            nIndex++;
        }


        for (String node : tp.keySet()) {
            for (isNeighbor neib : tp.get(node).getIsNeighborList()) {
                   if (graph.getEdge(node + "-" + neib.getIsNeighborName() + "-00") == null && graph.getEdge(neib.getIsNeighborName() + "-00" + "-" + node) == null) { //&& h != "0.0.0.0" && npp.get(nnp).getLo0() != "0.0.0.0") {
                       graph.addEdge(node + "-" + neib.getIsNeighborName() + "-00", nodeIndex.get(node), nodeIndex.get(neib.getIsNeighborName() + "-00"));
                   }
            }
        }
        Viewer viewer = graph.display();
    }
}