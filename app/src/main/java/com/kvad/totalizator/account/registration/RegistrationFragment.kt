package com.kvad.totalizator.account.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.kvad.totalizator.App
import com.kvad.totalizator.R
import com.kvad.totalizator.databinding.RegistrationFragmentBinding
import com.kvad.totalizator.di.ViewModelFactory
import com.kvad.totalizator.di.injectViewModel
import com.kvad.totalizator.account.registration.models.RawRegisterRequest
import java.util.Date
import javax.inject.Inject

@Suppress("TooManyFunctions")
class RegistrationFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: RegistrationViewModel
    private var _binding: RegistrationFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDi()
        setupMaxDate()
        setupListener()
        setupLiveDataObserver()
    }

    private fun setupLiveDataObserver() {
        viewModel.registerStateLiveData.observe(viewLifecycleOwner) {
            updateState(it)
        }
    }

    private fun updateState(state: RegisterState) {
        hideErrorInput()
        when (state) {
            RegisterState.WITHOUT_ERROR -> findNavController().navigate(R.id.action_to_event_from_login)
            RegisterState.BIRTHDAY_ERROR -> showDialogError(R.string.register_birthday_error)
            RegisterState.NETWORK_ERROR -> showDialogError(R.string.register_network_error_dialog_body)
            RegisterState.PASSWORD_LENGTH_ERROR -> {
                binding.tfPassword.error = getString(R.string.login_password_error)
            }
            RegisterState.USERNAME_ERROR -> {
                binding.tfUsername.error = getString(R.string.login_username_error)
            }
            RegisterState.EMAIL_ERROR -> {
                binding.tfEmail.error = getString(R.string.login_email_validation_error)
            }
        }
    }

    private fun hideErrorInput() {
        binding.apply {
            tfUsername.error = null
            tfEmail.error = null
            tfPassword.error = null
        }
    }

    private fun showDialogError(@StringRes res: Int) {
        MaterialDialog(requireContext()).show {
            message(res)
            negativeButton(R.string.register_error_dialog_button)
        }
    }

    private fun setupMaxDate() {
        binding.dpBirthday.maxDate = Date().time
    }

    private fun setupListener() {
        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        binding.apply {
            viewModel.register(
                RawRegisterRequest(
                    username = teUsername.text.toString(),
                    email = teEmail.text.toString(),
                    password = tePassword.text.toString(),
                    day = dpBirthday.dayOfMonth,
                    month = dpBirthday.month + 1,
                    year = dpBirthday.year
                )
            )
        }
    }

    private fun setupDi() {
        val app = requireActivity().application as App
        app.getComponent().inject(this)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onDestroyView() {
        binding.root.layoutAnimation = null
        _binding = null
        super.onDestroyView()
    }
}
