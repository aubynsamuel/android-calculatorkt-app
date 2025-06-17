package com.example.myfirstproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myfirstproject.ui.theme.ScientificCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScientificCalculatorTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Expression Display
        Text(
            text = expression.ifEmpty { "0" },
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Result Display
        if (result.isNotEmpty()) {
            Text(
                text = "Result: $result",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        val buttons = listOf(
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "*"),
            listOf("1", "2", "3", "-"),
            listOf("0", ".", "=", "+"),
            listOf("C", "(", ")", "DEL")
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { label ->
                    Button(
                        onClick = {
                            when (label) {
                                "=" -> {
                                    try {
                                        result = evalExpression(expression)
                                    } catch (e: Exception) {
                                        result = "Error"
                                    }
                                }
                                "C" -> {
                                    expression = ""
                                    result = ""
                                }
                                "DEL" -> {
                                    if (expression.isNotEmpty()) {
                                        expression = expression.dropLast(1)
                                    }
                                }
                                else -> {
                                    expression += label
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                    ) {
                        Text(label)
                    }
                }
            }
        }
    }
}

fun evalExpression(expr: String): String {
    return try {
        val result = ExpressionParser().evaluate(expr)
        result.toString()
    } catch (e: Exception) {
        "Error"
    }
}

class ExpressionParser {
    private var pos = -1
    private var ch = 0
    private lateinit var input: String

    fun evaluate(expression: String): Double {
        input = expression
        pos = -1
        nextChar()
        val result = parseExpression()
        if (pos < input.length) throw RuntimeException("Unexpected: ${ch.toChar()}")
        return result
    }

    private fun nextChar() {
        ch = if (++pos < input.length) input[pos].code else -1
    }

    private fun eat(charToEat: Int): Boolean {
        while (ch == ' '.code) nextChar()
        if (ch == charToEat) {
            nextChar()
            return true
        }
        return false
    }

    private fun parseExpression(): Double {
        var x = parseTerm()
        while (true) {
            x = when {
                eat('+'.code) -> x + parseTerm()
                eat('-'.code) -> x - parseTerm()
                else -> return x
            }
        }
    }

    private fun parseTerm(): Double {
        var x = parseFactor()
        while (true) {
            x = when {
                eat('*'.code) -> x * parseFactor()
                eat('/'.code) -> x / parseFactor()
                else -> return x
            }
        }
    }

    private fun parseFactor(): Double {
        if (eat('+'.code)) return parseFactor()
        if (eat('-'.code)) return -parseFactor()

        var x: Double
        val startPos = pos
        if (eat('('.code)) {
            x = parseExpression()
            eat(')'.code)
        } else if (ch in '0'.code..'9'.code || ch == '.'.code) {
            while (ch in '0'.code..'9'.code || ch == '.'.code) nextChar()
            x = input.substring(startPos, pos).toDouble()
        } else {
            throw RuntimeException("Unexpected: ${ch.toChar()}")
        }

        return x
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    ScientificCalculatorTheme {
        CalculatorScreen()
    }
}
