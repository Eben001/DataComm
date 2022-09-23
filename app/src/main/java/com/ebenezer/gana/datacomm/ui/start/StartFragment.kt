package com.ebenezer.gana.datacomm.ui.start

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
import androidx.navigation.fragment.findNavController
import com.ebenezer.gana.datacomm.R
import com.ebenezer.gana.datacomm.databinding.FragmentStartBinding
import com.ebenezer.gana.datacomm.prefsStore.UserPreference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StartFragmentViewModel by viewModels()

    @Inject
    lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModels()
        setGreetingMessage(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
        setOnClickListeners()
    }

    private fun observeViewModels() {
        //Observe and save the user's balance in preferenceStore
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.balance.collectLatest { balance ->
                        balance?.let {
                            userPreference.saveDataBalance(it)
                        }
                        binding.swipeRefresh.isRefreshing = false
                    }
                }

                launch {
                    //observe and display greeting text
                    viewModel.greetingText.collectLatest {
                        binding.tvGreetings.text = it?.asString(requireContext())
                    }
                }
                launch {
                    //Observe and display balance from dataStore preference
                    userPreference.dataBalance.collectLatest { balance ->
                        binding.balance.text = resources.getString(R.string.balance, balance ?: 0.0)
                    }
                }
                launch {
                    viewModel.error
                        .collect { message ->
                            message?.let {
                                Toast.makeText(
                                    requireContext(),
                                    it.asString(requireContext()),
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.swipeRefresh.isRefreshing = false
                            }

                        }
                }


            }
        }
    }


    private fun setGreetingMessage(hourOfDay: Int) {
        viewModel.setGreetingMessage(hourOfDay)
    }

    private fun setOnClickListeners() {
        binding.buyAirtime.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_airtimeFragment)
        }

        binding.buyData.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_dataTopupFragment)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getBalance()
            binding.swipeRefresh.isRefreshing = true
        }
    }

    override fun onStart() {
        super.onStart()
            lifecycleScope.launch {
                viewModel.getBalance()
                binding.swipeRefresh.isRefreshing = true
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}