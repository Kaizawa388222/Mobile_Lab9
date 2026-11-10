package vn.edu.vlu.lab09.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import vn.edu.vlu.lab09.R
import vn.edu.vlu.lab09.data.PhotoItem
import vn.edu.vlu.lab09.databinding.ItemPhotoBinding

/**
 * RecyclerView Adapter displaying PhotoItem list using ViewBinding and Glide.
 * Features:
 * - 100% ViewBinding (zero findViewById)
 * - Asynchronous image loading with placeholder, error fallback, cross-fade, and DiskCacheStrategy.ALL
 * - Glide cancellation in onViewRecycled() to prevent image flickering and memory overflow
 */
class PhotoAdapter(
    private var photoList: List<PhotoItem> = emptyList(),
    private val onItemClick: ((PhotoItem) -> Unit)? = null
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photoList[position]

        holder.binding.tvPhotoTitle.text = photo.title
        holder.binding.tvPhotoAuthor.text = "Tác giả: ${photo.author}"

        // Glide asynchronous loading with disk caching strategy as specified
        Glide.with(holder.itemView.context)
            .load(photo.imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_broken_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.ivPhoto)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(photo)
        }
    }

    override fun getItemCount(): Int = photoList.size

    /**
     * Cancel pending Glide requests when view is recycled.
     * Prevents image flickering and memory overflow.
     */
    override fun onViewRecycled(holder: PhotoViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(holder.itemView.context).clear(holder.binding.ivPhoto)
    }

    fun submitList(newList: List<PhotoItem>) {
        this.photoList = newList
        notifyDataSetChanged()
    }
}
