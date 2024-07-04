package com.example.coroutineflow_course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.coroutineflow_course.databinding.FragmentFlowUsecasesBinding

class FlowUsecasesFragment : Fragment() {

    private var _binding: FragmentFlowUsecasesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFlowUsecasesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvFlowAsLivedataUsecase.setOnClickListener {
            findNavController().navigate(R.id.action_FlowUsecasesFragment_to_FlowAsLivedataUsecaseFragment)
        }

        binding.tvFlowAsSharedFlowUsecase.setOnClickListener {
            findNavController().navigate(R.id.action_FlowUsecasesFragment_to_FlowAsSharedFlowUsecaseFragment)
        }

        binding.tvFlowAsStateFlowUsecase.setOnClickListener {
            findNavController().navigate(R.id.action_FlowUsecasesFragment_to_FlowAsStateFlowUsecaseFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}