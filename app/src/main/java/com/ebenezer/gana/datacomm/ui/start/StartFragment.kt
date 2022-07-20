package com.ebenezer.gana.datacomm.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ebenezer.gana.datacomm.R
import com.ebenezer.gana.datacomm.databinding.FragmentStartBinding
import com.ebenezer.gana.datacomm.prefsStore.UserPreference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val TAG = "StartFragment"
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

    private fun setGreetingMessage(hourOfDay: Int) {
        viewModel.setGreetingMessage(hourOfDay)

    }

    private fun observeError() {
        viewModel.error.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message.asString(requireContext()), Toast.LENGTH_SHORT)
                .show()
            binding.swipeRefresh.isRefreshing = false
        }

    }

    private fun setOnClickListeners() {
        binding.buyAirtime.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_airtimeFragment)
        }

        binding.buyData.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_dataTopupFragment)
        }

        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getBalance()
                observeError()
                binding.swipeRefresh.isRefreshing = true


            }

        }
    }

    private fun observeViewModels() {
        //Observe and save the user's balance in preferenceStore
        viewModel.balance.observe(viewLifecycleOwner) { balance ->
            lifecycleScope.launch {
                balance?.let {
                    userPreference.saveDataBalance(it)
                }
                binding.swipeRefresh.isRefreshing = false

            }


        }

        //Observe and display balance from dataStore preference
        userPreference.dataBalance.asLiveData().observe(viewLifecycleOwner) { balance ->
            binding.balance.text = resources.getString(R.string.balance, balance ?: 0.0)

        }

        //observe and display greeting text
        viewModel.greetingText.observe(viewLifecycleOwner) {
            binding.tvGreetings.text = it.asString(requireContext())
        }

    }

    override fun onStart() {
        super.onStart()
        if (viewModel.isBalanceLoaded.value == false) {
            lifecycleScope.launch {
                viewModel.getBalance()
                observeError()
                binding.swipeRefresh.isRefreshing = true
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}