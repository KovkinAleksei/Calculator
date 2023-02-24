package com.example.calculator

class Calculation {
    private var result = 0.0
    private var prevNumber = ""
    private var currentNumber = "0"
    private var operation = ""

    // Приписывание цифры в конец числа
    fun addDigit(digit: String) : String {
        // Удаление незначащих нулей
        if (currentNumber == "0")
            currentNumber = ""

        // Ввод числа до девяти знаков
        if (currentNumber.length < 9)
            currentNumber += digit

        if (prevNumber == "" || operation == "=")
            result = currentNumber.toDouble()

        return currentNumber
    }

    // Перевод Double в String для вывода результата
    private fun doubleToString(result: Double) : String {
        // Вывод нуля без минуса
        if (result.toString() == "-0.0")
            return "0"

        // Разделение числа на целую и дробную части
        val strResult = result.toString()
        var resultParts = strResult.split('.')

        // Ошибка вывода слишком длинного числа
        if (resultParts[0].length > 9)
            return "error"

        // Вывод числа без лишней дробной части
        if (resultParts[1] == "0")
            return resultParts[0]

        // Вывод числа с дробной частью, укороченной до длины числа в 9 символов
        if (strResult[8] != ',')
            return strResult.removeRange(9..strResult.length - 1)

        return strResult.removeRange(8..strResult.length - 1)
    }

    // Выбор операции
    fun addOperation(operation: String) : String {
        // Нахождение результата вычисления
        getResult(this.operation)

        // Вывод выбранной операции
        this.operation = operation

        // Продолжение ввода следующего числа
        if (currentNumber != "") {
            prevNumber = currentNumber
            currentNumber = ""
        }

       return doubleToString(result) + operation
    }

    // Нахождение результата выражения
    private fun getResult(operation: String) {
        if (prevNumber == "") {
            result = currentNumber.toDouble()
            return
        }

        if (currentNumber == "0" && operation == "÷")
            result = 0.0
        else if (currentNumber != "" && operation != "=") {
            result = when (operation) {
                "+" -> result + currentNumber.toDouble()
                "-" -> result - currentNumber.toDouble()
                "×" -> result * currentNumber.toDouble()
                "÷" -> result / currentNumber.toDouble()
                else -> 0.0
            }
        }
    }

    // Вывод результата
    fun showResult() : String {
        getResult(operation)
        operation = "="

        currentNumber=doubleToString(result)

        return doubleToString(result)
    }

    // Отмена действий
    fun reset() : String {
        result = 0.0
        prevNumber = ""
        currentNumber = "0"
        operation = ""

        return currentNumber
    }

    // Смена знака введённого числа или операции
    fun changeSign() : String {
        // Смена знака последнего введённого числа
        if (currentNumber != "") {
            currentNumber = (currentNumber.toInt() * (-1)).toString()

            if (operation == "=")
                result *= -1

            return currentNumber
        }

        // Смена знака последней введённой операции
        if (operation == "-") {
            operation = "+"

            return doubleToString(result) + operation
        }

        if (operation == "+") {
            operation = "-"

            return doubleToString(result) + operation
        }

        // Пропуск нажатия кнопки
        return doubleToString(result) + operation
    }

    // Взятие процента от числа
    fun getPercent() : String {
        // Пропуск взятия процента, если не было введено предыдущее или текущее число
        if (prevNumber == "" || currentNumber == "") {
            if (operation != "=")
                return doubleToString(result) + operation

            return doubleToString(result)
        }

        // Умножение или деление результата на процент
        currentNumber = (currentNumber.toDouble() * 0.01).toString()

        // Сложение или вычитание результата и процента
        if (operation == "-" || operation == "+")
            currentNumber = (currentNumber.toDouble() * result).toString()

        // Вывод результата
        return showResult()
    }
}