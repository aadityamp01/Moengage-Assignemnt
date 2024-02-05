package com.aaditya.moengageassignemnt

import android.app.Application
import com.google.firebase.FirebaseApp

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}