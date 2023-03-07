package com.example.calculator

import androidx.lifecycle.MutableLiveData
import kotlin.math.abs

class Calculation {
    private var result = 0.0
    private var prevNumber = ""
    private var currentNumber = "0"
    private var operation = ""
    private var hasError: MutableLiveData<Boolean> = MutableLiveData(false)
    private var resultIsPrinted = false
    private var memoryString: MutableLiveData<String> = MutableLiveData("")

    // Сообщение об ошибке
    companion object {
        val ERROR_MESSAGE = "Error"
    }

    // Перевод Double в String для вывода результата
    private fun doubleToString(result: Double) : String {
        // Вывод нуля без минуса
        if (result.toString() == "-0.0" || result.toString() == "-0,0")
            return "0"

        // Разделение числа на целую и дробную части
        var strResult = String.format("%.8f", result)

        val firstSplit = strResult.split('.')[0].trimEnd('0')
        val secondSplit = strResult.split(',')[0].trimEnd('0')

        // Ошибка вывода слишком длинного числа
        if ((firstSplit.length > 10 && secondSplit.length > 10) || abs(result) > 999999999) {
            hasError.value = true
            return ERROR_MESSAGE
        }

        // Удаление лишней дробной части
        if (strResult.length > 9) {
            strResult = strResult.removeRange(9..strResult.length - 1)
        }

        // Удаление лишних нулей в дробной части
        strResult = strResult.trimEnd('0')

        // Удаление лишнего разделителя целой и дробной части
        if (strResult[strResult.length - 1] == '.' || strResult[strResult.length - 1] == ',')
            strResult = strResult.removeRange(strResult.length - 1.. strResult.length - 1)

        return strResult
    }

    // Приписывание цифры в конец числа
    fun addDigit(digit: String) : String {
        // Стререть строку с сохранённым вводом при введении нового выражения
        if (resultIsPrinted) {
            memoryString.value = ""
            resultIsPrinted = false
        }

        // Отмена действий при вознокновении ошибки
        if (hasError.value!!)
            reset()

        // Удаление незначащих нулей
        if (currentNumber == "0")
            currentNumber = ""

        // Ввод числа до девяти знаков
        if (currentNumber.length < 9)
            currentNumber += digit

        if (prevNumber == "" || operation == "=") {
            currentNumber = currentNumber.replace(',', '.')
            result = currentNumber.toDouble()
        }

        return currentNumber
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

        // Обновление строки, запоминающей ввод
        if (doubleToString(result) != ERROR_MESSAGE && !hasError.value!!) {
            memoryString.value = "${doubleToString(result)} ${operation}"
            resultIsPrinted = false

            return doubleToString(result) + operation
        }

        // Вывод сообщения об ошибке
        return ERROR_MESSAGE
    }

    // Добавление разделителя целой и дробной части
    fun addComma() : String {
        // Обновление строки, запоминающей ввод
        if (resultIsPrinted && !hasError.value!!) {
            memoryString.value = ""
            resultIsPrinted = false
        }

        // Вывод сообщения об ошибке
        if (hasError.value!!)
            return ERROR_MESSAGE

        // Добавление разделителя
        if (currentNumber != "" && currentNumber.split('.').count() == 1 && currentNumber.split(',').count() == 1) {
            currentNumber += '.'
            return currentNumber
        }

        // Пропуск добавления разделителя
        if (currentNumber != "")
            return currentNumber

        if (operation != "=" && operation != "")
            return doubleToString(result) + operation

        return doubleToString(result)
    }

