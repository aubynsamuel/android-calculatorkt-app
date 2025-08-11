package com.example.myfirstproject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import net.objecthunter.exp4j.ExpressionBuilder

data class CalculatorState(
    val display: String = "0",
)

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set
    var preCalcValue by mutableStateOf(CalculatorState(display = ""))
        private set
    var stateParts by mutableStateOf(emptyList<String>())

    fun onButtonClick(buttonValue: String) {
        when (buttonValue) {
            "C" -> clear()
            "=" -> calculate()
            "." -> enterDecimal()
            "DELETE" -> delete() // Special case for delete button
            in "0".."9" -> enterNumber(buttonValue)
            in "+-*/()" -> enterOperation(buttonValue)
        }
        checkPreCalculation()
    }

    private fun clear() {
        state = CalculatorState()
        preCalcValue = CalculatorState(display = "")
    }

    private fun enterOperation(symbol: String) {
        state = if (state.display.last() in "+-*/()") {
            state.copy(
                display = state.display.dropLast(1) + symbol
            )
        } else if (state.display == "0") {
            state.copy(display = symbol)
        } else {
            state.copy(display = state.display + symbol)
        }
    }

    private fun calculate() {
        try {
            val result = ExpressionBuilder(state.display).build().evaluate()
            state = state.copy(
                display = if (result.toString().endsWith(".0")) {
                    result.toInt().toString() // Convert to Int if no decimal part
                } else {
                    result.toString() // Keep as String if it has a decimal part
                }
            )
        } catch (_: Exception) {
            state = state.copy(
                display = "Error"
            )
        }
    }

    private fun checkPreCalculation() {
        val operators = listOf("+", "-", "*", "/", "(", ")")
        val parts = state.display.split(*operators.toTypedArray())
        stateParts = parts

        if (parts.count() >= 2 && parts.last().isNotBlank()) {
            preCalculate()
        } else {
            preCalcValue = preCalcValue.copy(
                display = ""
            )
        }
    }

    private fun preCalculate() {
        try {
            val result = ExpressionBuilder(state.display).build().evaluate()
            preCalcValue = preCalcValue.copy(
                display = if (result.toString().endsWith(".0")) {
                    result.toInt().toString() // Convert to Int if no decimal part
                } else {
                    result.toString() // Keep as String if it has a decimal part
                }
            )
        } catch (_: Exception) {
            preCalcValue = preCalcValue.copy(
                display = ""
            )
        }
    }

    private fun delete() {
        state = if (state.display.length > 1) {
            state.copy(
                display = state.display.dropLast(1)
            )
        } else {
            CalculatorState()
        }
    }

    private fun enterDecimal() {
        if (!stateParts.last().contains("."))
            state = state.copy(
                display = state.display + "."
            )
    }

    private fun enterNumber(number: String) {
        state = if (state.display == "0") {
            state.copy(display = number)
        } else {
            state.copy(
                display = state.display + number
            )
        }
    }
}