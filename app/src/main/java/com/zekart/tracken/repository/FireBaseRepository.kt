package com.zekart.tracken.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException

class FireBaseRepository(private val firebase: FirebaseFirestore) {

    private var mListStations = MutableLiveData<Map<String, Any>?>()
    private var mListConsume = MutableLiveData<Map<String, Any>?>()

    fun getStoredData(userId:Long){
        try {
            firebase.collection("stations").document(userId.toString())
                .get()
                .addOnSuccessListener { querySnapshot ->
                    mListStations.postValue(querySnapshot.data)
                }
                .addOnFailureListener { _ ->
                    mListStations.postValue(null)
                }

            firebase.collection("consumes")
                .document(userId.toString())
                .get()
                .addOnSuccessListener { querySnapshot ->
                    mListConsume.postValue(querySnapshot.data)
                }
                .addOnFailureListener { _ ->
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