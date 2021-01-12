package isisTLV;

/**
 * Created by mitya on 12/5/20.
 */
public class LSPBufferSize {
    private Integer TLV =  14;
    private Integer LSPBufferSize;

    public LSPBufferSize(Integer lspBufferSize) {
        this.LSPBufferSize = lspBufferSize;
    }

    public Integer getLSPBufferSize() {
        return LSPBufferSize;
    }

    public void setLSPBufferSize(Integer lspBufferSize) {
        this.LSPBufferSize = lspBufferSize;
    }
}

