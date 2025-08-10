package com.example.myfirstproject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import net.objecthunter.exp4j.ExpressionBuilder

data class CalculatorState(
    val display: String = "0"
)

sealed class CalculatorAction {
    data class Number(val number: Int) : CalculatorAction()
    object Clear : CalculatorAction()
    object Delete : CalculatorAction()
    object Decimal : CalculatorAction()
    object Calculate : CalculatorAction()
    data class Operation(val operation: CalculatorOperation) : CalculatorAction()
}

sealed class CalculatorOperation(val symbol: String) {
    object Add : CalculatorOperation("+")
    object Subtract : CalculatorOperation("-")
    object Multiply : CalculatorOperation("*")
    object Divide : CalculatorOperation("/")
    data class Parentheses(val value: String) : CalculatorOperation(value)
}

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number.toString())
            is CalculatorAction.Clear -> state = CalculatorState()
            is CalculatorAction.Delete -> delete()
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Calculate -> calculate()
            is CalculatorAction.Operation -> enterOperation(action.operation.symbol)
        }
    }

    private fun enterOperation(symbol: String) {
        state = if (state.display == "0") {
            state.copy(display = symbol)
        } else {
            state.copy(display = state.display + symbol)
        }
    }

    private fun calculate() {
        try {
            val result = ExpressionBuilder(state.display).build().evaluate()
            state = state.copy(
                display = result.toString()
            )
        } catch (_: Exception) {
            state = state.copy(
                display = "Error"
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
        if (!state.display.contains(".")) {
            state = state.copy(
                display = state.display + "."
            )
        }
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