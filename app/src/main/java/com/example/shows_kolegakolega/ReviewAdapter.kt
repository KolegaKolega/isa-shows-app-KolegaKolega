package com.example.shows_kolegakolega

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shows_kolegakolega.custom_view.ItemReviewView
import com.example.shows_kolegakolega.model.Review

class ReviewAdapter(
    private var items: List<Review>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val itemReview = ItemReviewView(parent.context)
        return ReviewViewHolder(itemReview)
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

    inner class ReviewViewHolder(private val itemReview: ItemReviewView) : RecyclerView.ViewHolder(itemReview.rootView){

        fun bind(item : Review){
            itemReview.setEmail(item.user.email)
            itemReview.setRating(item.rating)
            item.user.imageUrl?.let { itemReview.setProfilePicture(it) }

            if(!item.comment.isNullOrEmpty()){
                itemReview.setComment(item.comment)
            }else{
                itemReview.setCommentVisible(false)
            }

        }

    }
}