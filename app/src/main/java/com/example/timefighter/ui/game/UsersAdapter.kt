package com.example.timefighter.ui.game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.timefighter.R
import java.util.*

class UsersAdapter(context: Context?, users: ArrayList<User>) : ArrayAdapter<User?>(
    context!!, 0, users!! as List<User?>
) {
    var number = 1
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        number++

        // Get the data item for this position
        val user = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_scores, parent, false)
        }

        // Lookup view for data population
        val tvName = convertView!!.findViewById<View>(R.id.tv_FullName) as TextView
        val tvHome = convertView.findViewById<View>(R.id.tv_score) as TextView
        val imgScore = convertView.findViewById<View>(R.id.imgScore) as ImageView

        // Populate the data into the template view using the data object
        if (number == 2) {
            imgScore.setBackgroundResource(R.drawable.ic_looks_one_24)
        } else if (number == 3) {
            imgScore.setBackgroundResource(R.drawable.ic_looks_two_24)
        } else if (number == 4) {
            imgScore.setBackgroundResource(R.drawable.ic_looks_3_24)
        }
        tvName.text = user!!.name
        tvHome.text = user.score

        // Return the completed view to render on screen
        return convertView
    }
}