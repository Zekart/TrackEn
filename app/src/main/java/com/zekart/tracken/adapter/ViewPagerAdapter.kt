package com.zekart.tracken.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zekart.tracken.ui.fragment.GasStationListFragment
import com.zekart.tracken.ui.fragment.GasStationStatisticFragment

/**
 * Adapter for creating two fragment for ViewPager.
 * @
 **/

class ViewPagerAdapter(fragmentAct: FragmentActivity) :
    FragmentStateAdapter(fragmentAct) {

    private val mFragmentList: MutableList<Fragment> = ArrayList()

    init {
        mFragmentList.add(GasStationListFragment.newInstance())
        mFragmentList.add(GasStationStatisticFragment.newInstance())
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }
}