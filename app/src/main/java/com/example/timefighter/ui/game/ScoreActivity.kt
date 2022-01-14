package com.example.timefighter.ui.game

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timefighter.R
import com.example.timefighter.util.RecyclerViewAdapter
import com.example.timefighter.util.UserMany
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList


class ScoreActivity : AppCompatActivity() {

    lateinit var firebase: FirebaseDatabase

    // ArrayList for person names, email Id's and mobile numbers
    var full_name = ArrayList<String>()
    var first_name = ArrayList<String>()
    var last_name = ArrayList<String>()
    var score_user = ArrayList<String>()

    // Construct the data source
    var arrayOfUsers = ArrayList<User>()


    private var pDialog: ProgressDialog? = null

    companion object {
        private val TAG = ScoreActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        displayLoader()

        firebase = FirebaseDatabase.getInstance()


        //val products = arrayListOf<String>()
        //val products = arrayListOf<User>()
        val ref = FirebaseDatabase.getInstance().getReference("user")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //full_name.clear()
                //score_user.clear()
                arrayOfUsers.clear()

                Log.e("Count " ,"" + dataSnapshot.getChildrenCount());


                for (userSnapshot in dataSnapshot.children) {
                    val product = userSnapshot.getValue(UserMany::class.java)

                    val firstName: String = java.lang.String.valueOf(product?.firstName)
                    val lastName: String = java.lang.String.valueOf(product?.lastName)
                    val score: String = java.lang.String.valueOf(product?.score)

                    val newUser = User("$firstName $lastName", score)

                    arrayOfUsers.add(newUser)

                    //full_name.add("$firstName $lastName")

                    //score_user.add(score)


                }

                arrayOfUsers.sortByDescending{it.score}

                // Create the adapter to convert the array to views
                val adapter = UsersAdapter(applicationContext, arrayOfUsers)

                // Attach the adapter to a ListView
                val listView: ListView = findViewById<ListView>(R.id.listViewScores)

                listView.adapter = adapter

                 /**

                score_user.sort()
                score_user.reverse()

                val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

                //  call the constructor of CustomAdapter to send the reference and data to Adapter
                val customAdapter = RecyclerViewAdapter(
                    this@ScoreActivity,
                    full_name,
                    // first_name,
                    // last_name,
                    score_user
                )
                recyclerView.layoutManager = LinearLayoutManager(this@ScoreActivity)
                recyclerView.adapter = customAdapter // set the Adapter to RecyclerView

                **/

                pDialog?.dismiss()

                /*val data = arrayListOf<String>();
                 for (i in 1..dataSnapshot.getChildrenCount()) {
                     data.add("Item " + dataSnapshot.getChildrenCount())
                 }

                 val adapter = CustomAdapter(this@GameActivity, arrayListGamePlayers)
                 recyclerView.layoutManager = LinearLayoutManager(this@GameActivity)
                 recyclerView.adapter = adapter
                 */

            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })

        //Log.e( TAG, "ArraylIst: "+products)


    }

    private fun displayLoader() {
        pDialog = ProgressDialog(this@ScoreActivity)
        pDialog!!.setMessage("Loading score board.. Please wait...")
        pDialog!!.setIndeterminate(false)
        pDialog!!.setCancelable(false)
        pDialog!!.show()
    }

}