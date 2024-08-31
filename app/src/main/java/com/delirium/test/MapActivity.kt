package com.delirium.test

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import com.delirium.test.ui.theme.TestTheme
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val lifecycleOwner = LocalLifecycleOwner.current
                    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

                    val mapView = remember {
                        mutableStateOf<MapView?>(null)
                    }
                    val markersList = remember {
                        mutableListOf<PlacemarkMapObject>()
                    }
                    val cameraPosition = remember {
                        mutableStateOf(
                            CameraPosition(
                                Point(coordinates.first().latitude, coordinates.first().longitude),
                                13f,
                                0.0f,
                                0.0f
                            )
                        )
                    }
                    Column {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            onClick = {
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                            },
                            content = {
                                Text(text = "Back")
                            }
                        )
                        AndroidView(
                            modifier = Modifier
                                .fillMaxSize(),
                            factory = { MapView(applicationContext) }
                        ) {
                            mapView.value = it
                            mapView.value?.mapWindow?.map?.move(
                                cameraPosition.value,
                                Animation(Animation.Type.LINEAR, 1f),
                                null
                            )

                            coordinates.forEach { marker ->
                                mapView.value?.mapWindow?.map?.mapObjects?.addPlacemark { placemark ->
                                    placemark.geometry = Point(marker.latitude, marker.longitude)
                                    placemark.setIcon(
                                        ImageProvider.fromBitmap(
                                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_marker)?.toBitmap()
                                        )
                                    )
                                    placemark.addTapListener { _, _ ->
                                        Toast.makeText(applicationContext, "Click on marker", Toast.LENGTH_SHORT).show()
                                        true
                                    }
                                    markersList.add(placemark)
                                }
                            }
                        }
                    }
                    LaunchedEffect(lifecycleState) {
                        when (lifecycleState) {
                            Lifecycle.State.STARTED -> {
                                MapKitFactory.getInstance().onStart()
                                mapView.value?.onStart()
                            }

                            Lifecycle.State.DESTROYED -> {
                                mapView.value?.onStop()
                                MapKitFactory.getInstance().onStop()
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private val coordinates = listOf(
        Coordinates(54.888970, 90.855990),
        Coordinates(54.984430, 90.849119),
        Coordinates(54.960784, 91.114337),
        Coordinates(55.090669, 90.958884),
        Coordinates(55.045060, 90.704660),
        Coordinates(54.883954, 90.986692),
        Coordinates(54.917119, 90.601919),
    )

    data class Coordinates(
        val latitude: Double,
        val longitude: Double
    )
}