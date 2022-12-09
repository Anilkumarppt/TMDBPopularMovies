package com.anil.tmdb_movie_api_data.data

import android.content.Context
import android.widget.Toast

class ToastMessage {

    companion object{
       fun toastCustom(context:Context,msg:String){
            Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
        }
    }

}