package com.tuncayavci.myquizapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.tuncayavci.myquizapplication.databinding.ActivityQuizQuestionsBinding

class QuizQuestionsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityQuizQuestionsBinding

    private var mCurrentPosition: Int = 1 // Default and the first question position
    private var mUsername: String? = null
    private var mCorrectAnswers: Int = 0
    private var mQuestionsList: ArrayList<Question>? = null

    private var mSelectedOptionPosition: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizQuestionsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mUsername = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()
        // END

        setQuestion()

        binding.tvOptionOne?.setOnClickListener {
            onClick(it)
        }
        binding.tvOptionTwo?.setOnClickListener {
            onClick(it)
        }
        binding.tvOptionThree?.setOnClickListener {
            onClick(it)
        }
        binding.tvOptionFour?.setOnClickListener {
            onClick(it)
        }

        // Adding a click event for submit button.
        binding.btnSubmit.setOnClickListener {
            onClick(it)
        }

    }

    private fun setQuestion() {
        val question: Question =
            mQuestionsList!![mCurrentPosition - 1] //Getting the question from the list with the help of current position
        defaultOptionsView()

        // Check here if the position of question is last then change the text of the button.
        // START
        if (mCurrentPosition == mQuestionsList!!.size) {
            binding.btnSubmit?.text = "FINISH"
        } else {
            binding.btnSubmit?.text = "SUBMIT"
        }
        // END
        binding.progressBar?.progress =
            mCurrentPosition // Setting the current progress in the progressbar using the position of question
        binding.tvProgress?.text =
            "$mCurrentPosition" + "/" + binding.progressBar?.max // Setting up the progress text

        // Now set the current question and the options in the UI
        binding.tvQuestion?.text = question.question
        binding.ivImage?.setImageResource(question.image)
        binding.tvOptionOne?.text = question.optionOne
        binding.tvOptionTwo?.text = question.optionTwo
        binding.tvOptionThree?.text = question.optionThree
        binding.tvOptionFour?.text = question.optionFour
    }

    private fun onClick(view: View) {
        when(view.id){
            R.id.tv_option_one ->{
                selectedOptionView(binding.tvOptionOne,1)
            }
            R.id.tv_option_two -> {
                binding.tvOptionTwo?.let {
                    selectedOptionView(it, 2)
                }

            }

            R.id.tv_option_three -> {
                binding.tvOptionThree?.let {
                    selectedOptionView(it, 3)
                }

            }

            R.id.tv_option_four -> {
                binding.tvOptionFour?.let {
                    selectedOptionView(it, 4)
                }

            }

            // Adding a click event for submit button. And change the questions and check the selected answers.)
            // START
                R.id.btn_submit->{

                    if (mSelectedOptionPosition == 0) {

                        mCurrentPosition++

                        when {

                            mCurrentPosition <= mQuestionsList!!.size -> {

                                setQuestion()
                            }
                            else -> {

                                val intent = Intent(this,ResultActivity::class.java)
                                intent.putExtra(Constants.USER_NAME,mUsername)
                                intent.putExtra(Constants.CORRECT_ANSWER.toString(),mCorrectAnswers)
                                intent.putExtra(Constants.TOTAL_QUESTIONS.toString(),mQuestionsList?.size)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } else {
                        val question = mQuestionsList?.get(mCurrentPosition - 1)

                        // This is to check if the answer is wrong
                        if (question!!.correctAnswer != mSelectedOptionPosition) {
                            answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                        } else {
                            mCorrectAnswers++
                        }

                        // This is for correct answer
                        answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                        if (mCurrentPosition == mQuestionsList!!.size) {
                            binding.btnSubmit?.text = "FINISH"
                        } else {
                            binding.btnSubmit?.text = "GO TO NEXT QUESTION"
                        }

                        mSelectedOptionPosition = 0

                }
            }
        }
    }

    //  Create a function for answer view.
    // START
    /**
     * A function for answer view which is used to highlight the answer is wrong or right.
     */
    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {

            1 -> {
                binding.tvOptionOne?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }
            2 -> {
                binding.tvOptionTwo?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }
            3 -> {
                binding.tvOptionThree?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }
            4 -> {
                binding.tvOptionFour?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }
        }
    }

    private fun selectedOptionView(tv: TextView,selectedOptionNum: Int) {
        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(
            Color.parseColor("#363A43")
        )
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this@QuizQuestionsActivity,
            R.drawable.selected_option_border_bg
        )
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        binding.tvOptionOne?.let {
            options.add(0,it)
        }
        binding.tvOptionTwo?.let {
            options.add(1,it)
        }
        binding.tvOptionThree?.let {
            options.add(2,it)
        }
        binding.tvOptionFour?.let {
            options.add(3,it)
        }

        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background= ContextCompat.getDrawable(
                this@QuizQuestionsActivity,
                R.drawable.default_option_border_bg
            )
        }
    }
}
// END