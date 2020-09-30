package com.yoonlab.gallarypractice

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.photoitem_layout.view.*

class MainViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    var itemContainer : View = view

    fun itemBinder(data: PhotoUri) {
        Glide.with(itemContainer).load(data.itemUri).into(itemContainer.imageViewHolder)
    }
}