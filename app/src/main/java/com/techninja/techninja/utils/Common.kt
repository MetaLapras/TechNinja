package com.techninja.techninja.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar



fun showToast(activity: Context?, message: String?) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}

fun View.snack(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}