package com.zekart.tracken.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zekart.tracken.R
import com.zekart.tracken.model.entity.Consume
import com.zekart.tracken.model.entity.ConsumeToGasStation

class StatisticListAdapter(context: Context): RecyclerView.Adapter<StatisticListAdapter.SelfRecyclerViewHolder>() {
    private var mContext:Context = context
    private var mConsumeToGasStation:List<ConsumeToGasStation> = emptyList()
    //private var mListConsume = emptyList<Consume>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfRecyclerViewHolder {
        val rootView: View = LayoutInflater.from(mContext).inflate(
            R.layout.item_layout_statistic_list,
            parent,
            false
        )
        return StatisticListAdapter.SelfRecyclerViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: SelfRecyclerViewHolder, position: Int) {
        holder.txtStationConcern.text = mConsumeToGasStation[position].mGasStation.mOwner

        mConsumeToGasStation[position].mGasStation.let {
            holder.txtStationAddress.text = it.mPositionInfo?.mAddressInfo
        }
        mConsumeToGasStation[position].mConsumeFromGasStationLists.let {
            holder.txtStationVisits.text = it.size.toString()
        }
    }

    override fun getItemCount(): Int {
        var s = mConsumeToGasStation.size
        return mConsumeToGasStation.size
    }

    class SelfRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtStationConcern: TextView = itemView.findViewById(R.id.tx_view_statistic_gas_station_concern)
        val txtStationAddress: TextView = itemView.findViewById(R.id.tx_view_statistic_gas_station_address)
        val txtStationVisits: TextView = itemView.findViewById(R.id.tx_view_statistic_gas_station_visits)
    }

    internal fun setConsume(consume: List<ConsumeToGasStation>){
        this.mConsumeToGasStation = consume
        notifyDataSetChanged()
    }
}