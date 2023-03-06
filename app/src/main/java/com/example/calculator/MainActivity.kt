package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
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

        // Нажатие на кнопки 0-9
        listOf(binding.button0, binding.button1, binding.button2, binding.button3, binding.button4,
            binding.button5, binding.button6, binding.button7, binding.button8, binding.button9).forEachIndexed {
            index, element -> element.setOnClickListener(object: View.OnClickListener {

                // Добавление введённой цифры
                override fun onClick(v: View?) {
                    binding.result.text = calculation.addDigit(index.toString())
                    binding.memory.text = calculation.getMemoryStr()

                    // Отмена цвета ошибки
                    binding.result.setTextColor(getResources().getColor(R.color.resultColor))
                }
            })
        }

        // Нажатие на кнопки +, -, *, /
        val operation = mapOf(binding.minusButton to "-", binding.plusButton to "+", binding.multiplyButton to "×", binding.divideButton to "÷")

        listOf(binding.minusButton, binding.plusButton, binding.multiplyButton, binding.divideButton).forEach {
            it.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    // Добавление введённой операции
                    binding.result.text = calculation.addOperation(operation.getValue(it))
                    binding.memory.text = calculation.getMemoryStr()

                    // Установка цвета ошибки
                    if (binding.result.text.toString() == Calculation.ERROR_MESSAGE) {
                        binding.result.setTextColor(getResources().getColor(R.color.errorColor))
                    }
                }
            })
        }

        // Нажатие на кнопку равно
        binding.equalsButton.setOnClickListener {
            binding.result.text = calculation.showResult()
            binding.memory.text = calculation.getMemoryStr()

            // Установка цвета ошибки
            if (binding.result.text.toString() == Calculation.ERROR_MESSAGE) {
                binding.result.setTextColor(getResources().getColor(R.color.errorColor))
            }
        }

        // Нажатие на кнопку AC
        binding.resetButton.setOnClickListener {
            binding.result.text = calculation.reset()
            binding.memory.text = calculation.getMemoryStr()

            // Отмена цвета ошибки
            binding.result.setTextColor(getResources().getColor(R.color.resultColor))
        }

        // Нажатие на кнопку плюс-минус
        binding.plusMinusButton.setOnClickListener {
            binding.result.text = calculation.changeSign()
            binding.memory.text = calculation.getMemoryStr()
        }

        // Нажатие на кнопку процента
        binding.percentButton.setOnClickListener {
            binding.result.text = calculation.getPercent()
            binding.memory.text = calculation.getMemoryStr()

            // Установка цвета ошибки
            if (binding.result.text.toString() == Calculation.ERROR_MESSAGE) {
                binding.result.setTextColor(getResources().getColor(R.color.errorColor))
            }
        }

        // Нажатие на кнопку удаления последнего символа
        binding.eraseButton.setOnClickListener {
            binding.result.text = calculation.erase(binding.result.text.toString())
            binding.memory.text = calculation.getMemoryStr()

            // Отмена цвета ошибки
            binding.result.setTextColor(getResources().getColor(R.color.resultColor))
        }

        // Нажатие на кнопку разделения целой и дробной части числа
        binding.commaButton.setOnClickListener {
            binding.result.text = calculation.addComma()
            binding.memory.text = calculation.getMemoryStr()
        }
    }
}