package com.leshe4ka.rotator


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.leshe4ka.rotator.databinding.ActivityMainBinding
import com.leshe4ka.rotator.JoyActivity

fun valid(str: Editable): Boolean {
    val lstr =str.toString()
    if (lstr!="" && lstr!="." && lstr!="null"){
        return true
    }
    return false
}

val request = LocationRequest()
var handler = Handler()
var reset_counter:Int=0
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var fusedLocationProvider: FusedLocationProviderClient
    var runnable: Runnable? = null
    var delay = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        val view: View = findViewById(android.R.id.content)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //handler.postDelayed(runnableCode,1000);

        //setSupportActionBar(findViewById(R.id.materialtoolbar))
        setSupportActionBar(binding.materialtoolbar)
        /*  top navigation text */
        /*val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)*/
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        binding.editTextAzimuth.setText("0");
        binding.editTextElevation.setText("0");
        binding.sendAngles.setOnClickListener {
            if (valid(binding.editTextAzimuth.text) && valid(binding.editTextElevation.text)) {
                post_angles(binding.editTextAzimuth.text.toString(),binding.editTextElevation.text.toString(),view)
            }
            else{
                Snackbar.make(view,"Надо хоть что-то написать", Snackbar.LENGTH_LONG).setAction("Закрыть", { }).show()
            }
        }
        binding.getGPS.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProvider.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        binding.editTextHomeLatitude.setText(location?.latitude.toString())
                        binding.editTextHomeLongitude.setText(location?.longitude.toString())
                        if(location?.hashCode()==null){
                            Snackbar.make(view,"GPS выключен, нет данных", Snackbar.LENGTH_LONG).setAction("Открыть настройки") {
                                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }.show()
                            fusedLocationProvider.requestLocationUpdates(request, object : LocationCallback() {
                                override fun onLocationResult(locationResult: LocationResult) {
                                    val location: Location? = locationResult.lastLocation
                                    binding.editTextHomeLatitude.setText(location?.latitude.toString())
                                    binding.editTextHomeLongitude.setText(location?.longitude.toString())
                                }
                            }, null)

                        }
                    }
            }
            else{
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),Priority.PRIORITY_HIGH_ACCURACY)
                Snackbar.make(view,"Необходимо разрешение", Snackbar.LENGTH_LONG).setAction("Открыть настройки") {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }.show()
            }
        }
        binding.sendSatPos.setOnClickListener {
            if(valid(binding.editTextSatLatitude.text) && valid(binding.editTextSatLongitude.text) && valid(binding.editTextSatHeight.text)){
                post_coords_sat(binding.editTextSatLatitude.text.toString(),binding.editTextSatLongitude.text.toString(),binding.editTextSatHeight.text.toString(),view)
            }
            else{
                Snackbar.make(view,"Надо хоть что-то написать", Snackbar.LENGTH_LONG).setAction("Закрыть", { }).show()
            }
        }

        binding.sendHomePos.setOnClickListener {
            if(valid(binding.editTextHomeLatitude.text) && valid(binding.editTextHomeLongitude.text) && valid(binding.editTextHomeHeight.text)){
                post_coords_home(binding.editTextHomeLatitude.text.toString(),binding.editTextHomeLongitude.text.toString(),binding.editTextHomeHeight.text.toString(),view)
            }
            else{
                Snackbar.make(view,"Надо хоть что-то написать", Snackbar.LENGTH_LONG).setAction("Закрыть", { }).show()
            }
        }
        binding.dorotateSwitch.setOnClickListener {
            post_dorotate(binding.dorotateSwitch.isChecked)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.itemId ==R.id.mainMenuResetAngles){
            Snackbar.make(this.findViewById(R.id.scroll),"Точно сбросить углы?", Snackbar.LENGTH_LONG).setAction("Да") { post_reset() }
                .show()
            return true
        }
        if(item.itemId ==R.id.mainMenuOpenJoystick){
            val intent = Intent()
            intent.setClass(this, JoyActivity::class.java)
            startActivity(intent)
            return true
        }
        if(item.itemId ==R.id.mainMenuExit){
            finish();
            return true
        }

        return super.onOptionsItemSelected(item)

        /*return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }*/
    }
    override fun onResume() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            get_data(binding)
        }.also { runnable = it }, delay.toLong())
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable!!)
    }
}