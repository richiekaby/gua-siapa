package net.larntech.guasiapa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.annotation.NonNull
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import net.larntech.guasiapa.allstories.StoriesActivity
import net.larntech.guasiapa.model.post_story.PostStoryResponse
import net.larntech.guasiapa.network.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class AddStoryActivity : AppCompatActivity() {
    private lateinit var tlBar: Toolbar
    private lateinit var imageView: ImageView
    private lateinit var openCamera: Button
    private lateinit var openGallery: Button
    private lateinit var edDesc: EditText
    private lateinit var btnUpload: Button
    private var fusedLocationClient: FusedLocationProviderClient? = null
    val REQUESTS_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_GALLERY = 2
    var imageBitmap: Bitmap? = null
    var imageUri: Uri? = null
    private lateinit var location: Location;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)
        initData()
    }

    private fun initData(){
        tlBar = findViewById(R.id.toolbar)
        imageView = findViewById(R.id.imageView)
        openCamera = findViewById(R.id.openCamera)
        openGallery = findViewById(R.id.openGallery)
        edDesc = findViewById(R.id.edDesc)
        btnUpload = findViewById(R.id.btnUpload)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setToolBar()
        clickListener()
    }

    private fun clickListener(){
        openCamera.setOnClickListener {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                    if(report.areAllPermissionsGranted()){
                        openCam()
                    }

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) { /* ... */
                    }
                }).check()

        }

        openGallery.setOnClickListener {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                        if(report.areAllPermissionsGranted()){
            openGallery()
                        }

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) { /* ... */
                    }
                }).check()
        }

        btnUpload.setOnClickListener {
            showPermissionAlert();
        }

    }


    private fun setToolBar(){
        this.setSupportActionBar(tlBar);
        this.supportActionBar!!.title = "Add Story"
    }


    private fun openCam() {
        val openCamIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(
            openCamIntent,REQUESTS_IMAGE_CAPTURE
        )
    }

    private fun openGallery() {
        val openGalleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(
            openGalleryIntent,REQUEST_IMAGE_GALLERY
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUESTS_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                val extras = data!!.extras
                imageBitmap = extras!!["data"] as Bitmap?
                imageUri = getImageUri(imageBitmap!!)
                imageView.setImageBitmap(imageBitmap)
            }
        } else if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (resultCode == RESULT_OK) {
                val uri = data!!.data
                imageUri = uri
                try {
                    imageBitmap = BitmapFactory.decodeStream(
                        this
                            .contentResolver.openInputStream(uri!!)
                    )
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                imageView.setImageBitmap(imageBitmap)
            }
        }
    }




    private fun saveData(){
        if(edDesc.text.isNotEmpty() && imageBitmap != null && ::location.isInitialized){
            val file = bitmapToFile(imageBitmap!!,"image.png")!!
            val reqFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("photo",file.name, reqFile)
            save( edDesc.text.toString(),body,location.latitude.toString(),location.longitude.toString())
        }else{
            showMessage("All info required ")
        }

    }

    private fun getFile(): File{

        return File(imageUri!!.path!!);
    }

    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(getCacheDir(), fileNameToSave)
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }



    private fun showMessage(message: String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }



    private fun save(description: String,photo: MultipartBody.Part, lat: String, lon: String){


        showMessage(" Saving story ...")
        val requestBody: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            description
        )

        val myLat: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            lat
        )
        val myLon: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            lon
        )
        val apiCall = ApiClient.getApiService().postStory(requestBody,myLat,myLon,photo);
        apiCall.enqueue(object : Callback<PostStoryResponse> {
            override fun onResponse(call: Call<PostStoryResponse>, response: Response<PostStoryResponse>) {
                if(response.isSuccessful){
                    showMain()
                }else{
                    showMessage("Unable to save")
                }
            }

            override fun onFailure(call: Call<PostStoryResponse>, t: Throwable) {
                Log.e(" data "," ==> "+t.localizedMessage)
                showMessage("An error occurred "+t.localizedMessage)
            }

        })
    }

    private fun showMain(){
        Handler().postDelayed(Runnable {
                      startActivity(Intent(this, StoriesActivity::class.java))
            finish()
        },1000)
    }

    fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            this.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun fetchLastLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                showPermissionAlert()
                return
            }
        }
        fusedLocationClient!!.lastLocation
            .addOnSuccessListener(
                this
            ) { locatio ->
                // Got last known location. In some rare situations this can be null.
                if (locatio != null) {
                    location = locatio
                    // Logic to handle location object
                    Log.e("LAST LOCATION: ", location.toString())
                    saveData()

                }
            }
    }

    private fun showPermissionAlert() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                    if(report.areAllPermissionsGranted()){
                        fetchLastLocation()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()


    }
}