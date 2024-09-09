package com.bignerdranch.android.geoquiz
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var binding: ActivityMainBinding
    private var correctAnswersCount = 0
    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
       if (result.resultCode == Activity.RESULT_OK){
           quizViewModel.isCheater =
               result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
       }
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizVIewModel: $quizViewModel")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)

        // Replace Toast with Snackbar

        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            disableAnswerButtons()
            checkIfQuizCompleted()
        }

        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            disableAnswerButtons()
            checkIfQuizCompleted()
        }

        binding.nextButton.setOnClickListener{
           quizViewModel.moveToNext()
            updateQuestion()
        }
        binding.prevButton.setOnClickListener{
            quizViewModel.moveToPrev()
            updateQuestion()
        }
        binding.cheatButton.setOnClickListener{
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivity(intent)
            cheatLauncher.launch(intent)
        }
        updateQuestion()

    }
    override fun onStart(){
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume(){
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause(){
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop(){
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy(){
        super.onDestroy()
        Log.d(TAG, "onStart() called")
    }
    private fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        enableAnswerButtons()
    }
    private fun disableAnswerButtons() {
        binding.trueButton.isEnabled = false
        binding.falseButton.isEnabled = false
    }
    private fun enableAnswerButtons() {
        binding.trueButton.isEnabled = true
        binding.falseButton.isEnabled = true
    }
    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgement_toast
            userAnswer == correctAnswer -> {
                correctAnswersCount++
                R.string.correct_toast
            }
                else -> R.string.incorrect_toast
            }

        Snackbar.make(
            binding.root,  // Use the root view of your layout
            messageResId,  // The message to display
            Snackbar.LENGTH_SHORT // Duration of the Snackbar
        ).show()

    }

    private fun checkIfQuizCompleted() {
        if (quizViewModel.ifCompleted) { // Use the computed property from ViewModel
            displayScore()
        }
    }
    private fun displayScore() {
        val scorePercentage = (correctAnswersCount.toDouble() / quizViewModel.getQuestionBank().size) * 100
        val scoreMessage = getString(R.string.quiz_score, scorePercentage.toInt())

        Toast.makeText(this, scoreMessage, Toast.LENGTH_LONG).show()
    }
}

