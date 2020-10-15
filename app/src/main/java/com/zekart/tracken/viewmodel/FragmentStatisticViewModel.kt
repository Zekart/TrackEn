package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.GasStation
import com.zekart.tracken.model.entity.GasStationToConsume
import com.zekart.tracken.model.pojo.StatisticResponse
import com.zekart.tracken.repository.StationRepository
import com.zekart.tracken.utils.DataAppUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentStatisticViewModel(application: Application): AndroidViewModel(application) {

    private val repository: StationRepository
    private val mUserId: Long = DataAppUtil.getUserID(application)
    private var mConsumeListTest: LiveData<List<GasStationToConsume>> = MutableLiveData()

    init {
        val dao = GasStationDataBase.getDatabase(application).stationDao()
        repository = StationRepository(dao)
        allConsumeTest()
    }

    private fun allConsumeTest()= viewModelScope.launch(Dispatchers.IO) {
        mConsumeListTest = repository.getAllConsumeTest(mUserId)
    }

    fun getAllConsumeListTest():LiveData<List<GasStationToConsume>>{
        return mConsumeListTest
    }

    fun createStatisticToRecycler(list:List<GasStationToConsume>): ArrayList<StatisticResponse> {
        val statisticList:ArrayList<StatisticResponse> = ArrayList()
        val hashStation: HashSet<GasStation> = HashSet()

        for (ind in list){
            ind.station.let { hashStation.add(it) }
        }

        for (inc in hashStation){
            val count = list.count { inc.mPositionInfo.mAddressInfo == it.station.mPositionInfo.mAddressInfo}
            statisticList.add(StatisticResponse(inc.mConcernName,inc.mPositionInfo.mAddressInfo,count))
        }
        return statisticList
    }
}