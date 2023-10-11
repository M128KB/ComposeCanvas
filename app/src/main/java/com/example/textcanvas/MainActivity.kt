package com.example.textcanvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.textcanvas.ui.theme.TextCanvasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextCanvasTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DrawingScreen()
                }
            }
        }
    }
}


@Composable
fun DrawingScreen() {
    val lines = remember {
        mutableStateListOf<Line>()
    }

    val palette = listOf(
        Color(0xFFF44336),
        Color(0xFFFFEB3B),
        Color(0xFF4CAF50),
        Color(0xFF009688),
        Color(0xFF03A9F4),
        Color(0xFF29B6F6),
        Color(0xFF81C784),
        Color(0xFF000000),
        Color(0xFF607D8B),
        Color(0xFFBF360C),
        Color(0xFFE65100),
    )

    val myColor = remember {
        mutableStateOf(Color(0xFF000000))
    }
    var sliderPosition by remember { mutableStateOf(0f) }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = { lines.clear() }) {
            Text(text = "DELETE")
        }

        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 1f..10f
        )
        Row(
            Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            LazyRow() {
                items(palette.size) {
                    Button(
                        onClick = { myColor.value = palette[it] },
                        Modifier
                            .padding(4.dp)
                            .aspectRatio(1f / 1f)
                            .size(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = palette[it],
                        )
                    ) {

                    }
                }
            }
        }



        Canvas(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .border(border = BorderStroke(4.dp, Color.Black))
                .clipToBounds()
                .pointerInput(true) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()

                        val line = Line(
                            start = change.position - dragAmount,
                            end = change.position,
                            color = myColor.value,
                            strokeWidth = sliderPosition.toDp()
                        )
                        lines.add(line)
                    }
                }
        ) {
            lines.forEach { line ->
                drawLine(
                    color = line.color,
                    start = line.start,
                    end = line.end,
                    strokeWidth = line.strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }


    }


}

data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color,
    val strokeWidth: Dp
)

