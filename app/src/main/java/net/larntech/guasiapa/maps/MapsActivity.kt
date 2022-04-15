package net.larntech.guasiapa.maps

import android.graphics.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import net.larntech.guasiapa.R
import net.larntech.guasiapa.databinding.ActivityMapsBinding
import net.larntech.guasiapa.model.stories.StoriesResponse
import net.larntech.guasiapa.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var storiesResponseList: MutableList<StoriesResponse.ListStoryBean>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storiesResponseList = arrayListOf()


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setToolBar();

    }

    private fun setToolBar(){
        this.setSupportActionBar(binding.toolbar);
        this.supportActionBar!!.title = "Stories on Map"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        fetchAllStories();
    }


    private fun fetchAllStories(){
        showMessage(" Fetching stories ...")
        val apiCall = ApiClient.getApiService().allStories();
        apiCall.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(call: Call<StoriesResponse>, response: Response<StoriesResponse>) {
                if(response.isSuccessful){
                    if(response.body()!!.listStory != null && response.body()!!.listStory!!.isNotEmpty()){
                        storiesResponseList = response.body()!!.listStory as MutableList<StoriesResponse.ListStoryBean>
                        showMarkers()
                    }
                }else{
                    showMessage("Unable to complete response")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
            }

        })

    }


    private fun showMarkers(){

        for(storeResponse in storiesResponseList){
            addMarker(LatLng(storeResponse.lat,storeResponse.lon),storeResponse.name!!, storeResponse.photoUrl!!)
        }

    }

    private fun addMarker( latlng: LatLng,markerIn: String, url : String ){
        // Add a marker in Sydney and move the camera
        var marker = mMap.addMarker(MarkerOptions().position(latlng).title(markerIn).position(latlng))

        loadMarkerIcon(marker!!,url)
    }


    private fun loadMarkerIcon(marker: Marker, url: String){

        Glide.with(this).asBitmap().load(url).fitCenter().into(object: SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                var mBitmap:Bitmap = getCroppedBitmap(resource)!!;
                var icon:BitmapDescriptor = BitmapDescriptorFactory.fromBitmap(mBitmap);
                marker.setIcon(icon);
            }

        })

    }


    fun getCroppedBitmap(bitmap: Bitmap): Bitmap? {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        paint.setColor(color)
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(), paint
        )
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        var _bmp = Bitmap.createScaledBitmap(output, 120, 120, false);
        return _bmp;
    }

    private fun showMessage(message: String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}