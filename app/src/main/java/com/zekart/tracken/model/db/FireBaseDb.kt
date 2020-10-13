package com.zekart.tracken.model.db

import com.google.firebase.firestore.FirebaseFirestore

abstract class FireBaseDb {
    fun db(): FirebaseFirestore? {
        return FirebaseFirestore.getInstance()
    }
}