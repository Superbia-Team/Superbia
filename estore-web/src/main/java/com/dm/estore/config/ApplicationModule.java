package com.dm.estore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * User: denis
 * Date: 11/1/13
 * Time: 9:18 PM
 */
@Configuration
@ComponentScan(basePackages = {
        "com.dm.estore",
        "com.estore"
})
public class ApplicationModule {
    // Declare "application" scope beans here (ie., beans that are not only used by the web context)

    //@Bean
    //public Configuration applicationConfiguration() { ... }
}
