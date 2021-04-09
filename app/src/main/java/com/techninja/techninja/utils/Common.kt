package com.techninja.techninja.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.techninja.techninja.HomeActivity

const val USER_REFERENCE = "Users"
const val TAGG = "TAG-->"

fun showToast(activity: Context?, message: String?) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}

fun View.snack(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

