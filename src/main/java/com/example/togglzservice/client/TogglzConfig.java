package com.example.togglzservice.client;

import com.example.togglzservice.MyFeatures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.cache.CachingStateRepository;
import org.togglz.core.repository.jdbc.JDBCStateRepository;
import org.togglz.core.spi.FeatureProvider;
import org.togglz.core.user.NoOpUserProvider;
import org.togglz.core.user.UserProvider;

import javax.sql.DataSource;

@Configuration
@Profile("client")
public class TogglzConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    public FeatureProvider featureProvider() {
        return new EnumBasedFeatureProvider(MyFeatures.class);
    }

    @Bean
    public StateRepository stateRepository() {
        StateRepository rest = new RestStateRepository("http://localhost:8080/features-api/v1/state");
        return new CachingStateRepository(rest, 10000);
    }

    @Bean
    public UserProvider userProvider() {
        return new NoOpUserProvider();
        //return new ServletUserProvider("featuresAdminRole");
    }
}
