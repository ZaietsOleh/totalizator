package com.kvad.totalizator.data

import com.kvad.totalizator.data.api.EventService
import com.kvad.totalizator.data.models.BetPool
import com.kvad.totalizator.data.models.Characteristic
import com.kvad.totalizator.data.models.Event
import com.kvad.totalizator.data.models.ParticipantDTO
import com.kvad.totalizator.shared.ResultWrapper
import com.kvad.totalizator.tools.safeApiCall
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventService: EventService,
) {

    suspend fun getEvents(): ResultWrapper<List<Event>> {
        return safeApiCall {
            eventService.getEvents()
        }
    }

    @Suppress("MagicNumber")
    suspend fun getEventById(id: String): ResultWrapper<Event> {
        return safeApiCall {
            return@safeApiCall Event(
                "id",
                "sport",
                ParticipantDTO(
                    5, "Olexiy", "link", setOf(
                        Characteristic("weight", "55")
                    )
                ),
                ParticipantDTO(
                    5, "Olexiy", "link", setOf(
                        Characteristic("weight", "55")
                    )
                ),
                BetPool(55F, 56F, 56F)
            )
        }
    }
}
