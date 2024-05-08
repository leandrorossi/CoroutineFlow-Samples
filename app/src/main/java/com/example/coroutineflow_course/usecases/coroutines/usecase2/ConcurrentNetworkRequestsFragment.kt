package com.example.coroutineflow_course.usecases.coroutines.usecase2

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
import com.example.coroutineflow_course.databinding.FragmentConcurrentNetworkRequestsBinding
import com.example.coroutineflow_course.funtions.fromHtml
import kotlinx.coroutines.launch

class ConcurrentNetworkRequestsFragment : Fragment() {

    private var _binding: FragmentConcurrentNetworkRequestsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ConcurrentNetworkRequestsViewModel>()

    private var operationStartTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConcurrentNetworkRequestsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConcurrentNetworkRequestSequentially.setOnClickListener {
            viewModel.performNetworkRequestsSequentially()
        }

        binding.btnConcurrentNetworkRequestConcurrently.setOnClickListener {
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
        binding.pbConcurrentNetworkRequest.visibility = View.VISIBLE
        binding.tvConcurrentNetworkRequest.text = ""
        binding.tvConcurrentNetworkRequestTime.text = ""
        disableButtons()
    }

    private fun onSuccess(uiState: UiState.Success) {
        enableButtons()
        binding.pbConcurrentNetworkRequest.visibility = View.GONE

        val duration = System.currentTimeMillis() - operationStartTime
        binding.tvConcurrentNetworkRequestTime.text = "Time taken: ${duration}ms"

        val versionFeatures = uiState.versionFeatures
        val versionFeaturesString = versionFeatures.joinToString(separator = "<br><br>") {
            "<b>New Features of ${it.androidVersion.name} </b> <br> ${
                it.features.joinToString(
                    prefix = "- ",
                    separator = "<br>- "
                )
            }"
        }

        binding.tvConcurrentNetworkRequest.text = fromHtml(versionFeaturesString)
    }

    private fun onError(uiState: UiState.Error) {
        binding.pbConcurrentNetworkRequest.visibility = View.GONE
        binding.tvConcurrentNetworkRequestTime.visibility = View.GONE
        Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
        enableButtons()
    }

    private fun enableButtons() {
        binding.btnConcurrentNetworkRequestSequentially.isEnabled = true
        binding.btnConcurrentNetworkRequestConcurrently.isEnabled = true
    }

    private fun disableButtons() {
        binding.btnConcurrentNetworkRequestSequentially.isEnabled = false
        binding.btnConcurrentNetworkRequestConcurrently.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}