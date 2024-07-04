package com.example.coroutineflow_course.usecases.flows.usecase1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.coroutineflow_course.databinding.FragmentFlowAsLivedataUsecaseBinding
import com.example.coroutineflow_course.usecases.flows.utils.StockAdapter
import com.example.coroutineflow_course.usecases.flows.utils.UiState
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class FlowAsLivedataUsecaseFragment : Fragment() {

    private var _binding: FragmentFlowAsLivedataUsecaseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FlowAsLivedataUsecaseViewModel by viewModels {
        ViewModelFactory(NetworkStockPriceDataSource(mockApi(requireContext())))
    }
    private val adapter = StockAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFlowAsLivedataUsecaseBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcvFlowAsLivedataUsecase.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.currentStockAsLivedata.observe(viewLifecycleOwner) { state ->
                    render(state)
                }
            }
        }
    }

    private fun render(uiState: UiState) {
        when (uiState) {
            is UiState.Loading -> {
                binding.pgFlowAsLivedataUsecase.visibility = View.VISIBLE
                binding.rcvFlowAsLivedataUsecase.visibility = View.GONE
            }

            is UiState.Success -> {
                binding.rcvFlowAsLivedataUsecase.visibility = View.VISIBLE
                binding.tvLastUpdateTime1.text =
                    "lastUpdateTime: ${LocalDateTime.now().toString(DateTimeFormat.fullTime())}"

                adapter.stockList = uiState.stockList
                binding.pgFlowAsLivedataUsecase.visibility = View.GONE
            }

            is UiState.Error -> {
                Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_SHORT).show()
                binding.pgFlowAsLivedataUsecase.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}