    // Нахождение результата выражения
    private fun getResult(operation: String) {
        if (prevNumber == "") {
            currentNumber = currentNumber.replace(',', '.')
            result = currentNumber.toDouble()
            return
        }

        if (currentNumber != "" && (doubleToString(currentNumber.replace(',', '.').toDouble()) == "0") && operation == "÷")
            hasError.value = true
        else if (currentNumber != "" && operation != "=") {
            currentNumber = currentNumber.replace(',', '.')

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
        // Обновление строки, запоминающей ввод
        if (operation != "=" && operation != "" && currentNumber != "") {
            memoryString.value = doubleToString(result) + " " + operation + " " + currentNumber + " = "
            resultIsPrinted = true
        }
        else if (!resultIsPrinted) {
            memoryString.value = doubleToString(result) + " ="
            resultIsPrinted = true
        }

        // Выполнение операции
        getResult(operation)

        // Вывод сообщения об ошибке
        if (hasError.value!!)
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
        hasError.value = false
        memoryString.value = ""

        return currentNumber
    }

    // Смена знака введённого числа или операции
    fun changeSign() : String {
        // Вывод сообщения об ошибке
        if (hasError.value!!)
            return ERROR_MESSAGE

        // Обновление строки, запоминающей ввод
        if (resultIsPrinted && result != 0.0) {
            memoryString.value = ""
            resultIsPrinted = false
        }

        // Смена знака последнего введённого числа
        if (currentNumber != "") {
            currentNumber = currentNumber.replace(',', '.')
            currentNumber = doubleToString(currentNumber.toDouble() * (-1))

            if (operation == "=" || operation == "")
                result *= -1

            return currentNumber
        }

        // Смена знака последней введённой операции
        if (operation == "-") {
            operation = "+"
            memoryString.value = doubleToString(result) + " " + operation

            return doubleToString(result) + operation
        }

        if (operation == "+") {
            operation = "-"
            memoryString.value = doubleToString(result) + " " + operation

            return doubleToString(result) + operation
        }

        // Пропуск нажатия кнопки
        if (operation != "=")
            return doubleToString(result) + operation

        return doubleToString(result)
    }

    // Взятие процента от числа
    fun getPercent() : String {
        // Вывод сообщения об ошибке
        if (hasError.value!!)
            return ERROR_MESSAGE

        // Пропуск взятия процента, если не было введено предыдущее или текущее число
        if (prevNumber == "" || currentNumber == "") {
            if (operation != "=")
                return doubleToString(result) + operation

            return doubleToString(result)
        }

        // Умножение или деление результата на процент
        currentNumber = currentNumber.replace(',', '.')
        currentNumber = doubleToString(currentNumber.toDouble() * 0.01)

        // Сложение или вычитание результата и процента
        if (operation == "-" || operation == "+") {
            currentNumber = currentNumber.replace(',', '.')
            currentNumber = doubleToString(currentNumber.toDouble() * result)
        }

        // Вывод результата
        return showResult()
    }

    // Удаление последнего символа
    fun erase(resultString : String) : String {
        // Обновление строки, запоминающей ввод
        if (resultIsPrinted) {
            memoryString.value = ""
            resultIsPrinted = false
        }

        // Отмена действий при возникновении ошибки
        if (hasError.value!!) {
            return reset()
        }

        // Удаление последней оставшейся цифры
        if (resultString.length == 1 || (resultString.length == 2 && resultString[0] == '-')) {
            currentNumber = "0"

            if (operation == "=" || operation == "")
                result = 0.0

            return currentNumber
        }

        // Удаление разделителя целой и дробной части числа
        if (resultString[resultString.length - 1] == '.' || resultString[resultString.length - 1] == ',') {
            currentNumber = currentNumber.removeRange(currentNumber.length - 1..currentNumber.length - 1)

            return currentNumber
        }

        // Удаление последней цифры вводимого числа
        if (currentNumber != "" && operation != "=") {
            currentNumber = currentNumber.removeRange(currentNumber.length - 1..currentNumber.length - 1)

            if (currentNumber[currentNumber.length - 1] == '.' || currentNumber[currentNumber.length - 1] == ',')
                currentNumber = currentNumber.removeRange(currentNumber.length - 1..currentNumber.length -1)

            if (currentNumber == "-0")
                currentNumber = "0"

            currentNumber = currentNumber.replace(',', '.')
            result = currentNumber.toDouble()

            return currentNumber
        }

        // Удаление знака операции
        else if (operation != "=" && operation != "" && !resultString[resultString.length - 1].isDigit()) {
            operation = "="
            currentNumber = doubleToString(result)

            // Обновление строки, запоминающей ввод
            memoryString.value = ""
            resultIsPrinted = false

            return doubleToString(result)
        }

        // Удаление последней цифры выведенного результата
        else if (doubleToString(result) != "0") {
            var strResult = doubleToString(result)
            var newResult = strResult.removeRange(strResult.length - 1..strResult.length - 1)

            if (newResult[newResult.length - 1] == '.' || newResult[newResult.length - 1] == ',')
                 newResult = newResult.removeRange(newResult.length - 1..newResult.length - 1)

            currentNumber = newResult
            newResult = newResult.replace(',', '.')
            result = newResult.toDouble()
        }

        return doubleToString(result)
    }

    // Возврат строки, запоминающей ввод
    fun getMemoryStr() : MutableLiveData<String> {
        return memoryString
    }

    // Возврат ошибки
    fun getError() : MutableLiveData<Boolean> {
        return hasError
    }
}