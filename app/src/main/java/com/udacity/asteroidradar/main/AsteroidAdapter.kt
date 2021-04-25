package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.AsteroidDomainModel
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding


class AsteroidAdapter(val clickListener: AsteroidListener):androidx.recyclerview.widget.ListAdapter<AsteroidDomainModel, AsteroidAdapter.ViewHolder>(AsteroidDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(clickListener,item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: ListItemAsteroidBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(clickListener: AsteroidListener, item: AsteroidDomainModel){
            binding.asteroid = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }
}

class AsteroidDiffCallback : DiffUtil.ItemCallback<AsteroidDomainModel>() {
    override fun areItemsTheSame(oldItem: AsteroidDomainModel, newItem: AsteroidDomainModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AsteroidDomainModel, newItem: AsteroidDomainModel): Boolean {
        return oldItem == newItem
    }
}

class AsteroidListener(val clickListener: (asteroid: AsteroidDomainModel) -> Unit) {
    fun onClick(asteroid: AsteroidDomainModel) = clickListener(asteroid)
}
