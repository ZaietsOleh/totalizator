package com.kvad.totalizator.data

import com.kvad.totalizator.data.api.UserService
import com.kvad.totalizator.data.models.LoginRequest
import com.kvad.totalizator.data.models.RegisterRequest
import com.kvad.totalizator.data.models.Token
import com.kvad.totalizator.data.models.Wallet
import com.kvad.totalizator.tools.REQUEST_DELAY
import com.kvad.totalizator.tools.ResultWrapper
import com.kvad.totalizator.tools.safeApiCall
import com.kvad.totalizator.tools.sharedPrefTools.SharedPref
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService,
    private val sharedPref: SharedPref
) {
    suspend fun login(loginRequest: LoginRequest): ResultWrapper<Token> {
        return safeApiCall {
            userService.login(loginRequest)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): ResultWrapper<Token> {
        return safeApiCall {
            userService.register(registerRequest)
        }
    }

    suspend fun wallet(): Flow<ResultWrapper<Wallet>> = flow {
        while (true) {
            emit(safeApiCall(userService::wallet))
            kotlinx.coroutines.delay(REQUEST_DELAY)
        }
    }

    fun updateToken(token: Token) {
        sharedPref.token = token.token
    }
}
