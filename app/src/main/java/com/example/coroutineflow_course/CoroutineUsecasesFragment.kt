package com.example.coroutineflow_course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.coroutineflow_course.databinding.FragmentCoroutineUsecasesBinding

class CoroutineUsecasesFragment : Fragment() {

    private var _binding: FragmentCoroutineUsecasesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCoroutineUsecasesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSequentialCallback.setOnClickListener {
            findNavController().navigate(R.id.action_CoroutineUsecasesFragment_to_SequentialNetworkRequestCallbackFragment)
        }

        binding.tvSequentialRxjava.setOnClickListener {
            findNavController().navigate(R.id.action_CoroutineUsecasesFragment_to_SequentialNetworkRequestRxJavaFragment)
        }

        binding.tvSequentialCoroutine.setOnClickListener {
            findNavController().navigate(R.id.action_CoroutineUsecasesFragment_to_SequentialNetworkRequestCoroutineFragment)
        }

        binding.tvConcurrentCoroutine.setOnClickListener {
            findNavController().navigate(R.id.action_CoroutineUsecasesFragment_to_ConcurrentNetworkRequestFragment)
        }

        binding.tvConcurrentVariableAmountCoroutine.setOnClickListener {
            findNavController().navigate(R.id.action_CoroutineUsecasesFragment_to_ConcurrentVariableAmountOfNetworkRequestFragment)
        }

        binding.tvNetworkRequestWithTimeout.setOnClickListener {
            findNavController().navigate(R.id.action_CoroutineUsecasesFragment_to_NetworkRequestWithTimeoutFragment)
        }

        binding.tvNetworkRequestWithRetry.setOnClickListener {
            findNavController().navigate(R.id.action_CoroutineUsecasesFragment_to_NetworkRequestWithRetryFragment)
        }

        binding.tvNetworkRequestWithTimeoutAndRetry.setOnClickListener {
            findNavController().navigate(R.id.action_CoroutineUsecasesFragment_to_NetworkRequestWithTimeoutAndRetryFragment)
        }

        binding.tvRoomAndCoroutines.setOnClickListener {
            findNavController().navigate(R.id.action_CoroutineUsecasesFragment_to_RoomAndCoroutineFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}