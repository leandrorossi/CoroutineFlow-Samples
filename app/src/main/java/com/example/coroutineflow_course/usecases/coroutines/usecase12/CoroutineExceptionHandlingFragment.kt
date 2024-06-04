package com.example.coroutineflow_course.usecases.coroutines.usecase12

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
import com.example.coroutineflow_course.databinding.FragmentCoroutineExceptionHandlingBinding
import com.example.coroutineflow_course.funtions.fromHtml
import kotlinx.coroutines.launch

class CoroutineExceptionHandlingFragment : Fragment() {

    private var _binding: FragmentCoroutineExceptionHandlingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CoroutineExceptionHandlingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCoroutineExceptionHandlingBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCoroutineExceptionHandlingWithTryCatch.setOnClickListener {
            viewModel.handleExceptionWithTryCatch()
        }

        binding.btnCoroutineExceptionHandlingWithExceptionHandler.setOnClickListener {
            viewModel.handleWithCoroutineExceptionHandler()
        }

        binding.btnCoroutineExceptionHandlingResultEvenWithFail.setOnClickListener {
            viewModel.showResultsEvenIfChildCoroutineFails()
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
        binding.pbCoroutineExceptionHandling.visibility = View.VISIBLE
        binding.tvCoroutineExceptionHandlingResult.text = ""
        disableButtons()
    }

    private fun onSuccess(uiState: UiState.Success) {
        enableButtons()
        binding.pbCoroutineExceptionHandling.visibility = View.GONE

        val versionFeaturesString = uiState.versionFeatures.joinToString(separator = "<br><br>") {
            "<b>New Features of ${it.androidVersion.name} </b> <br> ${
                it.features.joinToString(
                    prefix = "- ",
                    separator = "<br>- "
                )
            }"
        }

        binding.tvCoroutineExceptionHandlingResult.text = fromHtml(versionFeaturesString)
    }

    private fun onError(uiState: UiState.Error) {
        binding.pbCoroutineExceptionHandling.visibility = View.GONE
        Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()

        enableButtons()
    }

    private fun enableButtons() {
        binding.btnCoroutineExceptionHandlingWithTryCatch.isEnabled = true
        binding.btnCoroutineExceptionHandlingWithExceptionHandler.isEnabled = true
        binding.btnCoroutineExceptionHandlingResultEvenWithFail.isEnabled = true
    }

    private fun disableButtons() {
        binding.btnCoroutineExceptionHandlingWithTryCatch.isEnabled = false
        binding.btnCoroutineExceptionHandlingWithExceptionHandler.isEnabled = false
        binding.btnCoroutineExceptionHandlingResultEvenWithFail.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}