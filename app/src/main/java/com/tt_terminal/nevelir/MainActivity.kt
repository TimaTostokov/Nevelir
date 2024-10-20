package com.tt_terminal.nevelir

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tt_terminal.nevelir.databinding.ActivityMainBinding
import kotlin.math.abs

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        Thread.sleep(1500)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        binding.calculateButton.setOnClickListener {
            calculate()
        }
    }

    private fun String.toNumericDouble(): Double? = this.replace(',', '.').toDoubleOrNull()

    @SuppressLint("SetTextI18n")
    private fun calculate() {
        val repereMark = binding.repereEditText.text.toString().toNumericDouble() ?: 0.0
        val railRepere = binding.railRepereEditText.text.toString().toNumericDouble() ?: 0.0
        val constructionRail =
            binding.constructionRailEditText.text.toString().toNumericDouble() ?: 0.0
        val projectMark = binding.projectMarkEditText.text.toString().toNumericDouble() ?: 0.0

        val horizon = repereMark + railRepere / 1000
        val constructionMark = horizon - constructionRail / 1000
        val difference = projectMark - constructionMark

        binding.horizonTextView.text = "Горизонт инструмента, м: ${formatOutput(horizon)}"
        binding.constructionMarkTextView.text =
            "Отметка конструкции фактическая, м: ${formatOutput(constructionMark)}"
        binding.differenceTextView.text = if (difference < 0) {
            "Опускать: ${formatOutput(abs(difference))} м"
        } else {
            "Поднимать: ${formatOutput(abs(difference))} м"
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatOutput(value: Double): String {
        val formatted = String.format("%.4f", value)
        return formatted.trimEnd('0').trimEnd('.')
    }

}