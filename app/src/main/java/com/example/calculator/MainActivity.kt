package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
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

        // Изменение строки, запоминающей ввод
        calculation.getMemoryStr().observe(this, Observer {
            binding.memory.text = calculation.getMemoryStr().value
        })

        // Изменение цвета ответа при возникновении ошибки
        calculation.getError().observe(this, Observer {
            if (calculation.getError().value!!)
                binding.result.setTextColor(getResources().getColor(R.color.errorColor))
            else
                binding.result.setTextColor(getResources().getColor(R.color.resultColor))
        })

        // Нажатие на кнопки 0-9
        listOf(binding.button0, binding.button1, binding.button2, binding.button3, binding.button4,
            binding.button5, binding.button6, binding.button7, binding.button8, binding.button9).forEachIndexed {
            index, element -> element.setOnClickListener(object: View.OnClickListener {

                // Добавление введённой цифры
                override fun onClick(v: View?) {
                    binding.result.text = calculation.addDigit(index.toString())
                }
            })
        }

        // Нажатие на кнопки +, -, *, /
        val operation = mapOf(binding.minusButton to "-", binding.plusButton to "+", binding.multiplyButton to "×", binding.divideButton to "÷")

        listOf(binding.minusButton, binding.plusButton, binding.multiplyButton, binding.divideButton).forEach {
            it.setOnClickListener(object: View.OnClickListener {

                // Добавление введённой операции
                override fun onClick(v: View?) {
                    binding.result.text = calculation.addOperation(operation.getValue(it))
                }
            })
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