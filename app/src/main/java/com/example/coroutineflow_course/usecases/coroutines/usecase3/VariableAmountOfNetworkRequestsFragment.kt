package com.example.coroutineflow_course.usecases.coroutines.usecase3

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
import com.example.coroutineflow_course.databinding.FragmentVariableAmountOfNetworkRequestsBinding
import com.example.coroutineflow_course.funtions.fromHtml

import kotlinx.coroutines.launch

class VariableAmountOfNetworkRequestsFragment : Fragment() {

    private var _binding: FragmentVariableAmountOfNetworkRequestsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VariableAmountOfNetworkRequestsViewModel by viewModels()

    private var operationStartTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentVariableAmountOfNetworkRequestsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConcurrentVariableAmountNetworkRequestSequentially.setOnClickListener {
            viewModel.performNetworkRequestsSequentially()
        }

        binding.btnConcurrentVariableAmountNetworkRequestConcurrently.setOnClickListener {
            viewModel.performNetworkRequestsConcurrently()
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
        operationStartTime = System.currentTimeMillis()
        binding.pbConcurrentVariableAmountNetworkRequest.visibility = View.VISIBLE
        binding.tvConcurrentVariableAmountNetworkRequest.text = ""
        binding.tvConcurrentVariableAmountNetworkRequestTime.text = ""
        disableButtons()
    }

    private fun onSuccess(uiState: UiState.Success) {
        enableButtons()
        binding.pbConcurrentVariableAmountNetworkRequest.visibility = View.GONE

        val duration = System.currentTimeMillis() - operationStartTime
        binding.tvConcurrentVariableAmountNetworkRequestTime.text = "Time taken: ${duration}ms"

        val versionFeatures = uiState.versionFeatures
        val versionFeaturesString = versionFeatures.joinToString(separator = "<br><br>") {
            "<b>New Features of ${it.androidVersion.name} </b> <br> ${
                it.features.joinToString(
                    prefix = "- ",
                    separator = "<br>- "
                )
            }"
        }

        binding.tvConcurrentVariableAmountNetworkRequest.text = fromHtml(versionFeaturesString)
    }

    private fun onError(uiState: UiState.Error) {
        binding.pbConcurrentVariableAmountNetworkRequest.visibility = View.GONE
        binding.tvConcurrentVariableAmountNetworkRequestTime.visibility = View.GONE
        Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
        enableButtons()
    }

    private fun enableButtons() {
        binding.btnConcurrentVariableAmountNetworkRequestSequentially.isEnabled = true
        binding.btnConcurrentVariableAmountNetworkRequestConcurrently.isEnabled = true
    }

    private fun disableButtons() {
        binding.btnConcurrentVariableAmountNetworkRequestSequentially.isEnabled = false
        binding.btnConcurrentVariableAmountNetworkRequestConcurrently.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}