package com.example.calculator

import kotlin.math.abs

class Calculation {
    private var result = 0.0
    private var prevNumber = ""
    private var currentNumber = "0"
    private var operation = ""
    private var hasError = false

    // Сообщение об ошибке
    companion object {
        val ERROR_MESSAGE = "error"
    }

    // Перевод Double в String для вывода результата
    private fun doubleToString(result: Double) : String {
        // Вывод нуля без минуса
        if (result.toString() == "-0.0")
            return "0"

        // Разделение числа на целую и дробную части
        var strResult = String.format("%.8f", result)
        val resultParts = strResult.split('.')

        // Ошибка вывода слишком длинного числа
        if (resultParts[0].length > 9 || abs(result) > 999999999) {
            hasError = true
            return ERROR_MESSAGE
        }

        // Удаление лишней дробной части
        if (strResult.length > 9) {
            strResult = strResult.removeRange(9..strResult.length - 1)
        }

        // Удаление лишних нулей в дробной части
        strResult = strResult.trimEnd('0')

        // Удаление лишнего разделителя целой и дробной части
        if (strResult[strResult.length - 1] == '.')
            strResult = strResult.removeRange(strResult.length - 1.. strResult.length - 1)

        return strResult
    }

    // Приписывание цифры в конец числа
    fun addDigit(digit: String) : String {
        // Отмена действий при вознокновении ошибки
        if (hasError)
            reset()

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

    // Выбор операции
    fun addOperation(operation: String) : String {
        // Нахождение результата вычисления
        getResult(this.operation)

        // Вывод сообщения об ошибке
        if (hasError)
            return ERROR_MESSAGE

        // Вывод выбранной операции
        this.operation = operation

        // Продолжение ввода следующего числа
        if (currentNumber != "") {
            prevNumber = currentNumber
            currentNumber = ""
        }

       return doubleToString(result) + operation
    }

    // Добавление разделителя целой и дробной части
    fun addComma() : String {
        // Вывод сообщения об ошибке
        if (hasError)
            return ERROR_MESSAGE

        // Добавление разделителя
        if (currentNumber != "" && currentNumber.split('.').count() == 1) {
            currentNumber += '.'
            return currentNumber
        }

        // Пропуск добавления разделителя
        if (operation != "=" && operation != "")
            return doubleToString(result) + operation

        if (currentNumber != "")
            return currentNumber

        return doubleToString(result)
    }

    // Нахождение результата выражения
    private fun getResult(operation: String) {
        if (prevNumber == "") {
            result = currentNumber.toDouble()
            return
        }

        if ((currentNumber == "0" || currentNumber == "0.0") && operation == "÷")
            hasError = true
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
        // Выполнение операции
        getResult(operation)

        // Вывод сообщения об ошибке
        if (hasError)
            return ERROR_MESSAGE

        // Вывод результата
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
        hasError = false

        return currentNumber
    }

    // Смена знака введённого числа или операции
    fun changeSign() : String {
        // Вывод сообщения об ошибке
        if (hasError)
            return ERROR_MESSAGE

        // Смена знака последнего введённого числа
        if (currentNumber != "") {
            currentNumber = doubleToString(currentNumber.toDouble() * (-1))

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
        // Вывод сообщения об ошибке
        if (hasError)
            return ERROR_MESSAGE

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

    // Удаление последнего символа
    fun erase(resultString : String) : String {
        // Отмена действий при возникновении ошибки
        if (hasError) {
            return reset()
        }

        // Удаление последней оставшейся цифры
        if (resultString.length == 1 || (resultString.length == 2 && resultString[0] == '-')) {
            currentNumber = "0"

            if (operation == "=")
                result = 0.0

            return currentNumber
        }

        // Удаление разделителя целой и дробной части числа
        if (resultString[resultString.length - 1] == '.') {
            currentNumber = currentNumber.removeRange(currentNumber.length - 1..currentNumber.length - 1)

            return currentNumber
        }

        // Удаление последней цифры вводимого числа
        if (currentNumber != "" && operation != "=") {
            currentNumber = currentNumber.removeRange(currentNumber.length - 1..currentNumber.length - 1)

            if (currentNumber[currentNumber.length - 1] == '.')
                currentNumber = currentNumber.removeRange(currentNumber.length - 1..currentNumber.length -1)

            if (currentNumber == "-0")
                currentNumber = "0"

            return currentNumber
        }

        // Удаление знака операции
        else if (operation != "=" && operation != "" && !resultString[resultString.length - 1].isDigit()) {
            operation = "="
            return doubleToString(result)
        }

        // Удаление последней цифры выведенного результата
        else if (doubleToString(result) != "0") {
            var strResult = doubleToString(result)
            var newResult = strResult.removeRange(strResult.length - 1..strResult.length - 1)

            if (newResult[newResult.length - 1] == '.')
                 newResult = newResult.removeRange(newResult.length - 1..newResult.length - 1)

            currentNumber = newResult
            result = newResult.toDouble()
        }

        return doubleToString(result)
    }
}