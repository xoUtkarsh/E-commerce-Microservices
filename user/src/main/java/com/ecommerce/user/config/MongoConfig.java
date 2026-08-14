package com.ecommerce.user.config;



import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.connection-string}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database:userdb}")
    private String databaseName;

    @Bean
    public MongoClient mongoClient() {
        System.out.println("=== MongoDB Configuration ===");
        System.out.println("Connecting to database: " + databaseName);
        return MongoClients.create(mongoUri);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient, databaseName));
    }
}
