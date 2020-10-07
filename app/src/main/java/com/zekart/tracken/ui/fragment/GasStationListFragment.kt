package com.zekart.tracken.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zekart.tracken.R
import com.zekart.tracken.databinding.FragmentGasStationStatisticsBinding
import com.zekart.tracken.ui.activity.EditGasStationActivity
import kotlinx.android.synthetic.main.fragment_gas_station_list.view.*

class GasStationListFragment: Fragment() {
    private var binding: FragmentGasStationStatisticsBinding? = null

    companion object {
        @JvmStatic
        fun newInstance() = GasStationListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gas_station_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentGasStationStatisticsBinding.bind(view)

        binding?.root?.floatingActionButton?.setOnClickListener(onFloatButtonClick)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    var onFloatButtonClick : View.OnClickListener = View.OnClickListener {
        val intent = Intent(activity, EditGasStationActivity::class.java)
        startActivity(intent)
    }
}