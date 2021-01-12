package lsdbJunos;

import java.util.ArrayList;

/**
 * Created by mitya on 12/5/20.
 */
public class Header {
    private String LSP_ID;
    private Integer Length;
    private Integer Allocated_length;
    private String Router_ID;
    private Integer Remaining_lifetime;
    private ArrayList<Integer> Level;
    private Integer Interface;
    private Integer Estimated_free_bytes;
    private Integer Actual_free_bytes;
    private Integer Aging_timer_expires_in;
    private ArrayList<String> Protocols;


    public Header(String lsp_id, Integer length) {
        this.LSP_ID = lsp_id;
        this.Length = length;
    }

    public void setAllocated_length(Integer length) {
        this.Allocated_length = length;
    }

    public void setRouter_ID(String router_ID) {
        this.Router_ID = router_ID;
    }

    public void setRemaining_lifetime(Integer remaining_lifetime) {
        this.Remaining_lifetime = remaining_lifetime;
    }

    public void setLevel(Integer level) {
        //this.Level = level;
    }

    public void setInterface(Integer Interface) {
        this.Interface = Interface;
    }

    public void setEstimated_free_bytes(Integer estimated_free_bytes) {
        this.Estimated_free_bytes = estimated_free_bytes;
    }

    public void setActual_free_bytes(Integer actual_free_bytes) {
        this.Actual_free_bytes = actual_free_bytes;
    }

    public void setAging_timer_expires_in(Integer aging_timer_expires_in) {
        this.Aging_timer_expires_in = aging_timer_expires_in;
    }

    public void setProtocols() {
        //this.setProtocols();
    }

    public String getLSP_ID() {
        return LSP_ID;
    }

    public String toString() {
        return "Header:\n" +
        "LSP_ID:" + LSP_ID + "\n" +
        "Length: " + Length + "\n" +
        "Allocated_length: " + Allocated_length + "\n" +
        "Router_ID: " + Router_ID + "\n" +
        "Remaining_lifetime: " + Remaining_lifetime + "\n" +
        //private ArrayList<Integer> Level;
        "Interface: " + Interface + "\n" +
        "Estimated_free_bytes: " + Estimated_free_bytes + "\n" +
        "Actual_free_bytes: " + Actual_free_bytes + "\n" +
        "Aging_timer_expires_in: " + Aging_timer_expires_in;
        //private ArrayList<String> Protocols;
    }
}
