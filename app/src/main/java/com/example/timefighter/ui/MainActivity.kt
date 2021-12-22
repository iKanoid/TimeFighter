package com.example.timefighter.ui

import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.timefighter.R

class MainActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        /*Set Status bar Color*/
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window?.statusBarColor = resources?.getColor(R.color.white)!!
        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    
}