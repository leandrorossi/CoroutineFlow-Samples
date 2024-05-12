package com.example.coroutineflow_course.usecases.coroutines.usecase5

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
import com.example.coroutineflow_course.databinding.FragmentNetworkRequestsWithRetryBinding
import com.example.coroutineflow_course.funtions.fromHtml
import kotlinx.coroutines.launch

class NetworkRequestsWithRetryFragment : Fragment() {

    private var _binding: FragmentNetworkRequestsWithRetryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NetworkRequestsWithRetryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNetworkRequestsWithRetryBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRetryNetworkRequest.setOnClickListener {
            viewModel.performNetworkRequest()
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
        binding.pbRetryNetworkRequest.visibility = View.VISIBLE
        binding.tvRetryNetworkRequestTime.text = ""
        disableButtons()
    }

    private fun onSuccess(uiState: UiState.Success) {
        enableButtons()
        binding.pbRetryNetworkRequest.visibility = View.GONE

        val readableVersions = uiState.androidVersions.map { "API ${it.apiLevel}: ${it.name}" }
        binding.tvRetryNetworkRequestTime.text = fromHtml(
            "<b>Recent Android Versions</b><br>${readableVersions.joinToString(separator = "<br>")}"
        )
    }

    private fun onError(uiState: UiState.Error) {
        binding.pbRetryNetworkRequest.visibility = View.GONE
        Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
        enableButtons()
    }

    private fun enableButtons() {
        binding.btnRetryNetworkRequest.isEnabled = true
    }

    private fun disableButtons() {
        binding.btnRetryNetworkRequest.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}