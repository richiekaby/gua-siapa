package net.larntech.guasiapa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.squareup.picasso.Picasso
import net.larntech.guasiapa.model.stories.StoriesResponse

class StoryDetailsActivity : AppCompatActivity() {

    private lateinit var story: StoriesResponse.ListStoryBean
    private lateinit var imageView: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tlBar: Toolbar;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_details)
        initData()
    }

    private fun initData(){
        imageView = findViewById(R.id.imageView)
        tvUserName = findViewById(R.id.tvUserName)
        tvDescription = findViewById(R.id.tvDescription)
        tlBar = findViewById(R.id.toolbar)
        val intent = intent;
        story = intent.extras!!.getSerializable("data") as StoriesResponse.ListStoryBean
        Picasso.get().load(story.photoUrl).into(imageView);
        tvUserName.text = story.name
        tvDescription.text = story.description

        setToolBar()

    }

    private fun setToolBar(){
        this.setSupportActionBar(tlBar);
        this.supportActionBar!!.title = "Story Details"
    }
}