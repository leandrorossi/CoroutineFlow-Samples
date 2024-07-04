package com.example.coroutineflow_course.usecases.flows.usecase3

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
import com.example.coroutineflow_course.databinding.FragmentFlowAsStateFlowUsecaseBinding
import com.example.coroutineflow_course.usecases.flows.utils.StockAdapter
import com.example.coroutineflow_course.usecases.flows.utils.UiState
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class FlowAsStateFlowUsecaseFragment : Fragment() {

    private var _binding: FragmentFlowAsStateFlowUsecaseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FlowAsStateFlowUsecaseViewModel by viewModels {
        ViewModelFactory(NetworkStockPriceDataSource(mockApi(requireContext())))
    }
    private val adapter = StockAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFlowAsStateFlowUsecaseBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcvFlowAsStateFlowUsecase.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.currentStockAsStateFlow.collect { state ->
                    render(state)
                }
            }
        }
    }

    private fun render(uiState: UiState) {
        when (uiState) {
            is UiState.Loading -> {
                binding.pgFlowAsStateFlowUsecase.visibility = View.VISIBLE
                binding.rcvFlowAsStateFlowUsecase.visibility = View.GONE
            }

            is UiState.Success -> {
                binding.rcvFlowAsStateFlowUsecase.visibility = View.VISIBLE
                binding.tvLastUpdateTime3.text =
                    "lastUpdateTime: ${LocalDateTime.now().toString(DateTimeFormat.fullTime())}"

                adapter.stockList = uiState.stockList
                binding.pgFlowAsStateFlowUsecase.visibility = View.GONE
            }

            is UiState.Error -> {
                Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_SHORT).show()
                binding.pgFlowAsStateFlowUsecase.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}