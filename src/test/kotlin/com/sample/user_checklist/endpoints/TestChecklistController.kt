package com.sample.user_checklist.endpoints

import com.sample.user_checklist.domain.UserChecklist
import com.sample.user_checklist.domain.Status
import com.sample.user_checklist.infra.UserChecklistDynamoRepo
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
class TestChecklistController {
    @Autowired
    lateinit var repo: UserChecklistDynamoRepo

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @Test
    fun `it should return not found when there is no checklist for organization`() {
        mockMvc.get("/organizations/-1/tasks/")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun `it should return checklist that belongs to organization`() {
        repo.store(UserChecklist("1", publishEvent = Status.COMPLETED, campaignSent = Status.COMPLETED))

        mockMvc.get("/organizations/1/tasks/")
            .andExpect {
                status { isOk() }
                content {
                    json(
                        mapper.writeValueAsString(
                            mapOf(
                                "data" to mapOf(
                                    "organization_id" to "1",
                                    "publish_event" to Status.COMPLETED,
                                    "campaign_sent" to Status.COMPLETED
                                )
                            )
                        )
                    )
                }
            }
    }
}