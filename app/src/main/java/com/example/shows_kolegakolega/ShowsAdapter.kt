package com.example.shows_kolegakolega

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.shows_kolegakolega.databinding.ViweShowItemBinding
import com.example.shows_kolegakolega.model.Show

class ShowsAdapter(
    private var items: List<Show>,
    private val onClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        val binding = ViweShowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowsViewHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ShowsViewHolder(private val binding: ViweShowItemBinding, private val parent: ViewGroup) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Show){
            binding.showtitle.text = item.title

            binding.showdescription.text = item.description

            Glide.with(parent.context)
                .load(item.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.showimage)

            binding.root.setOnClickListener {
                onClickCallback(item)
            }
        }

    }
}

