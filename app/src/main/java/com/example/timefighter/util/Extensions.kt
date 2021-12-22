package com.example.timefighter.util

import android.app.Activity
import android.content.Intent
import com.example.timefighter.ui.MainActivity
import com.example.timefighter.R

class Extensions {
}

fun Activity.logOut(sessionManager: SessionManager) {
    Intent(this, MainActivity::class.java).also {
        sessionManager.clearSharedPref()
        sessionManager.saveToSharedPref(
            getString(R.string.login_status),
            getString(R.string.log_out)
        )
        startActivity(it)
        finish()
    }
}