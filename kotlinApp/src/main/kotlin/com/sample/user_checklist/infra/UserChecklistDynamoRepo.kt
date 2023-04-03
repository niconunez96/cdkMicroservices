package com.sample.user_checklist.infra

import com.sample.user_checklist.domain.UserChecklist
import com.sample.user_checklist.domain.UserChecklistRepo
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Repository
class UserChecklistDynamoRepo(
    val dynamoClient: DynamoDbEnhancedClient
): UserChecklistRepo {

    override fun store(checklist: UserChecklist) {
        table().updateItem { it.item(checklist).ignoreNulls(true) }
    }

    override fun find(userId: String): UserChecklist? {
        return table().getItem(Key.builder().partitionValue(userId).build())
    }

    private fun table(): DynamoDbTable<UserChecklist> =
        dynamoClient.table(
            "UserChecklists",
            TableSchema.fromImmutableClass(UserChecklist::class.java)
        )
}
