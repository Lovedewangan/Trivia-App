package com.example.triviaapp


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var timer: CountDownTimer
    private var timeLeftInMillis: Long = 300000 // 5 minutes for the entire quiz
    private var timerRunning = false
    private lateinit var timerTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var nextButton: Button
    private lateinit var startQuizButton: Button
    private lateinit var scoreTextView: TextView

    // WebGL-related questions and answers
    private val questions = arrayOf(
        "Q.1 Which of the following is the most commonly used programming language for Android app development?",
        "Q.2 What is the primary language used for iOS app development?",
        "Q.3 Which of the following is a cross-platform framework for mobile app development?",
        "Q.4 What is the main purpose of Firebase in mobile app development?",
        "Q.5 Which mobile app development framework allows building apps for both Android and iOS using a single codebase?"
    )
    private val answers = arrayOf(
        arrayOf("Kotlin","Swift","Objective-C","Ruby"),
        arrayOf("Swift", "Java", "Kotlin", "Python"),
        arrayOf("React Native", "Xcode", "Android Studio", "SwiftUI"),
        arrayOf("Real-time database and backend services", "Front-end framework", "User interface design tool", "App debugging tool"),
        arrayOf("Flutter", "Objective-C", "Kotlin", "Java")
    )

    private var currentQuestion = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        timerTextView = findViewById(R.id.timer)
        questionTextView = findViewById(R.id.question)
        radioGroup = findViewById(R.id.answers)
        nextButton = findViewById(R.id.next_button)
        startQuizButton = findViewById(R.id.start_quiz_button)
        scoreTextView = findViewById(R.id.score)

        // Hide quiz elements initially
        hideQuizElements()

        startQuizButton.setOnClickListener {
            startQuiz()
        }

        nextButton.setOnClickListener {
            checkAnswer()
            if (currentQuestion < 4) {
                currentQuestion++
                updateQuestion()
            } else {
                finishQuiz()
            }
        }
    }

    private fun startQuiz() {
        // Hide the start button, show quiz elements
        startQuizButton.visibility = View.GONE
        showQuizElements()

        currentQuestion = 0
        score = 0
        updateQuestion()

        // Start the timer when the quiz begins
        startTimer()
    }

    private fun updateQuestion() {
        questionTextView.text = questions[currentQuestion]
        val options = answers[currentQuestion]
        for (i in 0 until radioGroup.childCount) {
            (radioGroup.getChildAt(i) as RadioButton).text = options[i]
        }
        radioGroup.clearCheck()
    }

    private fun checkAnswer() {
        val selectedOptionId = radioGroup.checkedRadioButtonId
        if (selectedOptionId != -1) {
            val selectedAnswer = findViewById<RadioButton>(selectedOptionId).text
            if (selectedAnswer == answers[currentQuestion][0]) {
                score++
            }
        }
    }

    private fun startTimer() {

        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Time's up!", Toast.LENGTH_SHORT).show()
                finishQuiz()
            }
        }.start()
        timerRunning = true
    }

    private fun updateTimer() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        timerTextView.text = String.format("Time: %02d:%02d", minutes, seconds)
    }

    private fun finishQuiz() {
        // Hide quiz elements and show the final score
        hideQuizElements()
        scoreTextView.text = "Final Score: $score/5"
        scoreTextView.visibility = View.VISIBLE
        startQuizButton.visibility = View.VISIBLE // Option to retake the quiz
    }

    private fun hideQuizElements() {
        questionTextView.visibility = View.GONE
        radioGroup.visibility = View.GONE
        timerTextView.visibility = View.GONE
        nextButton.visibility = View.GONE
    }

    private fun showQuizElements() {
        questionTextView.visibility = View.VISIBLE
        radioGroup.visibility = View.VISIBLE
        timerTextView.visibility = View.VISIBLE
        nextButton.visibility = View.VISIBLE
        scoreTextView.visibility = View.GONE
    }

    // Override the back button to show exit confirmation
    override fun onBackPressed() {
        super.onBackPressed()
        showExitConfirmationDialog()
    }

    // Show a dialog when user tries to exit the quiz
    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to exit the quiz?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog: DialogInterface, _: Int ->
                // Just finish the current activity and go back to the previous one in the back stack
                finish()
            }
            .setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }


}