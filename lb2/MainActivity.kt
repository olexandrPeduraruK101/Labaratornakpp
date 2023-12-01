package com.company.englishwordsapp

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    // Виділення пам'яті для компонентів UI
    private lateinit var resultTextView: TextView
    private lateinit var timerText: TextView
    private lateinit var colorNameText: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    // Ініціалізація змінних для гри
    private var correctAnswers = 0
    private var incorrectAnswers = 0
    private val colors = ArrayList<String>()
    private val colorNames = ArrayList<String>()
    private val random = Random()
    private lateinit var timer: CountDownTimer
    private var timeLeftMillis = 180000L // Початкова тривалість таймера (180 секунд)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Зв'язування з компонентами UI з розмітки
        resultTextView = findViewById(R.id.resultTextView)
        timerText = findViewById(R.id.timerText)
        colorNameText = findViewById(R.id.colorNameText)
        trueButton = findViewById(R.id.trueButton)
        falseButton = findViewById(R.id.falseButton)

        // Додавання кольорів та їх назв до списків
        colors.add("Red")
        colors.add("Green")
        colors.add("Blue")
        colors.add("Yellow")
        colors.add("Purple")

        // Змішування назв кольорів
        colorNames.addAll(colors)
        colorNames.shuffle()

        // Запуск гри
        startGame()
    }

    private fun startGame() {
        // Оновлення першого питання та запуск таймера
        updateQuestion()
        startTimer()
        // ховаємо кнопку
        findViewById<Button>(R.id.restartButton).visibility = View.GONE
        // Очистка тексту результату
        resultTextView.text = ""
    }

    private fun updateQuestion() {
        // Оновлення питання (вибір випадкового кольору та його назви)
        val randomColorIndex = random.nextInt(colors.size)
        val randomColorNameIndex = random.nextInt(colorNames.size)

        // Встановлення тексту та кольору для питання
        val color = Color.parseColor(colors[randomColorIndex])
        colorNameText.text = colorNames[randomColorNameIndex]
        colorNameText.setTextColor(color)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Оновлення залишкового часу та тексту таймера
                timeLeftMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                // Виклик функції при закінченні часу
                showResult()
            }
        }.start()
    }


    private fun updateTimerText() {
        // Оновлення тексту таймера в секундах
        val seconds = (timeLeftMillis / 1000).toInt()
        timerText.text = seconds.toString()
    }

    fun checkAnswer(view: View) {
        // Перевірка відповіді гравця
        val selectedButton = view as Button
        val answer = when (selectedButton.id) {
            R.id.trueButton -> true
            R.id.falseButton -> false
            else -> false
        }

        if (isAnswerCorrect() == answer) {
            // Якщо відповідь вірна, збільшити лічильник правильних відповідей
            correctAnswers++
        } else {
            // Якщо відповідь невірна, збільшити лічильник неправильних відповідей
            incorrectAnswers++
        }

        // Оновлення питання після відповіді та продовження гри
        updateQuestion()
    }

    private fun isAnswerCorrect(): Boolean {
        // Перевірка, чи вірна відповідь гравця
        val textColor = colorNameText.currentTextColor
        val color = Color.parseColor(colors[colors.indexOf(colorNames[0])])

        return textColor == color
    }

    private fun showResult() {
        val resultMessage = "Гра закінчена. Правильних відповідей: $correctAnswers, Неправильних відповідей: $incorrectAnswers"
        resultTextView.text = resultMessage
        // Показати кнопку "Почати знову" після завершення гри
        findViewById<Button>(R.id.restartButton).visibility = View.VISIBLE
    }

    // Функція для перезапуску гри
    fun restartGame(view: View) {
        // Очищення лічильників
        correctAnswers = 0
        incorrectAnswers = 0
        // Сховати кнопку "Почати знову"
        findViewById<Button>(R.id.restartButton).visibility = View.GONE
        //Встановлення часу
        timeLeftMillis = 180000L
        // Почати нову гру
        startGame()
    }

}