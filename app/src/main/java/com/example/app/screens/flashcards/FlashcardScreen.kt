package com.example.app.screens.flashcards

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.data.Word
import com.example.app.utils.TextToSpeechManager
import com.example.app.viewmodel.FlashcardMode
import com.example.app.viewmodel.FlashcardViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    mode: FlashcardMode = FlashcardMode.ALL,
    viewModel: FlashcardViewModel = viewModel()
) {
    LaunchedEffect(mode) {
        viewModel.setMode(mode)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val tts = remember { TextToSpeechManager(context) }

    val word = viewModel.currentWord
    if (word == null) {
        EmptyStateScreen()
        return
    }

    var flipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(400),
        label = "cardFlip"
    )

    DisposableEffect(Unit) {
        onDispose { tts.shutdown() }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("German Flashcards") }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()
                        val threshold = 50.dp.toPx()
                        if (dragAmount < -threshold) {
                            flipped = false
                            viewModel.next()
                        } else if (dragAmount > threshold) {
                            flipped = false
                            viewModel.previous()
                        }
                    }
                },
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- PROGRESS BAR ---
            FlashcardProgressBar(viewModel.currentIndex, viewModel.totalWords)

            // --- ACTION BUTTONS ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Favorite
                IconButton(onClick = { viewModel.toggleFavorite(word) }) {
                    val isFav = viewModel.isFavorite(word)
                    Icon(
                        imageVector = if (isFav) Icons.Filled.Star else Icons.Outlined.Star,
                        tint = if (isFav) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = "Favorite"
                    )
                }

                // 🔀 SHUFFLE TOGGLE
                IconButton(
                    onClick = {
                        flipped = false
                        viewModel.toggleShuffle()
                        scope.launch {
                            val msg = if (viewModel.isShuffleOn) "Shuffle ON" else "Shuffle OFF"
                            snackbarHostState.showSnackbar(msg, duration = SnackbarDuration.Short, withDismissAction = true)
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        // Highlight color if Shuffle is ON
                        containerColor = if (viewModel.isShuffleOn) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                        contentColor = if (viewModel.isShuffleOn) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(Icons.Filled.Shuffle, "Shuffle Toggle")
                }

                // Sound
                IconButton(onClick = {
                    if (rotation <= 90f) tts.speakGerman(word.german)
                    else tts.speakEnglishUS(word.english)
                }) {
                    Icon(Icons.AutoMirrored.Filled.VolumeUp, "Speak", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // --- THE FLASHCARD ---
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        }
                        .clickable { flipped = !flipped },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                        if (rotation <= 90f) {
                            AnimatedContent(
                                targetState = word,
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                label = "frontContent"
                            ) { targetWord -> FrontSide(targetWord) }
                        } else {
                            Box(modifier = Modifier.graphicsLayer { rotationY = 180f }) {
                                BackSide(word)
                            }
                        }
                    }
                }
            }

            // --- NAVIGATION ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { flipped = false; viewModel.previous() },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Prev")
                }

                Button(
                    onClick = { flipped = false; viewModel.next() },
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Next")
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                }
            }
        }
    }
}

// --- HELPER COMPONENTS ---

@Composable
fun FlashcardProgressBar(current: Int, total: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        LinearProgressIndicator(
            progress = { (current + 1) / total.coerceAtLeast(1).toFloat() },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50))
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "${current + 1} / $total",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.align(Alignment.End),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FrontSide(word: Word) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            color = articleColor(word.article),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = word.article.uppercase(),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(word.german, style = MaterialTheme.typography.displaySmall, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text("Plural: ${word.plural}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))
        Text(word.example, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text("Tap to flip", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
private fun BackSide(word: Word) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("English Meaning", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(24.dp))
        Text(word.english, style = MaterialTheme.typography.displaySmall, textAlign = TextAlign.Center)
    }
}

@Composable
fun EmptyStateScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No cards available!", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
private fun articleColor(article: String): Color {
    val isDark = isSystemInDarkTheme()
    return when (article.uppercase()) {
        "DER" -> if (isDark) Color(0xFF64B5F6) else Color(0xFF1976D2)
        "DIE" -> if (isDark) Color(0xFFE57373) else Color(0xFFD32F2F)
        "DAS" -> if (isDark) Color(0xFF81C784) else Color(0xFF388E3C)
        else -> MaterialTheme.colorScheme.secondary
    }
}