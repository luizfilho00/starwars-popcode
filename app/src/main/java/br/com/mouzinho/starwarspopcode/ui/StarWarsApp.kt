package br.com.mouzinho.starwarspopcode.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm

@HiltAndroidApp
class StarWarsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}