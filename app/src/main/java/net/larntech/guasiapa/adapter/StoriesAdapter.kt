package net.larntech.guasiapa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import net.larntech.guasiapa.R
import net.larntech.guasiapa.model.stories.StoriesResponse

class StoriesAdapter(val clicklistener: clickedListener)
    :PagingDataAdapter<StoriesResponse.ListStoryBean, StoriesAdapter.StoriesAdapterVh>(MovieDiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesAdapterVh {
        return StoriesAdapterVh(
            LayoutInflater.from(parent.context).inflate(R.layout.row_stories,parent,false)
        )
    }

    override fun onBindViewHolder(holder: StoriesAdapterVh, position: Int) {
        var storyResponse = getItem(position)
        holder.tvUserName.text = storyResponse!!.name
        holder.tvDescription.text = storyResponse.description
        Picasso.get().load(storyResponse.photoUrl).into(holder.imageView);
        holder.itemView.setOnClickListener {
            clicklistener.Clicked(storyResponse)
        }
    }


     interface clickedListener {
        fun Clicked(story: StoriesResponse.ListStoryBean)
    }

    class StoriesAdapterVh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.imageView)
        var tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
        var tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)

    }

    class MovieDiffCallBack : DiffUtil.ItemCallback<StoriesResponse.ListStoryBean>() {
        override fun areItemsTheSame(oldItem: StoriesResponse.ListStoryBean, newItem: StoriesResponse.ListStoryBean): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StoriesResponse.ListStoryBean, newItem: StoriesResponse.ListStoryBean): Boolean {
            return oldItem == newItem
        }
    }




}