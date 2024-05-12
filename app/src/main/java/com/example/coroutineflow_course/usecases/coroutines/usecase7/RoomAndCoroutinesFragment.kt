package com.example.coroutineflow_course.usecases.coroutines.usecase7

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
import com.example.coroutineflow_course.R
import com.example.coroutineflow_course.databinding.FragmentRoomAndCoroutinesBinding
import com.example.coroutineflow_course.funtions.fromHtml
import kotlinx.coroutines.launch

class RoomAndCoroutinesFragment : Fragment() {

    private var _binding: FragmentRoomAndCoroutinesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RoomAndCoroutinesViewModel by viewModels {
        ViewModelFactory(
            mockApi(),
            AndroidVersionDatabase.getInstance(requireActivity().applicationContext)
                .androidVersionDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRoomAndCoroutinesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRoomAndCoroutinesLoadData.setOnClickListener {
            viewModel.loadData()
        }

        binding.btnRoomAndCoroutinesClearDatabase.setOnClickListener {
            viewModel.clearDatabase()

            binding.tvRoomAndCoroutines.text = ""
            binding.imgRoomAndCoroutinesFromDatabase.visibility = View.GONE
            binding.tvRoomAndCoroutinesFromDatabase.visibility = View.GONE
            binding.imgRoomAndCoroutinesFromNetwork.visibility = View.GONE
            binding.pbRoomAndCoroutinesFromNetwork.visibility = View.GONE
            binding.tvRoomAndCoroutinesFromNetwork.visibility = View.GONE
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
                binding.pbRoomAndCoroutinesFromDatabase.visibility = View.VISIBLE
                binding.tvRoomAndCoroutinesFromDatabase.visibility = View.VISIBLE
                binding.imgRoomAndCoroutinesFromDatabase.visibility = View.GONE
            }

            UiState.Loading.LoadFromNetwork -> {
                binding.imgRoomAndCoroutinesFromNetwork.visibility = View.GONE
                binding.pbRoomAndCoroutinesFromNetwork.visibility = View.VISIBLE
                binding.tvRoomAndCoroutinesFromNetwork.visibility = View.VISIBLE
            }
        }

        disableButtons()
    }

    private fun onSuccess(uiState: UiState.Success) {
        enableButtons()

        when (uiState.dataSource) {
            DataSource.DATABASE -> {
                binding.pbRoomAndCoroutinesFromDatabase.visibility = View.GONE
                binding.imgRoomAndCoroutinesFromDatabase.visibility = View.VISIBLE
                binding.imgRoomAndCoroutinesFromDatabase.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_round_done
                    )
                )
            }

            DataSource.NETWORK -> {
                binding.pbRoomAndCoroutinesFromNetwork.visibility = View.GONE
                binding.imgRoomAndCoroutinesFromNetwork.visibility = View.VISIBLE
                binding.imgRoomAndCoroutinesFromNetwork.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_round_done
                    )
                )
            }
        }

        val readableVersions = uiState.androidVersions.map { "API ${it.apiLevel}: ${it.name}" }
        binding.tvRoomAndCoroutines.text = fromHtml(
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
                binding.pbRoomAndCoroutinesFromDatabase.visibility = View.GONE
                binding.imgRoomAndCoroutinesFromDatabase.visibility = View.VISIBLE
                binding.imgRoomAndCoroutinesFromDatabase.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_round_error
                    )
                )
            }

            DataSource.NETWORK -> {
                binding.pbRoomAndCoroutinesFromNetwork.visibility = View.GONE
                binding.imgRoomAndCoroutinesFromNetwork.visibility = View.VISIBLE
                binding.imgRoomAndCoroutinesFromNetwork.setImageDrawable(
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
        binding.btnRoomAndCoroutinesLoadData.isEnabled = true
        binding.btnRoomAndCoroutinesClearDatabase.isEnabled = true
    }

    private fun disableButtons() {
        binding.btnRoomAndCoroutinesLoadData.isEnabled = false
        binding.btnRoomAndCoroutinesClearDatabase.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}