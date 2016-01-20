package cn.globalph.common.time;

public class DefaultTimeSource implements TimeSource {

    public long timeInMillis() {
        return System.currentTimeMillis();
    }
}
