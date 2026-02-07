package com.example.app.screens.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app.screens.menu.components.MenuCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onLearnClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onMyWordsClick: () -> Unit,
    onAddWordClick: () -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("German Learning") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            /* HEADER */
            Column {
                Text(
                    text = "Learn German",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Build vocabulary step by step",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(12.dp))

            /* ACTION CARDS */
            MenuCard(
                icon = Icons.Default.List,
                title = "Learn Words",
                subtitle = "Practice with flashcards",
                onClick = onLearnClick
            )

            MenuCard(
                icon = Icons.Default.Star,
                title = "Favorite Words",
                subtitle = "Review saved vocabulary",
                onClick = onFavoritesClick
            )

            MenuCard(
                icon = Icons.Default.Edit,
                title = "My Words",
                subtitle = "Words you added yourself",
                onClick = onMyWordsClick
            )

            Spacer(modifier = Modifier.weight(1f))

            /* PRIMARY CTA */
            Button(
                onClick = onAddWordClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Add New Word")
            }
        }
    }
}
