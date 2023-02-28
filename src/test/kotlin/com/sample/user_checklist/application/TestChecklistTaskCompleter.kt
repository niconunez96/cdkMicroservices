package com.sample.user_checklist.application

import com.sample.in_memory_repos.UserChecklistInMemoryRepo
import com.sample.user_checklist.domain.UserChecklist
import com.sample.user_checklist.domain.Status
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.UUID

class TestChecklistTaskCompleter : StringSpec({
    val inMemoryRepo = UserChecklistInMemoryRepo()
    val checklistTaskCompleter = ChecklistTaskCompleter(inMemoryRepo)

    "it should complete the specified task" {
        val randomId = UUID.randomUUID().toString()

        checklistTaskCompleter.completeTask(randomId, TaskName.CampaignSent)

        inMemoryRepo.find(randomId)?.toResponse() shouldBe UserChecklist(
            randomId,
            campaignSent = Status.COMPLETED
        ).toResponse()
    }

    "it should not overwrite completed tasks" {
        val randomId = UUID.randomUUID().toString()
        inMemoryRepo.store(UserChecklist(randomId, publishEvent = Status.COMPLETED))

        checklistTaskCompleter.completeTask(randomId, TaskName.CampaignSent)

        inMemoryRepo.find(randomId)?.toResponse() shouldBe UserChecklist(
            randomId,
            campaignSent = Status.COMPLETED,
            publishEvent = Status.COMPLETED,
        ).toResponse()
    }
})
