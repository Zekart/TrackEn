package com.zekart.tracken.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException

class FireBaseRepository(private val firebase: FirebaseFirestore) {

    private var mListStations = MutableLiveData<Map<String, Any>?>()
    private var mListConsume = MutableLiveData<Map<String, Any>?>()

    fun getStoredData(){

        try {
            firebase.collection("db").document("station_list")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    mListStations.postValue(querySnapshot.data)
                }
                .addOnFailureListener { exception ->
                    mListStations.postValue(null)
                }

            firebase.collection("db").document("consume")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    mListConsume.postValue(querySnapshot.data)
                }
                .addOnFailureListener { exception ->
                    mListConsume.postValue(null)
                }

        }catch (n: NullPointerException){
            n.printStackTrace()
        }catch (io: IOException){
            io.printStackTrace()
        }
    }

    fun getDataStation():LiveData<Map<String, Any>?>{
        return mListStations
    }

    fun getDataConsume():LiveData<Map<String, Any>?>{
        return mListConsume
    }
}