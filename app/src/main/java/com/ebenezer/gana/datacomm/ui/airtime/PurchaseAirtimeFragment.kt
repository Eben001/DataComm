package com.ebenezer.gana.datacomm.ui.airtime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ebenezer.gana.datacomm.R
import com.ebenezer.gana.datacomm.databinding.FragmentPurchaseAirtimeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseAirtimeFragment : Fragment() {

    private var _binding: FragmentPurchaseAirtimeBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: PurchaseAirtimeFragmentArgs by navArgs()

    private val viewModel: PurchaseAirtimeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPurchaseAirtimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeRefresh.isEnabled = false
        observeViewModel()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.recharge.setOnClickListener {
            if (isValidDetails()) {
                buyNewAirtime()
                disableViews()
                binding.swipeRefresh.isRefreshing = true
            }
        }


        when (navigationArgs.airtimeType) {
            resources.getString(R.string.airtel) -> {
                binding.etNetwork.setText(resources.getString(R.string.text_airtel_airtime))
                binding.etNetwork.isEnabled = false
            }
            resources.getString(R.string.glo) -> {
                binding.etNetwork.setText(resources.getString(R.string.text_glo_airtime))
                binding.etNetwork.isEnabled = false
            }

            resources.getString(R.string.mtn) -> {
                binding.etNetwork.setText(resources.getString(R.string.text_mtn_airtime))
                binding.etNetwork.isEnabled = false
            }
            else -> {
                binding.etNetwork.setText(resources.getString(R.string.text_9mobile_airtime))
                binding.etNetwork.isEnabled = false
            }

        }


    }


    private fun clearTextEntries() {
        binding.etPhoneNumber.text?.clear()
        binding.etAmount.text?.clear()
    }

    private fun disableViews() {
        binding.etPhoneNumber.isEnabled = false
        binding.etAmount.isEnabled = false
        binding.recharge.isEnabled = false
        binding.tvResult.visibility = View.INVISIBLE

    }

    private fun enableViews() {
        binding.etPhoneNumber.isEnabled = true
        binding.etAmount.isEnabled = true
        binding.recharge.isEnabled = true
        binding.tvResult.visibility = View.VISIBLE
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner) {
            clearTextEntries()
            binding.tvResult.text = it.asString(requireContext())
            enableViews()
            binding.swipeRefresh.isRefreshing = false

        }
    }

    private fun buyNewAirtime() {
        viewModel.buyAirtime(
            network = navigationArgs.airtimeType,
            phone = binding.etPhoneNumber.text.toString(),
            amount = binding.etAmount.text.toString()
        )


    }

    /**
     * Validates User's inputs. Returns true if the textField is not empty
     */
    private fun isValidDetails(): Boolean {
        return when {
            binding.etAmount.text.toString().trim().isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.err_msg_enter_amount),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            binding.etPhoneNumber.text.toString().trim().isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.err_msg_enter_phone),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> true
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}