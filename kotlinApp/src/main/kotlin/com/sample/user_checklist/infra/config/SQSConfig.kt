package com.sample.user_checklist.infra.config

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary


@Configuration
@EnableSqs
class SQSConfig {
    @Value("\${app.env}")
    private val env: String? = null

    @Bean
    fun queueMessagingTemplate(): QueueMessagingTemplate? {
        return QueueMessagingTemplate(amazonSQSAsync())
    }

    fun getCredentials(): AWSCredentialsProvider {
        logger.info("ENVIRONMENT: $env")
        if (env == "DEV") {
            logger.info("Using profile credentials provider")
            return ProfileCredentialsProvider("localstack")
        }
        logger.info("Using default aws credentials provider")
        return DefaultAWSCredentialsProviderChain()
    }

    @Bean
    @Primary
    fun amazonSQSAsync(): AmazonSQSAsync? {
        return if (env == "DEV") {
//            AmazonSQSClientBuilder
            AmazonSQSAsyncClientBuilder
                .standard()
                .withCredentials(getCredentials())
                .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
                .build()
        } else {
            AmazonSQSAsyncClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(getCredentials())
                .build()
        }
    }

    @Bean
    fun simpleMessageListenerContainerFactory(amazonSqs: AmazonSQSAsync?): SimpleMessageListenerContainerFactory? {
        val factory = SimpleMessageListenerContainerFactory()
        factory.setAmazonSqs(amazonSqs)
        factory.setAutoStartup(true)
        factory.setMaxNumberOfMessages(10)
        factory.setWaitTimeOut(10)
        factory.backOffTime = java.lang.Long.valueOf(60000)
        return factory
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}