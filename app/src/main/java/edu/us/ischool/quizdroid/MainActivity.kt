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
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

lateinit var alarmManager : AlarmManager
private lateinit var pendingIntent : PendingIntent
lateinit var sharedPreference : SharedPreferences
var url = ""
var time = 0

class IntentListener : BroadcastReceiver() {
    init {
        Log.i("BroadcastReceiver", "Created BroadcastReceiver")
    }
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (sharedPreference.getString("URL", "") != "") {
            url = sharedPreference.getString("URL", "") as String
        }
        if (sharedPreference.getInt("downloadTime", 1) != 0) {
            time = sharedPreference.getInt("downloadTime", 1) * 1000 * 60
        }
        Toast.makeText(p0, url, Toast.LENGTH_LONG).show()
        Log.i("BroadcastReceiver", "$url   $time")

        val airplaneModeStatus = Settings.Global.getInt(p0?.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0)
        Log.i("BroadcastReceiver", airplaneModeStatus.toString())

        val connectionStatus = checkForInternet(p0)
        Log.i("BroadcastReceiver", "Internet connection: $connectionStatus")

        // Airplane mode on = 1
        // Airplane mode off = 0
        val failed = false
        if (airplaneModeStatus == 0 && connectionStatus) {
            thread {
                var result : BufferedReader? = null
                try {
                    val server = URL(url)
                    val client: HttpURLConnection = server.openConnection() as HttpURLConnection
                    client.requestMethod = "GET"

                    result = BufferedReader(InputStreamReader(client.inputStream))
                    var inputLine: String?
                    while (result.readLine().also { inputLine = it } != null)
                        Log.i("BroadcastReceiver", inputLine!!)
                    result.close()
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent)

                } catch (e : Exception) {
                    Log.i("BroadcastReceiver", "Exception $e")
                    val intent = Intent(p0, DownloadFailedDialog::class.java)
                    p0?.startActivity(intent)
                } finally {
                    result?.close()
                }
            }
        } else {
            Log.e("QuizApp", "No internet connection")
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

class MainActivity : AppCompatActivity() {

    private lateinit var arrayAdapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        if (Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 1) {
            val intent = Intent(this, AirplaneModeDialog::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            startActivity(intent)
        }

        sharedPreference =  getSharedPreferences("DOWNLOAD_PREFERENCE", MODE_PRIVATE)
        if (sharedPreference.getString("URL", "") != "") {
            url = sharedPreference.getString("URL", "") as String
        }
        if (sharedPreference.getInt("downloadTime", 1) != 0) {
            time = sharedPreference.getInt("downloadTime", 1) * 1000 * 60
        }

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, IntentListener::class.java)
        intent.putExtra("url", url)
        intent.putExtra("time", time)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent)
        Log.i("BroadcastReceiver", "Set download timer")

        val quizApp = QuizApp()
        val repo : TopicRepository = quizApp.getTopicRepository()

        val allTopics = repo.getAllTopics()
        val allTitles = arrayOfNulls<String>(allTopics.size)
        for (i in allTitles.indices) {
            allTitles[i] = allTopics[i].title
        }

        val listView = findViewById<ListView>(R.id.listView)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, allTitles)
        listView.adapter = arrayAdapter
        listView.setOnItemClickListener { _, _, pos, _ ->

            val intent = Intent(this, TopicOverview::class.java).apply {
                putExtra("topic", pos)
            }
            startActivity(intent)
        }
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
            val intent = Intent(this, AirplaneModeDialog::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            startActivity(intent)
        }
    }
}