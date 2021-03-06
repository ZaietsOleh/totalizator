package com.kvad.totalizator.account.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvad.totalizator.account.data.model.LoginRequest
import com.kvad.totalizator.account.login.domain.LoginUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginStateViewModel = MutableLiveData<LoginState>()
    val loginStateViewModel: LiveData<LoginState> = _loginStateViewModel

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _loginStateViewModel.value = loginUseCase.login(loginRequest)
        }
    }
}
