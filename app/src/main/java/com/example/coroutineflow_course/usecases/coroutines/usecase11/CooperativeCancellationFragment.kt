package com.example.coroutineflow_course.usecases.coroutines.usecase11

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.coroutineflow_course.R
import com.example.coroutineflow_course.databinding.FragmentCooperativeCancellationBinding
import kotlinx.coroutines.launch

class CooperativeCancellationFragment : Fragment() {

    private var _binding: FragmentCooperativeCancellationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CooperativeCancellationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentCooperativeCancellationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCooperativeCancellationCalculate.setOnClickListener {

            if (binding.edtCooperativeCancellation.text.toString().isEmpty()) {
                binding.edtCooperativeCancellation.error = "Please enter a number"
                return@setOnClickListener
            }

            viewModel.performCalculation(
                binding.edtCooperativeCancellation.text.toString().toInt()
            )
        }

        binding.btnCooperativeCancellationCancel.setOnClickListener {
            viewModel.cancelCalculation()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is UiState.Loading -> onLoad()
                        is UiState.Success -> onSuccess(state)
                        is UiState.Error -> onError(state)
                    }
                }
            }
        }

    }

    private fun onLoad() {
        binding.pbCooperativeCancellation.visibility = View.VISIBLE
        binding.tvCooperativeCancellationFactorialTime.text = ""
        binding.tvCooperativeCancellationConversionTime.text = ""
        binding.tvCooperativeCancellationFactorialResult.text = ""

        disableButtonCalculate()
        enableButtonCancel()
    }

    private fun onSuccess(uiState: UiState.Success) {
        disableButtonCancel()
        enableButtonCalculate()

        binding.pbCooperativeCancellation.visibility = View.GONE

        binding.tvCooperativeCancellationFactorialTime.text =
            getString(R.string.duration_calculation, uiState.computationDuration)

        binding.tvCooperativeCancellationConversionTime.text =
            getString(R.string.duration_string_conversion, uiState.stringConversionDuration)

        binding.tvCooperativeCancellationFactorialResult.text =
            if (uiState.result.length <= 150) {
                uiState.result
            } else {
                "${uiState.result.substring(0, 147)}..."
            }
    }

    private fun onError(uiState: UiState.Error) {
        binding.pbCooperativeCancellation.visibility = View.GONE
        Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()

        disableButtonCancel()
        enableButtonCalculate()
    }

    private fun enableButtonCalculate() {
        binding.btnCooperativeCancellationCalculate.isEnabled = true
    }

    private fun disableButtonCalculate() {
        binding.btnCooperativeCancellationCalculate.isEnabled = false
    }

    private fun enableButtonCancel() {
        binding.btnCooperativeCancellationCancel.isEnabled = true
    }

    private fun disableButtonCancel() {
        binding.btnCooperativeCancellationCancel.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}