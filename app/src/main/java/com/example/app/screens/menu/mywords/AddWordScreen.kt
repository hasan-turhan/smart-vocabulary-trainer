package com.example.app.screens.mywords

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app.data.UserWordRepository
import com.example.app.data.Word

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordScreen(
    onSave: () -> Unit
) {
    var article by remember { mutableStateOf("") }
    var german by remember { mutableStateOf("") }
    var plural by remember { mutableStateOf("") }
    var english by remember { mutableStateOf("") }
    var example by remember { mutableStateOf("") }

    val isFormValid =
        article.isNotBlank() &&
                german.isNotBlank() &&
                plural.isNotBlank() &&
                english.isNotBlank() &&
                example.isNotBlank()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add New Word") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = article,
                onValueChange = { article = it.lowercase() },
                label = { Text("Article (der / die / das)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = german,
                onValueChange = { german = it },
                label = { Text("German word") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = plural,
                onValueChange = { plural = it },
                label = { Text("Plural") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = english,
                onValueChange = { english = it },
                label = { Text("English meaning") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = example,
                onValueChange = { example = it },
                label = { Text("Example sentence") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    UserWordRepository.addWord(
                        Word(
                            article = article.trim(),
                            german = german.trim(),
                            plural = plural.trim(),
                            english = english.trim(),
                            example = example.trim()
                        )
                    )
                    onSave()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text("Save Word")
            }
        }
    }
}
