package com.bignerdranch.android.geoquiz
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
class QuizViewModel (private val savedStateHandle: SavedStateHandle): ViewModel() {
    init{
        Log.d(TAG, "ViewModel instance created")
    }
    override fun onCleared(){
        super.onCleared()
        Log.d(TAG, "VIewModel instance about to be destroyed")
    }
    private val questionBank = listOf(
        Question(R.string.question_text, true),
        Question(R.string.question_sisters, false),
        Question(R.string.question_district, true),
        Question(R.string.question_panem, false),
        Question(R.string.question_thresh, true),
        Question(R.string.question_katniss, true),
        Question(R.string.question_haymitch, false)

    )
    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY)?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?:0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    private val answeredQuestions = BooleanArray(questionBank.size)
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val ifCompleted: Boolean
        get() = (currentIndex == questionBank.size - 1)

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId


    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToPrev(){
        currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
    }

    fun disableAnswer() {
        answeredQuestions[currentIndex] = true
    }

    fun isAnswerDisabled(): Boolean {
        return answeredQuestions[currentIndex]
    }
    fun getQuestionBank() = questionBank

}