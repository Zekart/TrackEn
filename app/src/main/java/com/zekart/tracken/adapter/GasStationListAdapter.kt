package com.zekart.tracken.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zekart.tracken.R


class GasStationListAdapter(context: Context, gasStationList: ArrayList<String>):
    RecyclerView.Adapter<GasStationListAdapter.SelfRecyclerViewHolder>() {

    private var mContext:Context = context
    private var mListGasStation = gasStationList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfRecyclerViewHolder {
        val rootView: View = LayoutInflater.from(mContext).inflate(R.layout.item_layout_gas_station_list_recycler, parent, false)
        return SelfRecyclerViewHolder(rootView);
    }

    override fun getItemCount(): Int {
        return mListGasStation.size
    }

    override fun onBindViewHolder(holder: SelfRecyclerViewHolder, position: Int) {
        holder.txtCounter.text = position.toString()
    }

    class SelfRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCounter:TextView = itemView.findViewById(R.id.tx_view_gas_station_counter)
        val txtStationName:TextView = itemView.findViewById(R.id.tx_view_gas_station_name)
        val txtStationAddress:TextView = itemView.findViewById(R.id.tx_view_gas_station_address)
    }
}