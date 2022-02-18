package edu.us.ischool.quizdroid

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.FileOutputStream
import java.net.URL
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

lateinit var alarmManager : AlarmManager
private lateinit var pendingIntent : PendingIntent
lateinit var sharedPreference : SharedPreferences
lateinit var fileOutputStream : FileOutputStream
var url = ""
var time = 0
val fileName = "questions.json"

class IntentListener : BroadcastReceiver() {
    init {
        Log.i("BroadcastReceiver", "Created BroadcastReceiver")
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (sharedPreference.getString("URL", "") != "") {
            url = sharedPreference.getString("URL", "") as String
        }
        if (sharedPreference.getInt("downloadTime", 10) != 0) {
            time = sharedPreference.getInt("downloadTime", 10) * 1000 * 60
        }
        Log.i("BroadcastReceiver", "$url   $time")

        val airplaneModeStatus = Settings.Global.getInt(p0?.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0)
        Log.i("BroadcastReceiver", airplaneModeStatus.toString())

        val connectionStatus = checkForInternet(p0)
        Log.i("BroadcastReceiver", "Internet connection: $connectionStatus")

        // Airplane mode on = 1
        // Airplane mode off = 0
        if (airplaneModeStatus == 0 && connectionStatus) {
            Toast.makeText(p0, url, Toast.LENGTH_LONG).show()
            var result : BufferedReader? = null
            thread {
                try {
                    var resultString: String = URL(url).readText()
                    Log.i("BroadcastReceiver", resultString)
                    if (p0 != null && resultString != null) {
                        fileOutputStream = p0.openFileOutput(fileName, Context.MODE_PRIVATE)
                        fileOutputStream.write(resultString.toByteArray())
                        fileOutputStream.close()
                    }
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + time,
                        pendingIntent
                    )

                } catch (e: Exception) {
                    Log.e("QuizApp", e.toString())
                    val intent = Intent(p0, DownloadFailedDialog::class.java)
                    p0?.startActivity(intent)
                } finally {
                    result?.close()
                }
            }
        } else if (airplaneModeStatus == 1) {
            Log.i("BroadcastReceiver", "Airplane mode alert dialog")
            val intent = Intent(p0, AirplaneModeDialog::class.java)
            p0?.startActivity(intent)
        } else {
            Log.e("QuizApp", "No internet connection")
            Toast.makeText(p0, "Error: no signal to download questions", Toast.LENGTH_LONG).show()
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent)
        }
    }

    private fun checkForInternet(context: Context?): Boolean {

        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var arrayAdapter : ArrayAdapter<String>
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        launch {
            setSupportActionBar(findViewById(R.id.toolbar))

            if (Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 1) {
                airplaneMode()
            }

            sharedPreference = getSharedPreferences("DOWNLOAD_PREFERENCE", MODE_PRIVATE)
            if (sharedPreference.getString("URL", "https://tednewardsandbox.site44.com/questions.json") != "") {
                url = sharedPreference.getString("URL", "https://tednewardsandbox.site44.com/questions.json") as String
            }
            if (sharedPreference.getInt("downloadTime", 10) != 0) {
                time = sharedPreference.getInt("downloadTime", 10) * 1000 * 60
            }

            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, IntentListener::class.java)
            intent.putExtra("url", url)
            intent.putExtra("time", time)
            pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                pendingIntent
            )
            Log.i("BroadcastReceiver", "Set download timer")

            val quizApp = QuizApp()
            val repo: TopicRepository = quizApp.getTopicRepository(context)

            val allTopics = repo.getAllTopics()
            val allTitles = arrayOfNulls<String>(allTopics.size)
            for (i in allTitles.indices) {
                allTitles[i] = allTopics[i].title
            }

            val listView = findViewById<ListView>(R.id.listView)
            arrayAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, allTitles)
            listView.adapter = arrayAdapter
            listView.setOnItemClickListener { _, _, pos, _ ->

                val intent = Intent(context, TopicOverview::class.java).apply {
                    putExtra("topic", pos)
                }
                startActivity(intent)
            }
        }
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_preferences -> {
            val intent = Intent(this, Preferences::class.java)
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 1) {
            airplaneMode()
        }
    }

    private fun airplaneMode() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Airplane Mode On")
        alertDialogBuilder.setMessage("Airplane mode is on. Would you like to go to settings to turn airplane mode off?")
        alertDialogBuilder.setPositiveButton("Settings") { _: DialogInterface, _: Int -> this.startActivity(Intent(Settings.ACTION_SETTINGS)) }
        alertDialogBuilder.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}