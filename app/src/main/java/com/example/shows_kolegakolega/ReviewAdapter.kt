package com.example.shows_kolegakolega

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shows_kolegakolega.databinding.ItemReviwBinding
import com.example.shows_kolegakolega.model.Review

class ReviewAdapter(
    private var items: List<Review>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviwBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(review: Review){
        items = items + review
        notifyItemInserted(items.lastIndex)
    }

    inner class ReviewViewHolder(private val binding : ItemReviwBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : Review){
            binding.name.text = item.name
            binding.rating.text = item.rating.toString()

            if(!item.comment.isNullOrEmpty())
                binding.reviweComment.text = item.comment
        }

    }
}