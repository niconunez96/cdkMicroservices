package com.sample.user_checklist.endpoints

import com.sample.user_checklist.infra.UserChecklistDynamoRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class ChecklistController {

    @Autowired
    lateinit var userChecklistDynamoRepo: UserChecklistDynamoRepo

    @GetMapping("users/{userId}/tasks/")
    fun getChecklistTasks(@PathVariable userId: String): ResponseEntity<*> {
        val checklist = userChecklistDynamoRepo.find(userId)
        if (checklist != null)
            return ResponseEntity.ok(checklist.toResponse())
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND")
    }

    @GetMapping("health")
    fun healthCheck(): ResponseEntity<*> {
        return ResponseEntity.ok("OK")
    }
}
