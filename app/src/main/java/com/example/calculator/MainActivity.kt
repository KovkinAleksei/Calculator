package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var calculation = Calculation()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Добавление ViewBinding в activity_main
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Нажатие на кнопку 0
        binding.button0.setOnClickListener{
            val digit = binding.button0.text.toString()
            binding.result.text = calculation.addDigit(digit)
        }

        // Нажатие на кнопку 1
        binding.button1.setOnClickListener{
            val digit = binding.button1.text.toString()
            binding.result.text = calculation.addDigit(digit)
        }

        // Нажатие на кнопку 2
        binding.button2.setOnClickListener{
            val digit = binding.button2.text.toString()
            binding.result.text = calculation.addDigit(digit)
        }

        // Нажатие на кнопку 3
        binding.button3.setOnClickListener{
            val digit = binding.button3.text.toString()
            binding.result.text = calculation.addDigit(digit)
        }

        // Нажатие на кнопку 4
        binding.button4.setOnClickListener{
            val digit = binding.button4.text.toString()
            binding.result.text = calculation.addDigit(digit)
        }

        // Нажатие на кнопку 5
        binding.button5.setOnClickListener{
            val digit = binding.button5.text.toString()
            binding.result.text = calculation.addDigit(digit)
        }

        // Нажатие на кнопку 6
        binding.button6.setOnClickListener{
            val digit = binding.button6.text.toString()
            binding.result.text = calculation.addDigit(digit)
        }

        // Нажатие на кнопку 7
        binding.button7.setOnClickListener{
            val digit = binding.button7.text.toString()
            binding.result.text = calculation.addDigit(digit)
        }

        // Нажатие на кнопку 8
        binding.button8.setOnClickListener{
            val digit = binding.button8.text.toString()
            binding.result.text = calculation.addDigit(digit)
        }

        // Нажатие на кнопку 9
        binding.button9.setOnClickListener{
            val digit = binding.button9.text.toString()
            binding.result.text = calculation.addDigit(digit)
        }

        // Нажатие на кнопку сложения
        binding.plusButton.setOnClickListener {
            val sign = binding.plusButton.text.toString()
            binding.result.text = calculation.addOperation(sign)
        }

        // Нажатие на кнопку вычитания
        binding.minusButton.setOnClickListener {
            val sign = binding.minusButton.text.toString()
            binding.result.text = calculation.addOperation(sign)
        }

        // Нажатие на кнопку умножения
        binding.multiplyButton.setOnClickListener {
            val sign = binding.multiplyButton.text.toString()
            binding.result.text = calculation.addOperation(sign)
        }

        // Нажатие на кнопку деления
        binding.divideButton.setOnClickListener {
            val sign = binding.divideButton.text.toString()
            binding.result.text = calculation.addOperation(sign)
        }

        // Нажатие на кнопку равно
        binding.equalsButton.setOnClickListener {
            binding.result.text = calculation.showResult()
        }

        // Нажатие на кнопку AC
        binding.resetButton.setOnClickListener {
            binding.result.text = calculation.reset()
        }

        // Нажатие на кнопку плюс-минус
        binding.plusMinusButton.setOnClickListener {
            binding.result.text = calculation.changeSign()
        }

        // Нажатие на кнопку процента
        binding.percentButton.setOnClickListener {
            binding.result.text = calculation.getPercent()
        }

        // Нажатие на кнопку удаления последнего символа
        binding.eraseButton.setOnClickListener {
            binding.result.text = calculation.erase(binding.result.text.toString())
        }

        // Нажатие на кнопку разделения целой и дробной части числа
        binding.commaButton.setOnClickListener {
            binding.result.text = calculation.addComma()
        }
    }
}