package com.zekart.tracken.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zekart.tracken.R
import com.zekart.tracken.adapter.GasStationListAdapter
import com.zekart.tracken.databinding.FragmentGasStationStatisticsBinding
import com.zekart.tracken.ui.activity.GasStationActivity
import com.zekart.tracken.viewmodel.FragmentStationListViewModel
import kotlinx.android.synthetic.main.fragment_gas_station_list.*
import kotlinx.android.synthetic.main.fragment_gas_station_list.view.*

class GasStationListFragment: Fragment(), LifecycleOwner {
    private var binding: FragmentGasStationStatisticsBinding? = null
    private var adapterRecyclerView: GasStationListAdapter? = null
    private lateinit var viewModel:FragmentStationListViewModel

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

        viewModel = ViewModelProvider(this).get(FragmentStationListViewModel::class.java)

        initRecyclerViewStationList()

        viewModel.mStation.observe(viewLifecycleOwner, Observer {
            adapterRecyclerView?.setStation(it)
        })
    }

    private var onFloatButtonClick : View.OnClickListener = View.OnClickListener {
        val intent = Intent(activity, GasStationActivity::class.java)
        startActivity(intent)
    }

    private fun initRecyclerViewStationList(){
        binding?.root?.recycler_gas_station.apply {
            adapterRecyclerView = context?.let { GasStationListAdapter(it) }
            this?.adapter = adapterRecyclerView
            this?.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}