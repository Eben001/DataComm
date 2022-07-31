package com.ebenezer.gana.datacomm.ui.dataSubscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ebenezer.gana.datacomm.R
import com.ebenezer.gana.datacomm.databinding.FragmentDataTopupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataTopupFragment : Fragment() {
    private var _binding:FragmentDataTopupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDataTopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.gloData.setOnClickListener {
            val action = DataTopupFragmentDirections.actionDataTopupFragmentToPurchaseDataFragment(
                resources.getString(R.string.glo_data)
            )
            findNavController().navigate(action)
        }
        binding.mtnSme.setOnClickListener {
            val action = DataTopupFragmentDirections.actionDataTopupFragmentToPurchaseDataFragment(
                resources.getString(R.string.mtn_sme)
            )
            findNavController().navigate(action)
        }
        binding.mtnCooperateGifting.setOnClickListener {
            val action = DataTopupFragmentDirections.actionDataTopupFragmentToPurchaseDataFragment(
                resources.getString(R.string.mtn_gifting)
            )

            findNavController().navigate(action)
        }
        binding.airtelData.setOnClickListener {
            val action = DataTopupFragmentDirections.actionDataTopupFragmentToPurchaseDataFragment(
                resources.getString(R.string.airtel_data)
            )
            findNavController().navigate(action)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}