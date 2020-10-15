package com.zekart.tracken.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zekart.tracken.R
import com.zekart.tracken.adapter.StatisticListAdapter
import com.zekart.tracken.databinding.FragmentGasStationStatisticsBinding
import com.zekart.tracken.utils.ViewUtil
import com.zekart.tracken.viewmodel.FragmentStatisticViewModel
import kotlinx.android.synthetic.main.fragment_gas_station_statistics.view.*

class GasStationStatisticFragment :Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = GasStationStatisticFragment()
    }

    private var binding: FragmentGasStationStatisticsBinding? = null
    private var adapterRecyclerView: StatisticListAdapter? = null
    private lateinit var viewModel: FragmentStatisticViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGasStationStatisticsBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(
            FragmentStatisticViewModel::class.java)

        initRecyclerViewStatistics()

        viewModel.getAllConsumeListTest().observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()){
                ViewUtil.showSnackBar(view,getString(R.string.empty_value_message))
                binding?.root?.lnr_header_statistic?.visibility = View.GONE
            }else{
                val tempList = viewModel.createStatisticToRecycler(it)
                binding?.root?.lnr_header_statistic?.visibility = View.GONE
                adapterRecyclerView?.setConsume(tempList)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(FragmentStatisticViewModel::class.java)
        setHasOptionsMenu(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    private fun initRecyclerViewStatistics(){
        binding?.recyclerStatistic.apply {
            adapterRecyclerView = context?.let { StatisticListAdapter(it) }
            this?.adapter = adapterRecyclerView
            this?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }
    }
}