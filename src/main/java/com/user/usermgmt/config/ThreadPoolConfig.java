package com.user.usermgmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    private static final String USER_TASK_EXECUTOR_PREFIX = "userTaskExecutor-";
    public static final String USER_TASK_EXECUTOR = "userTaskExecutor";

    @Bean(name = USER_TASK_EXECUTOR)
    public Executor userThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1000);
        executor.setMaxPoolSize(5000);
        executor.setThreadNamePrefix(USER_TASK_EXECUTOR_PREFIX);
        executor.initialize();
        return executor;
    }

   /* @Bean(name = "discountTaskExecutor")
    public Executor discountThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(25);
        executor.setThreadNamePrefix("discountTaskExecutor-");
        executor.initialize();
        return executor;
    }*/
}
