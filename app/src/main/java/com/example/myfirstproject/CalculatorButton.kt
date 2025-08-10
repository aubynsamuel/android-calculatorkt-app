package com.example.myfirstproject

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary
) {
//    TooltipBox(
//        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
//        tooltip = { PlainTooltip { Text(text) } },
//        state = rememberTooltipState()
//    ) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(2.dp)
            .aspectRatio(1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
    ) {
        Text(
            text = text,
            fontSize = 25.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.wrapContentSize(Alignment.Center)
        )
    }
}
//}