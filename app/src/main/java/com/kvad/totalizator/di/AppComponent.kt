package com.kvad.totalizator.di

import com.kvad.totalizator.MainActivity
import com.kvad.totalizator.betfeature.BetDialogFragment
import com.kvad.totalizator.chat.ui.ChatFragment
import com.kvad.totalizator.detail.EventDetailFragment
import com.kvad.totalizator.events.EventsFragment
import com.kvad.totalizator.header.HeaderFragment
import com.kvad.totalizator.login.LoginFragment
import com.kvad.totalizator.onboard.OnBoardFragment
import com.kvad.totalizator.registration.RegistrationFragment
import com.kvad.totalizator.transactionfeature.DepositPageFragment
import com.kvad.totalizator.transactionfeature.TransactionPagerFragment
import com.kvad.totalizator.transactionfeature.WithdrawPageFragment
import dagger.Component
import javax.inject.Singleton
@Suppress("TooManyFunctions")
@Singleton
@Component(
    dependencies = [],
    modules = [AppModule::class, DispatcherModule::class]
)
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: HeaderFragment)

    fun inject(fragment: EventsFragment)

    fun inject(fragment: OnBoardFragment)

    fun inject(fragment: EventDetailFragment)

    fun inject(fragment: BetDialogFragment)

    fun inject(fragment: LoginFragment)

    fun inject(fragment: RegistrationFragment)

    fun inject(fragment: ChatFragment)

    fun inject(fragment: TransactionPagerFragment)

    fun inject(fragment: WithdrawPageFragment)

    fun inject(fragment: DepositPageFragment)

}
