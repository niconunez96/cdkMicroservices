package com.sample.user_checklist.endpoints

import com.sample.user_checklist.application.ChecklistTaskCompleter
import com.sample.user_checklist.application.TaskName
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.aws.messaging.listener.Acknowledgment
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.stereotype.Component

@JsonIgnoreProperties(ignoreUnknown = true)
data class SQSMessage(val TopicArn: String, val Message: String) {
    private val TOPIC_REGEX = """arn:aws:sns:us-east-1:[0-9]+:(.+)""".toRegex()
    inline fun <reified T> toDataclass(): T = ObjectMapper().readValue(Message, T::class.java)

    val domainEventName: String?
        get() = TOPIC_REGEX.find(TopicArn)?.groupValues?.get(1)
}

@Component
class SQSConsumer(
    val jsonMapper: ObjectMapper,
    val checklistTaskCompleter: ChecklistTaskCompleter,
) {
    @SqsListener("DomainEvents", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    fun processMessage(message: String, ack: Acknowledgment, @Headers headers: Map<String, String>) {
        logger.info(message)
        val sqsMessage = jsonMapper.readValue(message, SQSMessage::class.java)
        val userId = sqsMessage.toDataclass<Map<String, String>>()["userId"]
        val domainEventName = sqsMessage.domainEventName
        logger.info("USER ID $userId")
        logger.info("DOMAIN EVENT NAME $domainEventName")
        if (userId != null && domainEventName != null) {
            checklistTaskCompleter.completeTask(userId, TaskName.valueOf(domainEventName))
        }
        ack.acknowledge()
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
