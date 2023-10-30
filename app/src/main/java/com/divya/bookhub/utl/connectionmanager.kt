package com.divya.bookhub.utl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class connectionmanager {

    fun checkconnectivity(context:Context):Boolean{

        val connectivitymanager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork:NetworkInfo?=connectivitymanager.activeNetworkInfo

        if(activeNetwork?.isConnected!=null){
            return activeNetwork.isConnected
        }
        else{
            return false
        }
    }
}