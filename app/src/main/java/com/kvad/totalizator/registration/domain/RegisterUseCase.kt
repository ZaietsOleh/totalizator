package com.kvad.totalizator.registration.domain

import android.util.Log
import com.kvad.totalizator.data.UserRepository
import com.kvad.totalizator.data.models.RegisterRequest
import com.kvad.totalizator.data.models.Token
import com.kvad.totalizator.login.LoginState
import com.kvad.totalizator.registration.RegisterState
import com.kvad.totalizator.registration.models.RawRegisterRequest
import com.kvad.totalizator.tools.ADULT
import com.kvad.totalizator.tools.LOGIN_MIN_LENGTH
import com.kvad.totalizator.tools.PASSWORD_MIN_LENGTH
import com.kvad.totalizator.tools.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    companion object {
        private const val MONTH_BEFORE_ADD_NULL = 9
    }

    private lateinit var state: RegisterState

    suspend fun register(rawRegisterRequest: RawRegisterRequest) = withContext(Dispatchers.Default) {

        val state = verifyRegisterComponent(rawRegisterRequest)
        if (state == RegisterState.WITHOUT_ERROR) {

            userRepository.register(toRegisterRequest(rawRegisterRequest)).doOnResult(
                onSuccess = ::doOnSuccess,
                onNetworkError = ::doOnNetworkError
            )
        }

        state
    }


    private fun doOnSuccess(token: Token) {
        userRepository.updateToken(token)
        state = RegisterState.WITHOUT_ERROR
    }

    private fun doOnNetworkError(error: ResultWrapper.Error) {
        Log.d("ERROR_TAG", error.msg)
        state = RegisterState.NETWORK_ERROR
    }

    private suspend fun verifyRegisterComponent(rawRegisterRequest: RawRegisterRequest) =
        withContext(Dispatchers.Default) {
            when {
                rawRegisterRequest.email.length < LOGIN_MIN_LENGTH -> RegisterState.LOGIN_LENGTH_ERROR
                rawRegisterRequest.password.length < PASSWORD_MIN_LENGTH -> RegisterState.PASSWORD_LENGTH_ERROR
                !isAdult(rawRegisterRequest) -> RegisterState.BIRTHDAY_ERROR
                else -> RegisterState.WITHOUT_ERROR
            }
        }

    private suspend fun isAdult(rawRegisterRequest: RawRegisterRequest) =
        withContext(Dispatchers.Default) {

            val calendarAdult = Calendar.getInstance()
            calendarAdult.add(Calendar.YEAR, ADULT.inv())
            val calendarBirthday = Calendar.getInstance()
            calendarBirthday.set(
                rawRegisterRequest.year,
                rawRegisterRequest.month,
                rawRegisterRequest.day
            )

            calendarAdult >= calendarBirthday
        }

    private suspend fun toRegisterRequest(rawRegisterRequest: RawRegisterRequest) = withContext(Dispatchers.Default) {
        RegisterRequest(
            email = rawRegisterRequest.email,
            password = rawRegisterRequest.password,
            dob = "${rawRegisterRequest.year}-${
                if (rawRegisterRequest.month > MONTH_BEFORE_ADD_NULL) rawRegisterRequest.month
                else "0${rawRegisterRequest.month}"
            }-${rawRegisterRequest.day}"
        )
    }
}
