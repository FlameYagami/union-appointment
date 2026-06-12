package com.gk.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

/**
 * 异步线程池配置类
 *
 * @author GuoYu
 * @since 2022-10-09 10:09
 **/

@Configuration
@EnableAsync
public class SpringAsyncConfig {

    @Value("${async.executor.thread.core_pool_size}")
    private int corePoolSize;

    @Value("${async.executor.thread.max_pool_size}")
    private int maxPoolSize;

    @Value("${async.executor.thread.queue_capacity}")
    private int queueCapacity;

    @Value("${async.executor.thread.keepAliveSeconds}")
    private int keepAliveSeconds;

    @Value("${async.executor.thread.name.prefix}")
    private String namePrefix;

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(namePrefix);

        executor.setRejectedExecutionHandler(new CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
