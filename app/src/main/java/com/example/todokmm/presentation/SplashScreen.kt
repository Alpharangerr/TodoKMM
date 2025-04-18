package com.example.todokmm.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.todokmm.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    // Remove the audio and play the splash screen without sound

    LaunchedEffect(Unit) {
        delay(3000)  // time interval for splash screen
        onSplashFinished()  // Trigger the completion callback
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.splashhehe),
            contentDescription = "Splash Screen Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Loading spinner centered over the image
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = androidx.compose.ui.graphics.Color.White
        )
    }
}
