package com.ebenezer.gana.datacomm.ui.airtime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ebenezer.gana.datacomm.R
import com.ebenezer.gana.datacomm.databinding.FragmentAirtimeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AirtimeFragment : Fragment() {

    private var _binding: FragmentAirtimeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAirtimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnclickListener()

    }

    private fun setOnclickListener() {
        binding.airtelAirtime.setOnClickListener {
            val action = AirtimeFragmentDirections.actionAirtimeFragmentToPurchaseAirtimeFragment(
                resources.getString(R.string.airtel)
            )
            this.findNavController().navigate(action)
        }
        binding.gloAirtime.setOnClickListener {
            val action = AirtimeFragmentDirections.actionAirtimeFragmentToPurchaseAirtimeFragment(
                resources.getString(R.string.glo)
            )
            this.findNavController().navigate(action)
        }
        binding.nineMobileAirtime.setOnClickListener {
            val action = AirtimeFragmentDirections.actionAirtimeFragmentToPurchaseAirtimeFragment(
                resources.getString(R.string._9mobile)
            )
            this.findNavController().navigate(action)
        }
        binding.mtnAirtime.setOnClickListener {
            val action = AirtimeFragmentDirections.actionAirtimeFragmentToPurchaseAirtimeFragment(
                resources.getString(R.string.mtn)
            )
            this.findNavController().navigate(action)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}