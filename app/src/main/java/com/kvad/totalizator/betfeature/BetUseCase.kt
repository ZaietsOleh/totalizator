package com.kvad.totalizator.betfeature

import android.util.Log
import com.kvad.totalizator.data.BetRepository
import com.kvad.totalizator.tools.safeapicall.ApiResultWrapper
import javax.inject.Inject

//TODO add verify with wallet
class BetUseCase @Inject constructor(
    private val betRepository: BetRepository,
    private val mapperBetModelToBetRequest: MapperBetModelToBetRequest
) {
    suspend fun bet(betToServerModel: BetToServerModel) : ApiResultWrapper<Unit> {
        val betRequest = mapperBetModelToBetRequest.map(betToServerModel)
        Log.d("TAG","betRequest -- ${betRequest.eventId}")
        return betRepository.doBet(betRequest)
    }
}
