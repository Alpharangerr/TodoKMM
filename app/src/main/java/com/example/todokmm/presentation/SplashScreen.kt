package com.example.todokmm.presentation

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.todokmm.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    // Get the context using LocalContext
    val context = LocalContext.current

    // Create a MediaPlayer instance to play audio
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.splash_audio) }

    // Play the audio when the composable is first launched
    LaunchedEffect(Unit) {
        mediaPlayer.start()  // Start playing the audio
        delay(4000)  // Keep the splash screen for 2 seconds
        mediaPlayer.release()  // Release the media player resources after the audio is finished
        onSplashFinished()  // Trigger the completion callback
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splashhehe),
            contentDescription = "Splash Screen Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
