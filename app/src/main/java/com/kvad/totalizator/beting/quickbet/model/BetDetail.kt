package com.kvad.totalizator.beting.bethistory.quickbet.model

data class BetDetail(
    val firstPlayerName: String,
    val secondPlayerName: String,
    val firstPlayerAmount: Float,
    val secondPlayerAmount: Float,
    val drawAmount: Float,
    val margin: Float,
    val eventId: String,
)
