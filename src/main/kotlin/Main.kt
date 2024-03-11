import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val state = rememberWindowState(width = 450.dp, height = 920.dp)
    Window(
        onCloseRequest = ::exitApplication,
        state = state,
        title = "Check IP info"
    ) {
        app()
    }
}

@Composable
@Preview
fun app() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { bestTopBar() },
        content = { innerPadding ->
            appUI(innerPadding)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun bestTopBar() {
    androidx.compose.material3.TopAppBar(
        title = { Text("Check IP info") },
        colors = topAppBarColors(
            containerColor = Color.Black,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary,

            ),
        navigationIcon = {
            IconButton(
                onClick = { },
                content = {
                    Icon(Icons.TwoTone.Home, null, tint = Color.White)
                }
            )
        },
        actions = {
            bestToggleButton()
        }
    )
}

@Composable
fun bestToggleButton() {
    val showDialogState = rememberSaveable { mutableStateOf(false) }

    IconToggleButton(
        checked = showDialogState.value,
        onCheckedChange = { showDialogState.value = it }
    ) {
        Icon(
            imageVector = Icons.TwoTone.Settings,
            contentDescription = null,
            tint = Color.White
        )
    }

    if (showDialogState.value) {
        bestShowDialog(showDialogState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun appUI(innerPadding: PaddingValues) {
    LazyColumn {
        item {
            MaterialTheme {
                bestOutlinedTextField()
            }
        }
        item {
            MaterialTheme {
                Row {
                    textButtonBest("Clear")
                    Spacer(modifier = Modifier.weight(0.1f))
                    buttonBest("Check IP")
                }
            }
        }
        item {
            bestOutlinedCard()
        }
    }
}

@Composable
fun bestOutlinedTextField() {
    var text by rememberSaveable { mutableStateOf("") }
    androidx.compose.material3.OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { androidx.compose.material3.Text(text = "Input !", color = Color.Black) },
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        singleLine = true,
        // isError = text.isEmpty(),
        supportingText = {
            if (text.isEmpty()) {
                androidx.compose.material3.Text(text = "Please input!", color = Color.Black)
            }
        },
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            )
    )
}

@Preview
@Composable
fun buttonBest(txt: String,) {
    androidx.compose.material3.Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(Color.Black),
        modifier = Modifier.padding(16.dp),
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        androidx.compose.material3.Text(txt)
    }
}

@Preview
@Composable
fun textButtonBest(txt: String) {
    androidx.compose.material3.OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = Modifier.padding(16.dp),
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        androidx.compose.material3.Text(text = txt, color = Color.Black)
    }
}

@Composable
fun bestOutlinedCard() {
    OutlinedCard(
        modifier = Modifier
            .fillMaxHeight(0.2f)
            .fillMaxWidth(0.2f)
            .padding(16.dp),
    ) {
        Text("121322")
    }
}

@Composable
fun bestShowDialog(showDialog:MutableState<Boolean>,onDismiss: () -> Unit = {}) {
    androidx.compose.material3.AlertDialog(
       onDismissRequest = {
         showDialog.value = false
         onDismiss()
       },
        containerColor = Color.White,
            title = { androidx.compose.material3.Text("Token setting") },
            text = {
                   Column(
                       modifier = Modifier.fillMaxWidth().padding(16.dp)
                   ) {
                      Row(modifier = Modifier.fillMaxHeight(0.15f)) {
                          bestOutlinedTextField()
                      }
                       Spacer(modifier = Modifier.height(16.dp))
                   }
            },
            confirmButton = {
                androidx.compose.material3.Button(colors = ButtonDefaults.buttonColors( Color.Black),onClick = { showDialog.value = false }) {
                    androidx.compose.material3.Text("OK")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showDialog.value = false }) {
                    androidx.compose.material3.Text("Cancel", color = Color.Black)
                }
            }
        )
}