package com.example.unit2

import android.content.ActivityNotFoundException
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.unit2.databinding.WishlistItemBinding

class WishlistAdapter(
    private val items: MutableList<WishlistItem>,
) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: WishlistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WishlistItem) {
            binding.nameTextView.text = item.name
            binding.priceTextView.text = item.price
            binding.urlTextView.text = item.url

            // Click item to open URL in browser
            binding.root.setOnClickListener {
                var url = item.url.trim()
                if (url.isNotEmpty()) {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "https://$url"
                    }
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        it.context.startActivity(intent)
                    } catch (_: ActivityNotFoundException) {
                        Toast.makeText(it.context, "Invalid URL or browser unavailable", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Long click item to delete from list
            binding.root.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    items.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
                    Toast.makeText(it.context, "'${item.name}' removed from wishlist", Toast.LENGTH_SHORT).show()
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WishlistItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
