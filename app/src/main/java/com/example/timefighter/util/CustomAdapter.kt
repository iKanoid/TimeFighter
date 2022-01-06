package com.example.timefighter.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timefighter.R
import kotlin.collections.ArrayList

class CustomAdapter(private val context: Context, private val list: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //internal var tvFirstName: TextView = itemView.findViewById(R.id.tv_Fname)
        //internal var tvLastName: TextView = itemView.findViewById(R.id.tv_Lname)
        internal var tvScore: TextView = itemView.findViewById(R.id.tv_score)

        init {
            // Initialize your All views present in list items
        }

        internal fun bind(position: Int) {
            // This method will be called anytime a list item is created or update its data
            //Do your stuff here
           if (position == position) {
               // tvFirstName.text = list[position]
               // tvLastName.text = list[position]
                tvScore.text = list[position]
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_scores, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}