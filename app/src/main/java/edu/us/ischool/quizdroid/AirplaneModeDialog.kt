package edu.us.ischool.quizdroid

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings

class AirplaneModeDialog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Airplane Mode On")
        alertDialogBuilder.setMessage("Airplane mode is on. Would you like to go to settings to turn airplane mode off?")
        alertDialogBuilder.setPositiveButton("Settings") { _: DialogInterface, _: Int -> this.startActivity(
            Intent(Settings.ACTION_SETTINGS)
        ) }
        alertDialogBuilder.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> finish() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}