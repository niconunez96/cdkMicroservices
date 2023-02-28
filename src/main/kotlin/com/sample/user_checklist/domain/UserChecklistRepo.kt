package com.sample.user_checklist.domain

interface UserChecklistRepo {

    fun store(checklist: UserChecklist)

    fun find(userId: String): UserChecklist?
}