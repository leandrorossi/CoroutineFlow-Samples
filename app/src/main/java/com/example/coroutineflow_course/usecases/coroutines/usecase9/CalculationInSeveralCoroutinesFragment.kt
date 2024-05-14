package com.example.coroutineflow_course.usecases.coroutines.usecase9

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
import com.example.coroutineflow_course.databinding.FragmentCalculationInSeveralCoroutinesBinding
import kotlinx.coroutines.launch

class CalculationInSeveralCoroutinesFragment : Fragment() {

    private var _binding: FragmentCalculationInSeveralCoroutinesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalculationInSeveralCoroutinesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCalculationInSeveralCoroutinesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCalculationSeveralCoroutine.setOnClickListener {

            if (binding.edtCalculationSeveralCoroutineFactorial.text.toString().isEmpty()) {
                binding.edtCalculationSeveralCoroutineFactorial.error = "Please enter a number"
                return@setOnClickListener
            }

            if (binding.edtCalculationSeveralCoroutineCoroutinesNumber.text.toString().isEmpty()) {
                binding.edtCalculationSeveralCoroutineCoroutinesNumber.error =
                    "Please enter a number"
                return@setOnClickListener
            }

            viewModel.performCalculation(
                binding.edtCalculationSeveralCoroutineFactorial.text.toString().toInt(),
                binding.edtCalculationSeveralCoroutineCoroutinesNumber.text.toString().toInt()
            )
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
        binding.pbCalculationSeveralCoroutine.visibility = View.VISIBLE
        binding.tvCalculationSeveralCoroutineFactorialTime.text = ""
        binding.tvCalculationSeveralCoroutineConversionTime.text = ""
        binding.tvCalculationSeveralCoroutineFactorialResult.text = ""
        disableButtons()
    }

    private fun onSuccess(uiState: UiState.Success) {
        enableButtons()
        binding.pbCalculationSeveralCoroutine.visibility = View.GONE

        binding.tvCalculationSeveralCoroutineFactorialTime.text =
            getString(R.string.duration_calculation, uiState.computationDuration)

        binding.tvCalculationSeveralCoroutineConversionTime.text =
            getString(R.string.duration_string_conversion, uiState.stringConversionDuration)

        binding.tvCalculationSeveralCoroutineFactorialResult.text =
            if (uiState.result.length <= 150) {
                uiState.result
            } else {
                "${uiState.result.substring(0, 147)}..."
            }
    }

    private fun onError(uiState: UiState.Error) {
        binding.pbCalculationSeveralCoroutine.visibility = View.GONE
        Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
        enableButtons()
    }

    private fun enableButtons() {
        binding.btnCalculationSeveralCoroutine.isEnabled = true
    }

    private fun disableButtons() {
        binding.btnCalculationSeveralCoroutine.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}