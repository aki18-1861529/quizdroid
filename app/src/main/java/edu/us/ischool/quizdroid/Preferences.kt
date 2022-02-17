package edu.us.ischool.quizdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class Preferences : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        val sharedPreference =  getSharedPreferences("DOWNLOAD_PREFERENCE", MODE_PRIVATE)
        var editor = sharedPreference.edit()
        val url = findViewById<TextView>(R.id.prefURL)
        val time = findViewById<TextView>(R.id.prefTime)
        if (sharedPreference.getString("URL", "") != "") {
            url.text = sharedPreference.getString("URL", "")
        }
        if (sharedPreference.getInt("downloadTime", 0) != 0) {
            time.text = sharedPreference.getInt("downloadTime", 1).toString()
        }

        val saveBtn = findViewById<Button>(R.id.saveBtn)
        saveBtn.setOnClickListener {
            editor.putString("URL", url.text.toString())
            editor.putInt("downloadTime",time.text.toString().toInt())
            editor.commit()
            Log.i("Preferences", "Preference saved!")
            Toast.makeText(this, "Preferences saved!", Toast.LENGTH_SHORT).show()
        }
    }
}