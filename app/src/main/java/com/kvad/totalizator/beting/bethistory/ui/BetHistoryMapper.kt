package com.kvad.totalizator.beting.bethistory.ui

import com.kvad.totalizator.beting.bethistory.model.BetHistoryDetailModel
import com.kvad.totalizator.beting.bethistory.model.RequestBetHistoryModel
import com.kvad.totalizator.di.DefaultDispatcher
import com.kvad.totalizator.di.MainDispatcher
import com.kvad.totalizator.tools.W1_BET_CHOICE
import com.kvad.totalizator.tools.W2_BET_CHOICE
import com.kvad.totalizator.tools.DRAW_BET_CHOICE
import com.kvad.totalizator.tools.W2_SERVER_FLAG
import com.kvad.totalizator.tools.W1_SERVER_FLAG
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.LocalDateTime


import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import javax.inject.Inject

class BetHistoryMapper @Inject constructor(
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun mapItem(requestBetHistoryModel: RequestBetHistoryModel): BetHistoryDetailModel =
        withContext(dispatcher) {
            return@withContext BetHistoryDetailModel(
                id = requestBetHistoryModel.betId,
                teamConfrontation = requestBetHistoryModel.teamConfrontation,
                choice = when (requestBetHistoryModel.choice) {
                    W1_SERVER_FLAG -> W1_BET_CHOICE
                    W2_SERVER_FLAG -> W2_BET_CHOICE
                    else -> DRAW_BET_CHOICE
                },
                eventStartTime = parseZonedDateTime(requestBetHistoryModel.eventStartTime),
                betStartTime = parseZonedDateTime(requestBetHistoryModel.betTime),
                amount = requestBetHistoryModel.amount,
                status = requestBetHistoryModel.status,
            )
        }

    suspend fun map(betHistoryPreview: List<RequestBetHistoryModel>): List<BetHistoryDetailModel> {
        return betHistoryPreview.map { mapItem(it) }
    }

    private fun parseZonedDateTime(time: String): String {

        val timeParsed = LocalDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        val month =
            if (timeParsed.monthValue.toString().length < 2) "0${timeParsed.monthValue}" else "${timeParsed.monthValue}"
        val day =
            if (timeParsed.dayOfMonth.toString().length < 2) "0${timeParsed.dayOfMonth}" else "${timeParsed.dayOfMonth}"
        val hour =
            if (timeParsed.hour.toString().length < 2) "0${timeParsed.hour}" else "${timeParsed.hour}"
        val minute =
            if (timeParsed.minute.toString().length < 2) "0${timeParsed.minute}" else "${timeParsed.minute}"

        return "$day.$month, $hour:$minute"
    }

}

