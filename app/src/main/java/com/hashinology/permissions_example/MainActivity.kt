package com.hashinology.permissions_example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRequestPermestion = findViewById<Button>(R.id.btnRequestPermestion)
        btnRequestPermestion.setOnClickListener {
            requestPermissions()
        }
    }

    // We Creates 3 Functions to Check whether the user accepted the 3 Permissions
    // checkSelfPermission functions used to check whether the user have accepted a Specific Permission currently or in the past
    // then Compare it with Permission Granted or not?
    private fun hasWriteExteranPermession() =
        // Carefully use the Manifest of Android not Java Util Package
        ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    /*
    Notice that the Permission is String Type (this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
     */
    private fun hasLocationForegroundPermession() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun hasLocationBackgroundPermession() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        } else {
            TODO("VERSION.SDK_INT < Q")
        }

    private fun requestPermissions(){
        // whenever required several permission we put them an String Array
        /*
        this function will check if the User Accepted above Permission and if he did not
        then we Add the Permission to a List and converted to Array and at the end request all Permission
        that not Accepted Before
         */
        var permissionsToRequest = mutableListOf<String>()
        if (!hasWriteExteranPermession()){
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!hasLocationForegroundPermession()){
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (!hasLocationBackgroundPermession()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissionsToRequest.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }
        /*
        After the 3 If Statement Above, we know all the permission the user didn't accepted Before,
        So Let Check if the Variable permissionToRequest is not Empty, we request all the permission in that List.
         */
        if (permissionsToRequest.isNotEmpty()){
            /*
            this below function will take ActvityContext not applicationContext
            and converted the Mutable list to String Array using permissionsToRequest.toTypedArray()
            and Finally We Need To Defined the requestCode Int Type
             */
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
        }
    }
    /*
    this Functions Loop through all What user Accepted and Declined permissions
    then We Check if the User Accepted Particular Permissions or Not?
    by override Functions in the Activity Class
     */
    override fun onRequestPermissionsResult(
        /*
        the requestedCode Variable is the same we have defined in ActivityCompat.requestPermissions() above,
        it used to Differentiate Between Several Requested Permissions as each Permission have Unique requestCode
         */
        requestCode: Int,
        // permissions Variables Contained our Permissions that we've Requested
        permissions: Array<out String>,
        /*
        the grantResults Int Array Variable that exactly Contained
        the PackageManager.PERMISSION_GRANTED which is Also Integer
         */
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()){

            /*
            this loop grantResults.indices is similar like  for (i in 0..grantResults.size - 1){}
            and its Loop through the grantResults Array and Check a Particular Permissions
            been Accepted/Granted or Not?
             */
            for (i in grantResults.indices){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.d("PermissionRequested", "${permissions[i]} is Graanted.")
                }
            }
        }
    }
}