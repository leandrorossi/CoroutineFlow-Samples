package com.example.coroutineflow_course.usecases.coroutines.usecase10

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.coroutineflow_course.App
import com.example.coroutineflow_course.R
import com.example.coroutineflow_course.databinding.FragmentContinueCoroutineWhenUserLeavesScreenBinding
import com.example.coroutineflow_course.funtions.fromHtml
import kotlinx.coroutines.launch

class ContinueCoroutineWhenUserLeavesScreenFragment : Fragment() {

    private var _binding: FragmentContinueCoroutineWhenUserLeavesScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContinueCoroutineWhenUserLeavesScreenViewModel by viewModels {
        ViewModelFactory((requireActivity().application as App).androidVersionRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentContinueCoroutineWhenUserLeavesScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnContinueCoroutineLoadData.setOnClickListener {
            viewModel.loadData()
        }

        binding.btnContinueCoroutineClearDatabase.setOnClickListener {
            viewModel.clearDatabase()

            binding.tvContinueCoroutine.text = ""
            binding.imgContinueCoroutineFromDatabase.visibility = View.GONE
            binding.tvContinueCoroutineFromDatabase.visibility = View.GONE
            binding.imgContinueCoroutineFromNetwork.visibility = View.GONE
            binding.pbContinueCoroutineFromNetwork.visibility = View.GONE
            binding.tvContinueCoroutineFromNetwork.visibility = View.GONE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is UiState.Loading -> onLoad(state)
                        is UiState.Success -> onSuccess(state)
                        is UiState.Error -> onError(state)
                    }
                }
            }
        }

    }

    private fun onLoad(loadingState: UiState.Loading) {
        when (loadingState) {
            UiState.Loading.LoadFromDb -> {
                binding.pbContinueCoroutineFromDatabase.visibility = View.VISIBLE
                binding.tvContinueCoroutineFromDatabase.visibility = View.VISIBLE
                binding.imgContinueCoroutineFromDatabase.visibility = View.GONE
            }

            UiState.Loading.LoadFromNetwork -> {
                binding.imgContinueCoroutineFromNetwork.visibility = View.GONE
                binding.pbContinueCoroutineFromNetwork.visibility = View.VISIBLE
                binding.tvContinueCoroutineFromNetwork.visibility = View.VISIBLE
            }
        }

        disableButtons()
    }

    private fun onSuccess(uiState: UiState.Success) {
        enableButtons()

        when (uiState.dataSource) {
            DataSource.DATABASE -> {
                binding.pbContinueCoroutineFromDatabase.visibility = View.GONE
                binding.imgContinueCoroutineFromDatabase.visibility = View.VISIBLE
                binding.imgContinueCoroutineFromDatabase.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_round_done
                    )
                )
            }

            DataSource.NETWORK -> {
                binding.pbContinueCoroutineFromNetwork.visibility = View.GONE
                binding.imgContinueCoroutineFromNetwork.visibility = View.VISIBLE
                binding.imgContinueCoroutineFromNetwork.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_round_done
                    )
                )
            }
        }

        val readableVersions = uiState.androidVersions.map { "API ${it.apiLevel}: ${it.name}" }
        binding.tvContinueCoroutine.text = fromHtml(
            "<b>Recent Android Versions [from ${uiState.dataSource.name}]</b><br>${
                readableVersions.joinToString(
                    separator = "<br>"
                )
            }"
        )
    }

    private fun onError(uiState: UiState.Error) {
        enableButtons()

        when (uiState.dataSource) {
            DataSource.DATABASE -> {
                binding.pbContinueCoroutineFromDatabase.visibility = View.GONE
                binding.imgContinueCoroutineFromDatabase.visibility = View.VISIBLE
                binding.imgContinueCoroutineFromDatabase.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_round_error
                    )
                )
            }

            DataSource.NETWORK -> {
                binding.pbContinueCoroutineFromNetwork.visibility = View.GONE
                binding.imgContinueCoroutineFromNetwork.visibility = View.VISIBLE
                binding.imgContinueCoroutineFromNetwork.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_round_error
                    )
                )
            }
        }
        Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_SHORT).show()
    }

    private fun enableButtons() {
        binding.btnContinueCoroutineLoadData.isEnabled = true
        binding.btnContinueCoroutineClearDatabase.isEnabled = true
    }

    private fun disableButtons() {
        binding.btnContinueCoroutineLoadData.isEnabled = false
        binding.btnContinueCoroutineClearDatabase.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}