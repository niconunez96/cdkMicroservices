package com.sample.user_checklist.domain

import com.sample.user_checklist.application.TaskName
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey


enum class Status {
    COMPLETED,
}

@DynamoDbImmutable(builder = UserChecklist.Builder::class)
class UserChecklist(
    @get:DynamoDbPartitionKey
    val id: String,
    val profileCompleted: Status? = null,
) {

    @DynamoDbIgnore
    fun completeTask(taskName: TaskName): UserChecklist {
        return when (taskName) {
            TaskName.ProfileCompleted -> UserChecklist(
                id,
                profileCompleted = Status.COMPLETED
            )
        }
    }

    @DynamoDbIgnore
    fun toResponse(): Map<String, Map<String, String>> = mapOf(
        "data" to mapOf(
            "userId" to id,
            "profileCompleted" to Status.COMPLETED.name
        )
    )

    class Builder {
        private var id: String? = null
        fun id(value: String) = apply { id = value }

        private var profileCompleted: Status? = null
        fun profileCompleted(value: Status) = apply { profileCompleted = value }

        fun build(): UserChecklist = UserChecklist(
            id!!,
            profileCompleted
        )
    }

    companion object {
        @JvmStatic
        fun builder() = Builder()
    }
}
