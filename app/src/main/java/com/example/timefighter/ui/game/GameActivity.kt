package com.example.timefighter.ui.game

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timefighter.BuildConfig
import com.example.timefighter.R
import com.example.timefighter.databinding.ActivityGameBinding
import com.example.timefighter.ui.MainActivity
import com.example.timefighter.util.RecyclerViewAdapter
import com.example.timefighter.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*
import kotlin.collections.ArrayList


class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
//    private lateinit var sessionManager: SessionManager
//    lateinit var pref: SharedPreferences

    internal var score = 0
    internal var gameStarted = false

    // Count down fields
    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 60000
    internal val countDownInterval: Long = 1000
    internal var timeLeftOnTimer: Long = 60000

    internal lateinit var blinkAnimation: Animation

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    lateinit var firebase: FirebaseDatabase

    // ArrayList for person names, email Id's and mobile numbers
    var full_name = ArrayList<String>()
    var first_name = ArrayList<String>()
    var last_name = ArrayList<String>()
    var score_user = ArrayList<String>()

    private var pDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        displayLoader()

        firebase = FirebaseDatabase.getInstance()

        //val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        //Toast.makeText(this, "" + currentFirebaseUser!!.getUid(), Toast.LENGTH_LONG).show()


        val products = arrayListOf<String>()
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
                     this@GameActivity,
                    full_name,
                    // first_name,
                    // last_name,
                     score_user
                 )
                 recyclerView.layoutManager = LinearLayoutManager(this@GameActivity)
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

        Log.e(TAG, "ArraylIst: "+products)



//        pref = application.getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE)
//        sessionManager = SessionManager(pref)

        Log.d(TAG, "onCreate called. Score is: $score")

        /*binding.tapMeButton.setOnClickListener {

            // Animation for Button
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            view.startAnimation(bounceAnimation)

            incrementScore()
        }*/

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    private fun displayLoader() {
        pDialog = ProgressDialog(this@GameActivity)
        pDialog!!.setMessage("Loading score board.. Please wait...")
        pDialog!!.setIndeterminate(false)
        pDialog!!.setCancelable(false)
        pDialog!!.show()
    }


    private fun restoreGame() {
        gameScoreTextView.text = getString(R.string.yourScore, score)

        val restoredTime = timeLeftOnTimer / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, restoredTime)

        countDownTimer = object : CountDownTimer(timeLeftOnTimer, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000

                // Animation starts when time left is 10 seconds
                displayTimeLeft(timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }

        countDownTimer.start()
        gameStarted = true
    }

    override fun onStart() {
        super.onStart()

        Log.d(TAG, "onStart called.")
    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume called.")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time Left: $timeLeftOnTimer")
    }

    private fun resetGame() {
        score = 0

        binding.gameScoreTextView.text = getString(R.string.yourScore, score)

        val initialTimeLeft = initialCountDown / 1000
        binding.timeLeftTextView.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000

                // Animation starts when time left is 10 seconds
                displayTimeLeft(timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false
    }

    // Animation starts when time left is 10 seconds
    private fun displayTimeLeft(timeLeft: Long) {
        if(timeLeft <= 10){
            blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
            timeLeftTextView.startAnimation(blinkAnimation)
            timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
        } else {
            timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
        }
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun incrementScore() {

        // Starts the counter
        if (!gameStarted)
            startGame()

        // Increases the score with each tap
        score += 1
        val newScore = getString(R.string.yourScore, score)
        binding.gameScoreTextView.text = newScore

        // Animation for TextView
        blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        gameScoreTextView.startAnimation(blinkAnimation)
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.gameOverMessage, score),
            Toast.LENGTH_LONG).show()

        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser


        val currentUserObj = currentFirebaseUser?.uid?.let { firebase.reference.child("user") }
        currentUserObj?.child(currentFirebaseUser.uid)!!.child("score").setValue(score)
        Toast.makeText(this, "Score saved", Toast.LENGTH_SHORT).show()

        //resetGame()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionAbout) {
            //showInfo()
            ShowPopup()
        }
        return true
    }

    @SuppressLint("StringFormatInvalid")
    private fun showInfo() {
        val dialogTitle = getString(R.string.aboutTitle, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.aboutMessage)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }

    fun ShowPopup() {
        val li = LayoutInflater.from(this)
        val promptsView: View = li.inflate(R.layout.dialog_display, null)
        val alertDialogBuilder = android.app.AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        alertDialogBuilder.setCancelable(true)
        val alertDialog: android.app.AlertDialog? = alertDialogBuilder.create()
        alertDialog?.show()
    }

}