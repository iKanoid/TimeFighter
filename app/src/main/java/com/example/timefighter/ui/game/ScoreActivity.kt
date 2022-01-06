package com.example.timefighter.ui.game

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timefighter.R
import com.example.timefighter.ui.MainActivity
import com.example.timefighter.util.RecyclerViewAdapter
import com.example.timefighter.util.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ScoreActivity : AppCompatActivity() {

    lateinit var firebase: FirebaseDatabase

    // ArrayList for person names, email Id's and mobile numbers
    var full_name = ArrayList<String>()
    var first_name = ArrayList<String>()
    var last_name = ArrayList<String>()
    var score_user = ArrayList<String>()

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

                full_name.clear()
                score_user.clear()

                Log.e("Count " ,"" + dataSnapshot.getChildrenCount());

                for (userSnapshot in dataSnapshot.children) {
                    val product = userSnapshot.getValue(User::class.java)

                    val firstName: String = java.lang.String.valueOf(product?.firstName)
                    val lastName: String = java.lang.String.valueOf(product?.lastName)
                    val score: String = java.lang.String.valueOf(product?.score)


                    /*arrayListGamePlayers.add(firstName)
                    arrayListGamePlayers.add(lastName)
                    arrayListGamePlayers.add(score)*/

                    full_name.add("$firstName $lastName")

                    //first_name.add(firstName)
                    //last_name.add(lastName)

                    score_user.add(score)


                }

                //System.out.println(products)
                //Toast.makeText(this@GameActivity,first_name.toString(),Toast.LENGTH_LONG).show()


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