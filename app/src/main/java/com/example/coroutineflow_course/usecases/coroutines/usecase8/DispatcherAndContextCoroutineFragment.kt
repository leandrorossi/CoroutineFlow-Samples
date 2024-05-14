package com.example.coroutineflow_course.usecases.coroutines.usecase8

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
import com.example.coroutineflow_course.databinding.FragmentDispatcherAndContextCoroutineBinding
import kotlinx.coroutines.launch

class DispatcherAndContextCoroutineFragment : Fragment() {

    private var _binding: FragmentDispatcherAndContextCoroutineBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DispatcherAndContextCoroutineViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentDispatcherAndContextCoroutineBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDispatcherAndContextCoroutine.setOnClickListener {

            if (binding.edtDispatcherAndContextCoroutine.text.toString().isEmpty()) {
                binding.edtDispatcherAndContextCoroutine.error = "Please enter a number"
                return@setOnClickListener
            }

            viewModel.performCalculation(
                binding.edtDispatcherAndContextCoroutine.text.toString().toInt()
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
        binding.pbDispatcherAndContextCoroutine.visibility = View.VISIBLE
        binding.tvDispatcherAndContextCoroutineFactorialTime.text = ""
        binding.tvDispatcherAndContextCoroutineConversionTime.text = ""
        binding.tvDispatcherAndContextCoroutineFactorialResult.text = ""
        disableButtons()
    }

    private fun onSuccess(uiState: UiState.Success) {
        enableButtons()
        binding.pbDispatcherAndContextCoroutine.visibility = View.GONE

        binding.tvDispatcherAndContextCoroutineFactorialTime.text =
            getString(R.string.duration_calculation, uiState.computationDuration)

        binding.tvDispatcherAndContextCoroutineConversionTime.text =
            getString(R.string.duration_string_conversion, uiState.stringConversionDuration)

        binding.tvDispatcherAndContextCoroutineFactorialResult.text =
            if (uiState.result.length <= 150) {
                uiState.result
            } else {
                "${uiState.result.substring(0, 147)}..."
            }
    }

    private fun onError(uiState: UiState.Error) {
        binding.pbDispatcherAndContextCoroutine.visibility = View.GONE
        Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
        enableButtons()
    }

    private fun enableButtons() {
        binding.btnDispatcherAndContextCoroutine.isEnabled = true
    }

    private fun disableButtons() {
        binding.btnDispatcherAndContextCoroutine.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}