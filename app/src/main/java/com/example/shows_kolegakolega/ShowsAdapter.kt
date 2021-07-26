package com.example.shows_kolegakolega

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shows_kolegakolega.databinding.ViweShowItemBinding
import com.example.shows_kolegakolega.model.Show

class ShowsAdapter(
    private var items : List<Show>,
    private val onClickCallback : (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        val binding = ViweShowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ShowsViewHolder(private val binding: ViweShowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Show){
            binding.showtitle.text = item.name

            binding.showdescription.text = item.description

            binding.showimage.setImageResource(item.image)

            binding.root.setOnClickListener {
                onClickCallback(item)
            }
        }

    }
}