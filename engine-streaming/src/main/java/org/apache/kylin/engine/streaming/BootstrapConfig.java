package org.apache.kylin.engine.streaming;

/**
 */
public class BootstrapConfig {

    private String streaming;
    private int partitionId = -1;

    private long start = 0L;
    private long end = 0L;

    private boolean fillGap;

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getStreaming() {
        return streaming;
    }

    public void setStreaming(String streaming) {
        this.streaming = streaming;
    }

    public boolean isFillGap() {
        return fillGap;
    }

    public void setFillGap(boolean fillGap) {
        this.fillGap = fillGap;
    }
}
