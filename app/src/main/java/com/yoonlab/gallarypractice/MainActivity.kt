package com.yoonlab.gallarypractice

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import splitties.alertdialog.alertDialog
import splitties.alertdialog.cancelButton
import splitties.alertdialog.okButton

class MainActivity : AppCompatActivity() {
    private val REQUEST_READ_STORAGE = 1000
    private val photoList = ArrayList<PhotoUri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerViewHorizontal.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val adapter = MainAdapter(photoList)
        recyclerViewHorizontal.adapter = adapter

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // code below will be executed only if the user has denied the permission at least once.
                alertDialog("Permission need to be granted", "Access to the Photo") {
                    okButton {
                        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_STORAGE)
                    }
                    cancelButton {}
                }.show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_STORAGE) //Request Permission for the first time
            }
        } else {
            getAllPhotos()
            adapter.notifyDataSetChanged()
            recyclerViewHorizontal.adapter = adapter
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_READ_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getAllPhotos() //Permission is granted
                } else {
                    Toast.makeText(this, "Permission is denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun getAllPhotos() {
        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        null,
        null,
        null,
        MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC") //SAME AS SQL ORDER BY can be excluded (sometimes SHOULD BE)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Get photo path's URI
                /*var uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.VOLUME_NAME)) +
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH))+
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                 */
                var imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                var uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ""+imageId)

                // DATA: String? is deprecated. Need access file path indirectly (using RELATIVE_PATH, DISPLAY_NAME
                Log.d("MainActivity", uri.toString())

                photoList.add(PhotoUri(uri.toString()))

            }
            cursor.close() //Cursor should be closed as soon as it finishes its task for preventing memory leak.
        }
    }
}