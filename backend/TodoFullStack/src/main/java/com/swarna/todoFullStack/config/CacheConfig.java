package com.swarna.todoFullStack.config;

// import org.springframework.context.annotation.Bean;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    // @Bean
    public Config cacheConfig() {
        return new Config()
                .setInstanceName("hazle-instance") // instance name for caching
                .addMapConfig(new MapConfig() // Adding one cache, we can configure as many cache as we want
                        .setName("todo-cache") // cache will be saved with this name
                        .setTimeToLiveSeconds(3000)); // cache will be evicted after 5 minutes
    }

}
