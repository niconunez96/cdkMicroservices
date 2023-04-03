package com.sample.in_memory_repos

import com.sample.user_checklist.domain.UserChecklist
import com.sample.user_checklist.domain.UserChecklistRepo


open class InMemoryRepo<T, ID> {
    private val entities = mutableMapOf<ID, T>()
    protected fun store(id: ID, entity: T) {
        entities[id] = entity
    }

    protected fun _find(id: ID): T? = entities[id]
}


class UserChecklistInMemoryRepo : UserChecklistRepo, InMemoryRepo<UserChecklist, String>() {
    override fun store(checklist: UserChecklist) {
        super.store(checklist.id, checklist)
    }

    override fun find(userId: String): UserChecklist? = super._find(userId)
}
