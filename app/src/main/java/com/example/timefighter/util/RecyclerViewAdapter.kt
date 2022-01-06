package com.example.timefighter.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timefighter.R
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewAdapter(
    var context: Context,
    var Full_name: ArrayList<String>,
    //var First_name: ArrayList<String>,
    //var Last_name: ArrayList<String>,
    var Score_user: ArrayList<String>,

) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        // infalte the item Layout
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_scores, parent, false)
        return MyViewHolder(v) // pass the view to View Holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        holder.tv_FullName.text = Full_name[position]
        //holder.tv_Fname.text = First_name[position]
        //holder.tv_Lname.text = Last_name[position]
        holder.tv_score.text = Score_user[position]

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener {
            // display a toast with person name on item click
            //Toast.makeText(context, personNames.get(position), Toast.LENGTH_SHORT).show();
        }
    }

    override fun getItemCount(): Int {
        return Full_name.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var tv_Fname: TextView
        //var tv_Lname: TextView
        var tv_FullName:TextView
        var tv_score: TextView


        init {

            // get the reference of item view's
            //tv_Fname = itemView.findViewById<View>(R.id.tv_Fname) as TextView
            //tv_Lname = itemView.findViewById<View>(R.id.tv_Lname) as TextView
            tv_FullName = itemView.findViewById<View>(R.id.tv_FullName) as TextView
            tv_score = itemView.findViewById<View>(R.id.tv_score) as TextView

        }
    }
}