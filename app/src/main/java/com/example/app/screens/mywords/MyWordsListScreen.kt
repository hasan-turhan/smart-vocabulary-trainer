package com.example.app.screens.mywords

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.app.data.UserWordRepository
import com.example.app.data.Word

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyWordsListScreen() {

    var words by remember {
        mutableStateOf(UserWordRepository.getAllWords())
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Words") }
            )
        }
    ) { padding ->

        if (words.isEmpty()) {
            EmptyState(padding)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(words) { index, word ->
                    MyWordCard(
                        word = word,
                        onDelete = {
                            UserWordRepository.removeWord(word)
                            words = UserWordRepository.getAllWords()
                        }

                    )
                }
            }
        }
    }
}

@Composable
private fun MyWordCard(
    word: Word,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🎨 ARTICLE BADGE
            Surface(
                shape = CircleShape,
                color = articleColor(word.article)
            ) {
                Text(
                    text = word.article.uppercase(),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(Modifier.width(16.dp))

            // 📘 WORD INFO
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = word.german,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Plural: ${word.plural}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (word.example.isNotBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = word.example,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 🗑️ DELETE
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete word",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun EmptyState(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No words yet",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Add your first word to start learning",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// 🎨 ARTICLE COLOR LOGIC
private fun articleColor(article: String): Color =
    when (article.lowercase()) {
        "der" -> Color(0xFF3F51B5) // blue
        "die" -> Color(0xFFD32F2F) // red
        "das" -> Color(0xFF388E3C) // green
        else -> Color.Gray
    }
