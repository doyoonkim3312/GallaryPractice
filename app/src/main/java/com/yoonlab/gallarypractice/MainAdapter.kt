package com.yoonlab.gallarypractice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MainAdapter (val data : ArrayList<PhotoUri>) : RecyclerView.Adapter<MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        //TODO("Not yet implemented")
        println("Called 1")
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.photoitem_layout, parent, false)
        return MainViewHolder(inflateView)
    }

    override fun getItemCount(): Int {
        //TODO("Not yet implemented")
        return data.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        //TODO("Not yet implemented")
        val targetItem = data[position]
        println("Called 3")
        holder.apply {
            itemBinder(targetItem)
            println("Called 4")
        }
    }

}

