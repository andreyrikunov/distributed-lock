package ru.devinside.utils.distributed.curator.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CuratorConfiguration {
    private static final Logger log = LoggerFactory.getLogger(CuratorConfiguration.class);

    private CuratorProperties curatorProperties;

    public CuratorConfiguration(CuratorProperties curatorProperties) {
        this.curatorProperties = curatorProperties;
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curatorFramework(RetryPolicy retryPolicy) {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(curatorProperties.getServers())
                .namespace(curatorProperties.getNamespace())
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(curatorProperties.getConnectionTimeoutMs())
                .sessionTimeoutMs(curatorProperties.getSessionTimeoutMs())
                .maxCloseWaitMs(curatorProperties.getMaxCloseWaitMs());

        log.info(
                "Curator namespace={}, servers={}, connectionTimeoutMs={}, sessionTimeoutMs={}",
                curatorProperties.getNamespace(),
                curatorProperties.getServers(),
                curatorProperties.getConnectionTimeoutMs(),
                curatorProperties.getSessionTimeoutMs()
        );

        return builder.build();
    }

    @Bean
    public RetryPolicy retryPolicy() {
        return new ExponentialBackoffRetry(curatorProperties.getBaseSleepTimeMs(), curatorProperties.getMaxRetries());
    }
}
