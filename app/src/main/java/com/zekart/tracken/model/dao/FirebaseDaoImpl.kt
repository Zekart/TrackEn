package com.zekart.tracken.model.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.zekart.tracken.model.entity.GasStation

class FirebaseDaoImpl {

    private val db:FirebaseFirestore = FirebaseFirestore.getInstance()
    private val listStation = MutableLiveData<String>()


    fun getAllStation() {
        db.collection("test")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    println("${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    fun saveToFireBase(station: GasStation){

        db.collection("environments")
            .document(station.id.toString())
            .set(station)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot written with ID: ")
            }
            .addOnFailureListener { e ->
                println("Error getting documents: $e")
            }
    }


    private fun testFireBase(){
        // Initialize Database


        db.collection("users").document("1")
            .get()
            .addOnSuccessListener { result ->
//                for (document in result) {
//                    println(document.id + " => " + document.data)
//                }
                println(" =>  ${result.id} + ${result.data}")
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }

//        val item = HashMap<String, Any>()
//        item["name"] = "test"
//        db.collection("environments")
//            .add(item)
//            .addOnSuccessListener { documentReference ->
//                Log.d("main", "DocumentSnapshot written with ID: " +
//                        documentReference.id)
//            }
//            .addOnFailureListener { e ->
//                Log.w("main", "Error adding document", e)
//            }

//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    for (document in task.result!!) {
//                        Log.d("1", document.id + " => " + document.data)
//                    }
//                } else {
//                    Log.w("1", "Error getting documents.", task.exception)
//                }
//            }

    }
}