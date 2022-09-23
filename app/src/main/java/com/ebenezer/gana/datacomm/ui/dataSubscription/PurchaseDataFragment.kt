package com.ebenezer.gana.datacomm.ui.dataSubscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.ebenezer.gana.datacomm.R
import com.ebenezer.gana.datacomm.databinding.FragmentPurchaseDataBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PurchaseDataFragment : Fragment() {

    private var _binding: FragmentPurchaseDataBinding? = null
    private val binding get() = _binding!!
    private val navigationArgs: PurchaseDataFragmentArgs by navArgs()
    private val viewModel: PurchaseDataViewModel by viewModels()

    private var amountId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPurchaseDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefresh.isEnabled = false
        observeViewModel()
        bind()
        setOnClickListeners()
    }

    private fun bind() {
        when (navigationArgs.dataType) {
            resources.getString(R.string.mtn_sme) -> {
                binding.tvDataPlan.text = resources.getString(R.string.text_select_sme_plan)
                val mtnSme = resources.getStringArray(R.array.MTN_SME)
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item, mtnSme
                )
                binding.dataPlanSpinner.adapter = adapter
                binding.dataPlanSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {

                            when (binding.dataPlanSpinner.selectedItemPosition) {
                                0 -> amountId = "M500MBS"
                                1 -> amountId = "M1GBS"
                                2 -> amountId = "M2GBS"
                                3 -> amountId = "M5GBS"
                                4 -> amountId = "M10GBS"
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }

            }

            resources.getString(R.string.mtn_gifting) -> {
                binding.tvDataPlan.text = resources.getString(R.string.text_select_mtn_cg_plan)
                val mtnGiftingList = resources.getStringArray(R.array.MTN_GIFTING)
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item, mtnGiftingList
                )
                binding.dataPlanSpinner.adapter = adapter
                binding.dataPlanSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            when (binding.dataPlanSpinner.selectedItemPosition) {
                                0 -> amountId = "M50MBG"
                                1 -> amountId = "M500MBG"
                                2 -> amountId = "M1GBG"
                                3 -> amountId = "M2GBG"
                                4 -> amountId = "M5GBG"
                                5 -> amountId = "M10GBG"
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }
            }
            resources.getString(R.string.airtel_data) -> {
                binding.tvDataPlan.text = resources.getString(R.string.text_select_airtel_plan)
                val airtel = resources.getStringArray(R.array.AIRTEL)
                val adapter = ArrayAdapter(
                    requireActivity(),
                    android.R.layout.simple_spinner_item, airtel
                )
                binding.dataPlanSpinner.adapter = adapter

                binding.dataPlanSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            when (binding.dataPlanSpinner.selectedItemPosition) {
                                0 -> amountId = "A100MB"
                                1 -> amountId = "A300MB"
                                2 -> amountId = "A500MB"
                                3 -> amountId = "A1GB"
                                4 -> amountId = "A2GB"
                                5 -> amountId = "A5GB"
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }
            }
            resources.getString(R.string.glo_data) -> {
                binding.tvDataPlan.text = resources.getString(R.string.text_select_glo_plan)
                val glo = resources.getStringArray(R.array.GLO)
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item, glo
                )
                binding.dataPlanSpinner.adapter = adapter

                binding.dataPlanSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            when (binding.dataPlanSpinner.selectedItemPosition) {
                                0 -> amountId = "G1GB"
                                1 -> amountId = "G2.9GB"
                                2 -> amountId = "G4.1GB"
                                3 -> amountId = "G5.8GB"
                                4 -> amountId = "G7.7GB"
                                5 -> amountId = "G10GB"
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }

            }
        }

    }

    private fun buyNewData() {
        viewModel.buyData(
            navigationArgs.dataType,
            binding.phoneNumber.text.toString(),
            amountId
        )
    }

    /*
    Sets Onclick Listeners to views
     */
    private fun setOnClickListeners() {
        binding.topUp.setOnClickListener {
            if (isValidDetails()) {
                buyNewData()
                disableViews()
                binding.swipeRefresh.isRefreshing = true
            }

        }

    }

    private fun isValidDetails(): Boolean {
        return when {
            binding.phoneNumber.text.toString().trim().isEmpty() -> {
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

    private fun enableViews() {
        binding.phoneNumber.isEnabled = true
        binding.topUp.isEnabled = true
        binding.tvResult.visibility = View.VISIBLE

    }

    private fun disableViews() {
        binding.tvResult.visibility = View.INVISIBLE
        binding.topUp.isEnabled = false
        binding.phoneNumber.isEnabled = false
    }


    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.resultSharedFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest { result ->
                    result?.let {
                        clearTextEntries()
                        binding.tvResult.text = it.asString(requireContext())
                        enableViews()
                        binding.swipeRefresh.isRefreshing = false
                    }
                }
        }

    }

    private fun clearTextEntries() {
        binding.phoneNumber.text?.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}