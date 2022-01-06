package com.example.timefighter.ui.game

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.timefighter.BuildConfig
import com.example.timefighter.R
import com.example.timefighter.databinding.ActivityGameBinding
import com.example.timefighter.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_game.*


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firebase = FirebaseDatabase.getInstance()


//        pref = application.getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE)
//        sessionManager = SessionManager(pref)

        Log.d(TAG, "onCreate called. Score is: $score")

        binding.tapMeButton.setOnClickListener {

            // Animation for Button
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            view.startAnimation(bounceAnimation)

            incrementScore()
        }

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
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

        resetGame()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionAbout) {
            showInfo()
        }

        if (item.itemId == R.id.actionHighScore){

            val diceRollIntent = Intent(this, ScoreActivity::class.java) //assigns the intent to a variable which we can use
            startActivity(diceRollIntent)

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

}