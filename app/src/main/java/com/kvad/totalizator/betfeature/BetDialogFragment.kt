package com.kvad.totalizator.betfeature

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kvad.totalizator.App
import com.kvad.totalizator.R
import com.kvad.totalizator.betfeature.model.BetDetail
import com.kvad.totalizator.betfeature.model.BetToServerModel
import com.kvad.totalizator.databinding.BetDialogFragmentBinding
import com.kvad.totalizator.shared.Bet
import com.kvad.totalizator.tools.ErrorState
import com.kvad.totalizator.tools.State
import com.kvad.totalizator.tools.StateVisibilityController
import javax.inject.Inject

@Suppress("TooManyFunctions")
class BetDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModel: BetViewModel

    private lateinit var binding: BetDialogFragmentBinding
    private lateinit var detailBet: Bet
    private var eventId: String = ""
    private lateinit var stateVisibilityController: StateVisibilityController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BetDialogFragmentBinding.inflate(inflater, container, false)
        arguments?.let {
            val safeArgs = BetDialogFragmentArgs.fromBundle(it).bet
            detailBet = safeArgs
        }
        stateVisibilityController =
            StateVisibilityController(binding.progressBarCircular, binding.tvError)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDi()
        setupListeners()
        setupTextWatcher()
        viewModel.uploadData()
        observeBetInfoLiveData()
        observeDoBetLiveData()
    }

    private fun setupDi() {
        val app = requireActivity().application as App
        app.getComponent().inject(this)
    }

    private fun setupListeners() {
        binding.tvCancel.setOnClickListener {
            cancelBetDialog()
        }
        binding.btnBet.setOnClickListener {
            val amount = binding.etBet.text.toString().toDouble()
            val betToServerModel = BetToServerModel(
                eventId, amount, detailBet
            )
            viewModel.createBet(betToServerModel)
        }
        binding.vClose.setOnClickListener {
            cancelBetDialog()
        }
    }

    private fun observeDoBetLiveData() {
        viewModel.betLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> stateVisibilityController.showLoading()
                is State.Content -> {
                    stateVisibilityController.hideAll()
                    setupDoBetResult()
                }
                is State.Error -> setupError(it.error)
            }
        }
    }

    private fun setupError(error: ErrorState){
        when(error){
            ErrorState.LOGIN_ERROR -> findNavController().navigate(R.id.login_fragment)
            ErrorState.LOADING_ERROR -> findNavController().popBackStack()
        }
    }

    private fun setupDoBetResult() {
        binding.apply {
            etBet.visibility = View.GONE
            tvCancel.visibility = View.GONE
            tvBetGood.visibility = View.VISIBLE
            vClose.visibility = View.VISIBLE
            btnBet.isEnabled = false
            val color = ContextCompat.getColor(requireContext(), R.color.light_grey)
            btnBet.setBackgroundColor(color)
            hideKeyBoard()
        }
    }

    private fun observeBetInfoLiveData() {
        viewModel.betInfoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> stateVisibilityController.showLoading()
                is State.Content -> {
                    stateVisibilityController.hideAll()
                    setupBetInfo(it)
                }
                is State.Error -> {
                    stateVisibilityController.showError()
                }
            }
        }
    }

    private fun setupBetInfo(state: State.Content<BetDetail>) {
        eventId = state.data.eventId
        binding.apply {
            tvGameDetails.text = getString(
                R.string.event_vs,
                state.data.firstPlayerName,
                state.data.secondPlayerName
            )
            tvWinnerName.text = when (detailBet) {
                Bet.DRAW -> getString(R.string.draw)
                Bet.SECOND_PLAYER_WIN -> state.data.firstPlayerName
                Bet.FIRST_PLAYER_WIN -> state.data.secondPlayerName
            }
        }
    }

    private fun setupTextWatcher() {
        binding.amountLayout.error = getString(R.string.min_bet)
        binding.etBet.requestFocus()
        binding.etBet.doOnTextChanged { text, _, _, _ ->
            when {
                text?.isEmpty() == true -> {
                    binding.apply {
                        amountLayout.error = getString(R.string.min_bet)
                        btnBet.isEnabled = false
                        val color = ContextCompat.getColor(requireContext(), R.color.light_grey)
                        btnBet.setBackgroundColor(color)
                        btnBet.text = getString(R.string.do_bet_simple)
                        tvPraise.visibility = View.INVISIBLE
                    }
                }
                text?.length == 1 -> {
                    val coefficient =
                        viewModel.calculate(detailBet, binding.etBet.text.toString().toFloat())
                    val possibleGain = (coefficient * binding.etBet.text.toString().toFloat())
                    binding.apply {
                        val color = ContextCompat.getColor(requireContext(), R.color.light_grey)
                        amountLayout.error = getString(R.string.min_bet)
                        btnBet.isEnabled = false
                        btnBet.setBackgroundColor(color)
                        btnBet.text =
                            getString(R.string.do_bet, binding.etBet.text.toString().toFloat())
                        tvPraise.visibility = View.VISIBLE
                        tvPraise.text = getString(R.string.possible_gain, possibleGain)
                    }
                }
                else -> {
                    val coefficient =
                        viewModel.calculate(detailBet, binding.etBet.text.toString().toFloat())
                    val possibleGain = (coefficient * binding.etBet.text.toString().toFloat())
                    binding.apply {
                        amountLayout.error = null
                        btnBet.isEnabled = true
                        val color = ContextCompat.getColor(requireContext(), R.color.yellow)
                        btnBet.setBackgroundColor(color)
                        btnBet.text =
                            getString(R.string.do_bet, binding.etBet.text.toString().toFloat())
                        tvPraise.visibility = View.VISIBLE
                        tvPraise.text = getString(R.string.possible_gain, possibleGain)
                    }
                }
            }
        }
    }

    private fun cancelBetDialog() {
        hideKeyBoard()
        dialog?.cancel()
        binding.etBet.clearFocus()
    }

    private fun hideKeyBoard() {
        val inputMethodManager =
            this.context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.applicationWindowToken, 0)
    }

}



