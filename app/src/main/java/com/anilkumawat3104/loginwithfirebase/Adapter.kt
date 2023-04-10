package com.anilkumawat3104.loginwithfirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anilkumawat3104.internship1.R


class Adapter(private val childItemList: List<data>) :
    RecyclerView.Adapter<Adapter.ChildViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        // Here we inflate the corresponding
        // layout of the child item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        // Create an instance of the ChildItem
        // class for the given position
        val childItem = childItemList[position]

        // For the created instance, set title.
        // No need to set the image for
        // the ImageViews because we have
        // provided the source for the images
        // in the layout file itself
        holder.name.text = childItem.name
        holder.about.text = childItem.about
//        holder.childItemTitle.text = childItem.getChildItemTitle()
        with(holder.itemView){
            with(childItem.url){
                com.squareup.picasso.Picasso.get().load(this).into(holder.image)
            }
        }

    }

    override fun getItemCount(): Int {
        // This method returns the number
        // of items we have added
        // in the ChildItemList
        // i.e. the number of instances
        // of the ChildItemList
        // that have been created
        return childItemList.size

    }

    // This class is to initialize
    // the Views present
    // in the child RecyclerView
    inner class ChildViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var name: TextView =
            itemView.findViewById(R.id.name)

        var about: TextView =
            itemView.findViewById(R.id.about)
        var image: ImageView =
            itemView.findViewById(R.id.image)
    }
}
