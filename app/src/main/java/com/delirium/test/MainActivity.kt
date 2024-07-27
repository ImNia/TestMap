package com.delirium.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.delirium.test.ui.theme.TestTheme
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

const val API_KEY = "df1bd91c-f27f-48dc-b230-3d75656da74b"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                    MapKitFactory.setApiKey(API_KEY)
                    MapKitFactory.initialize(this)

                    val mapView = remember {
                        mutableStateOf<MapView?>(null)
                    }
                    val cameraPosition = remember {
                        mutableStateOf(
                            CameraPosition(
                                Point(43.2366, 76.8823), //mock
                                13f,
                                0.0f,
                                0.0f
                            )
                        )
                    }

                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize(),
                        factory = { MapView(applicationContext) }
                    ) {
                        mapView.value = it
                        /*mapView.value?.mapWindow?.map?.move(
                            cameraPosition.value,
                            Animation(Animation.Type.LINEAR, 1f),
                            null
                        )*/
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestTheme {
        Greeting("Android")
    }
}