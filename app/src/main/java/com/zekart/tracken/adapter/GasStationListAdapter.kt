package com.zekart.tracken.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zekart.tracken.R
import com.zekart.tracken.model.entity.GasStation
import com.zekart.tracken.ui.listeners.GasStationAdapterListener

/**
 * Adapter for showing list of gas stations.
 *
 * **/

class GasStationListAdapter(context: Context, listener: GasStationAdapterListener):
    RecyclerView.Adapter<GasStationListAdapter.SelfRecyclerViewHolder>() {

    private var mContext:Context = context
    private var mListStation = emptyList<GasStation>()
    private var mStationAdapterListener: GasStationAdapterListener? = null

    init {
        this.mStationAdapterListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfRecyclerViewHolder {
        val rootView: View = LayoutInflater.from(mContext).inflate(
            R.layout.item_layout_gas_station_list_recycler,
            parent,
            false
        )
        return SelfRecyclerViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return mListStation.size
    }

    override fun onBindViewHolder(holder: SelfRecyclerViewHolder, position: Int) {

        val increment = position + 1
        holder.txtCounter.text = increment.toString()
        holder.txtStationName.text = mListStation[position].mConcernName
        holder.txtStationAddress.text = mListStation[position].mPositionInfo.mAddressInfo
        holder.itemView.setOnClickListener {
            mStationAdapterListener?.onGasStationClick(mListStation[position].id)
        }
    }

    class SelfRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCounter:TextView = itemView.findViewById(R.id.tx_view_gas_station_counter)
        val txtStationName:TextView = itemView.findViewById(R.id.tx_view_gas_station_name)
        val txtStationAddress:TextView = itemView.findViewById(R.id.tx_view_gas_station_address)
    }

    /**
     * Update from LiveData value.
     * @param station
     * @see GasStation
     **/
    internal fun setStation(station:List<GasStation>?){
        if (station != null) {
            mListStation = station
        }
        notifyDataSetChanged()
    }
}