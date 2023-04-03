package com.sample.user_checklist.application

import com.sample.user_checklist.domain.Status
import com.sample.user_checklist.domain.UserChecklist
import com.sample.user_checklist.domain.UserChecklistRepo
import org.springframework.stereotype.Service

enum class TaskName {
    ProfileCompleted
}

@Service
class ChecklistTaskCompleter(val repo: UserChecklistRepo) {

    private fun createNewChecklist(userId: String, taskName: TaskName): UserChecklist {
        return when (taskName) {
            TaskName.ProfileCompleted -> {
                UserChecklist(userId, profileCompleted = Status.COMPLETED)
            }
        }
    }

    fun completeTask(userId: String, taskName: TaskName) {
        val existentChecklist = repo.find(userId)
        val updatedChecklist =
            existentChecklist?.completeTask(taskName) ?: createNewChecklist(userId, taskName)
        repo.store(updatedChecklist)
    }
}
