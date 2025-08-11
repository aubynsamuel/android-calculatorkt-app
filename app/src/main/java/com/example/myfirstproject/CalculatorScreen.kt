package com.example.myfirstproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfirstproject.ui.theme.ScientificCalculatorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen() {
    val viewModel: CalculatorViewModel = viewModel()
    val state = viewModel.state
    val preCalcValue = viewModel.preCalcValue

    val buttons = listOf(
        "C", "(", ")", "/",
        "7", "8", "9", "*",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "", "0", ".", "="
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Calculator",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Display
            Card(
                modifier = Modifier
                    .fillMaxHeight(0.25f),
            ) {
                Text(
                    text = state.display,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .weight(1f),
                    style = MaterialTheme.typography.displayMedium.copy(
                        textAlign = TextAlign.End
                    ),
                    maxLines = 2
                )

                Text(
                    preCalcValue.display,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .padding(end = 15.dp),
                    fontSize = 18.sp, maxLines = 1
                )
            }

            IconButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { viewModel.onAction(CalculatorAction.Delete) }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            HorizontalDivider()

            // Buttons
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                items(buttons) { buttonValue ->
                    val backgroundColor = when (buttonValue) {
                        "C" -> MaterialTheme.colorScheme.error
                        "=" -> MaterialTheme.colorScheme.primary
                        in "+-*/()" -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.secondary
                    }

                    val textColor = when (buttonValue) {
                        "C" -> MaterialTheme.colorScheme.onError
                        "=" -> MaterialTheme.colorScheme.onPrimary
                        in "+-*/()" -> MaterialTheme.colorScheme.onPrimary
                        else -> MaterialTheme.colorScheme.onSecondary
                    }

                    CalculatorButton(
                        text = buttonValue,
                        onClick = {
                            when (buttonValue) {
                                "C" -> viewModel.onAction(CalculatorAction.Clear)
                                "=" -> viewModel.onAction(CalculatorAction.Calculate)
                                "." -> viewModel.onAction(CalculatorAction.Decimal)
                                in "0".."9" -> viewModel.onAction(
                                    CalculatorAction.Number(
                                        buttonValue.toInt()
                                    )
                                )

                                in "+-*/()" -> viewModel.onAction(
                                    CalculatorAction.Operation(
                                        when (buttonValue) {
                                            "+" -> CalculatorOperation.Add
                                            "-" -> CalculatorOperation.Subtract
                                            "*" -> CalculatorOperation.Multiply
                                            "/" -> CalculatorOperation.Divide
                                            "(" -> CalculatorOperation.Parentheses("(")
                                            ")" -> CalculatorOperation.Parentheses(")")
                                            else -> return@CalculatorButton
                                        }
                                    )
                                )
                            }
                        },
                        backgroundColor = backgroundColor,
                        textColor = textColor,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    ScientificCalculatorTheme {
        CalculatorScreen()
    }
}