package com.example.coroutineflow_course.usecases.coroutines.usecase1.rxjava

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
import com.example.coroutineflow_course.databinding.FragmentSequentialNetworkRequestsRxJavaBinding
import com.example.coroutineflow_course.funtions.fromHtml
import kotlinx.coroutines.launch

class SequentialNetworkRequestsRxJavaFragment : Fragment() {

    private var _binding: FragmentSequentialNetworkRequestsRxJavaBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SequentialNetworkRequestsRxJavaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSequentialNetworkRequestsRxJavaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSequentialNetworkRequestRxjava.setOnClickListener {
            viewModel.performToSequentialNetworkRequest()
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
        binding.pbSequentialNetworkRequestRxjava.visibility = View.VISIBLE
        binding.tvSequentialNetworkRequestRxjava.text = ""
    }

    private fun onSuccess(uiState: UiState.Success) {
        binding.pbSequentialNetworkRequestRxjava.visibility = View.GONE
        binding.tvSequentialNetworkRequestRxjava.text = fromHtml(
            "<b>Features of most recent Android Version \" ${uiState.versionFeatures.androidVersion.name} \"</b><br>" +
                    uiState.versionFeatures.features.joinToString(
                        prefix = "- ",
                        separator = "<br>- "
                    )
        )
    }

    private fun onError(uiState: UiState.Error) {
        binding.pbSequentialNetworkRequestRxjava.visibility = View.GONE
        Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}