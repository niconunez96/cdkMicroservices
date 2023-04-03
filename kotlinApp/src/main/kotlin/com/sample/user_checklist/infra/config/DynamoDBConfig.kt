package com.sample.user_checklist.infra.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

@Configuration
class DynamoDBConfig {
    @Value("\${app.env}")
    private val env: String? = null

    fun getCredentials(): AwsCredentialsProvider {
        logger.info("ENVIRONMENT: $env")
        if (env == "DEV") {
            logger.info("Using profile credentials provider")
            return DefaultCredentialsProvider.builder().profileName("localstack").build()
        }
        logger.info("Using default aws credentials provider")
        return DefaultCredentialsProvider.builder().build()
    }

    @Bean
    fun dynamoDbClient(): DynamoDbClient {
        return if (env == "DEV") {
            DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:4566"))
                .credentialsProvider(getCredentials())
                .region(Region.US_EAST_1)
                .build()
        } else {
            DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(getCredentials())
                .build()
        }
    }

    @Bean
    fun dynamoDbEnhancedClient(): DynamoDbEnhancedClient {
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient())
            .build()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}