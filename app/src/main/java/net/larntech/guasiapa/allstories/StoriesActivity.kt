package net.larntech.guasiapa.allstories

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.larntech.guasiapa.AddStoryActivity
import net.larntech.guasiapa.LoginActivity
import net.larntech.guasiapa.R
import net.larntech.guasiapa.StoryDetailsActivity
import net.larntech.guasiapa.adapter.StoriesAdapter
import net.larntech.guasiapa.model.stories.StoriesResponse
import net.larntech.guasiapa.network.ApiClient
import net.larntech.guasiapa.network.ApiService
import net.larntech.guasiapa.share_pref.SharePref


class StoriesActivity : AppCompatActivity(), StoriesAdapter.clickedListener {

    private lateinit var tlBar: Toolbar;
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoData: TextView
    private lateinit var fbNewBook: FloatingActionButton
    private lateinit var storyAdapter: StoriesAdapter
    private lateinit var storiesResponseList: MutableList<StoriesResponse.ListStoryBean>;
    lateinit var storyViewModel: StoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories)
        initData()
    }

    private fun initData(){
        tlBar = findViewById(R.id.toolbar)
        tvNoData = findViewById(R.id.tvNoData)
        recyclerView = findViewById(R.id.recyclerview)
        fbNewBook = findViewById(R.id.fbNewBook)
        storyAdapter = StoriesAdapter(this);
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = storyAdapter
        recyclerView.setHasFixedSize(true)
        storiesResponseList = arrayListOf()
        setToolBar();
        setupViewModel()
        fetchAllStories()
        clickListener()
    }

    private fun setupViewModel() {
        val factory = StoriesViewModelFactory(ApiClient.getApiService())
        storyViewModel = ViewModelProvider(this,factory).get(StoryViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    private fun setToolBar(){
        this.setSupportActionBar(tlBar);
        this.supportActionBar!!.title = "Stories"
    }


    private fun fetchAllStories(){

        lifecycleScope.launch {
            storyViewModel.getStories().collectLatest { pagedData ->
                storyAdapter.submitData(pagedData)
            }
        }
//        showMessage(" Fetching stories ...")
//        val apiCall = ApiClient.getApiService().allStories();
//        apiCall.enqueue(object : Callback<StoriesResponse> {
//            override fun onResponse(call: Call<StoriesResponse>, response: Response<StoriesResponse>) {
//                if(response.isSuccessful){
//                  if(response.body()!!.listStory != null && response.body()!!.listStory!!.isNotEmpty()){
//                      storiesResponseList = response.body()!!.listStory as MutableList<StoriesResponse.ListStoryBean>
//                      showStories()
//                  }
//                }else{
//                    showMessage("Unable to complete response")
//                }
//            }
//
//            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
//               tvNoData.visibility = View.VISIBLE
//            }
//
//        })

    }

    private fun showStories(){

    }



    private fun showMessage(message: String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }



    private fun clickListener(){
        fbNewBook.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    override fun Clicked(story: StoriesResponse.ListStoryBean) {
        startActivity(Intent(this, StoryDetailsActivity::class.java).putExtra("data",story))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    private fun logout(){
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout")
            .setPositiveButton("Yes") { _, _ ->
                SharePref.clearPrefs()
                startActivity(Intent(this@StoriesActivity, LoginActivity::class.java))
                finish()
            }
            .setNegativeButton("No") { _, _ -> //set what should happen when negative button is clicked
            }
            .show()
    }
}