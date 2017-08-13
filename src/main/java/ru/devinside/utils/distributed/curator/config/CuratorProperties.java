package ru.devinside.utils.distributed.curator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CuratorProperties {
    @Value("${curator.servers}")
    private String servers;

    @Value("${curator.namespace}")
    private String namespace;

    @Value("${curator.connection.timeout.ms:5000}")
    private Integer connectionTimeoutMs;

    @Value("${curator.session.timeout.ms:5000}")
    private Integer sessionTimeoutMs;

    @Value("${curator.max.close.wait.ms:1000}")
    private Integer maxCloseWaitMs;

    @Value("${curator.retries.base.sleep.time.ms:1000}")
    private int baseSleepTimeMs;

    @Value("${curator.retries.max:3}")
    private int maxRetries;

    public String getServers() {
        return servers;
    }

    public String getNamespace() {
        return namespace;
    }

    public Integer getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public Integer getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public Integer getMaxCloseWaitMs() {
        return maxCloseWaitMs;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }
}
