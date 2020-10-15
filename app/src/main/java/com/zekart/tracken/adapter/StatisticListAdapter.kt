package com.zekart.tracken.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zekart.tracken.R
import com.zekart.tracken.model.pojo.StatisticResponse

/**
 * Adapter for showing list of statistics.
 *
 * **/
class StatisticListAdapter(context: Context): RecyclerView.Adapter<StatisticListAdapter.SelfRecyclerViewHolder>() {
    private var mContext:Context = context
    private var mStatisticResponseList:List<StatisticResponse> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfRecyclerViewHolder {
        val rootView: View = LayoutInflater.from(mContext).inflate(
            R.layout.item_layout_statistic_list,
            parent,
            false
        )
        return SelfRecyclerViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: SelfRecyclerViewHolder, position: Int) {
        mStatisticResponseList[position].mConcernName.let {
            holder.txtStationConcern.text = it
        }

        mStatisticResponseList[position].mStationAddress.let {
            holder.txtStationAddress.text = it
        }
        mStatisticResponseList[position].mCountUserConsume.let {
            holder.txtStationVisits.text = it.toString()
        }
    }

    override fun getItemCount(): Int {
        return mStatisticResponseList.size
    }

    class SelfRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtStationConcern: TextView = itemView.findViewById(R.id.tx_view_statistic_gas_station_concern)
        val txtStationAddress: TextView = itemView.findViewById(R.id.tx_view_statistic_gas_station_address)
        val txtStationVisits: TextView = itemView.findViewById(R.id.tx_view_statistic_gas_station_visits)
    }


    /**
     * Update from LiveData value.
     * @param consumeStatisticResponse
     * @see StatisticResponse
     **/

    internal fun setConsume(consumeStatisticResponse:List<StatisticResponse>){
        this.mStatisticResponseList = consumeStatisticResponse
        notifyDataSetChanged()
    }
}