package com.example.myapplication

import android.app.Application
import com.example.myapplication.dependenciesinjection.ContainerApp
import com.example.myapplication.dependenciesinjection.InterfaceContainerApp

class KrsApp : Application() {
    //fungsinya untuk menyimpan instance ContainerApp
    lateinit var containerApp: ContainerApp

    override fun onCreate() {
        super.onCreate()
        //membuat instance ContainerApp
        containerApp = ContainerApp(this)
        //instance adalah object yang dibuat dari class
    }
}