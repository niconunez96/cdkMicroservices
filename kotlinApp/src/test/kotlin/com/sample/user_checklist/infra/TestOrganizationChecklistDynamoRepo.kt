package com.sample.user_checklist.infra

import com.sample.user_checklist.domain.UserChecklist
import com.sample.user_checklist.domain.Status
import com.sample.user_checklist.infra.config.DynamoDBConfig
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@ContextConfiguration(classes = [UserChecklistDynamoRepo::class, DynamoDBConfig::class])
@TestPropertySource("classpath:application.properties")
class TestOrganizationChecklistDynamoRepo(repo: UserChecklistDynamoRepo) : StringSpec() {

    init {
        "it should store organization checklist" {
            repo.store(UserChecklist("1", profileCompleted = Status.COMPLETED))
            val organization = repo.find("1")
            organization shouldNotBe null
        }
    }
}
