package cn.globalph.common.cache;

public interface StatisticsService {
    void addCacheStat(String key, boolean isHit);

    Long getLogResolution();

    void setLogResolution(Long logResolution);

    void activateLogging();

    void disableLogging();
}
