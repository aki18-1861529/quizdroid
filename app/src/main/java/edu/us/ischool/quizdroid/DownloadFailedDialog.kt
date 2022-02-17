package edu.us.ischool.quizdroid

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings

class DownloadFailedDialog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Download Failed")
        alertDialogBuilder.setMessage("Failed to download questions from given URL. Would you like to retry or quit the application and try again later?")
        alertDialogBuilder.setPositiveButton("Retry") { _: DialogInterface, _: Int -> finish() }
        alertDialogBuilder.setNegativeButton("Quit") { _: DialogInterface, _: Int -> finishAffinity() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}