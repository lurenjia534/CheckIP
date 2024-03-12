import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import backend.IPQueryService
import kotlinx.coroutines.launch

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
    val tokenState = rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { bestTopBar() },
        content = {innerPadding ->
            appUI(tokenState, innerPadding)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun bestTopBar() {
    TopAppBar(
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
    // 定义tokenState，供bestShowDialog使用
    val tokenState = rememberSaveable { mutableStateOf("") }

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

    // 在if条件下调用bestShowDialog，并传递tokenState
    if (showDialogState.value) {
        bestShowDialog(showDialog = showDialogState, tokenState = tokenState)
    }
}

@Composable
fun appUI(tokenState: MutableState<String>, innerPadding: PaddingValues) {
    var ip by rememberSaveable { mutableStateOf("") }
    var ipInfo by remember { mutableStateOf("") } // 新状态用于存储IP信息

    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        item {
            MaterialTheme {
                bestOutlinedTextField(ip, onValueChange = { ip = it })
            }
        }
        item {
            MaterialTheme {
                Row {
                    textButtonBest("Clear", onClear = { ip = "" })
                    Spacer(modifier = Modifier.weight(0.5f))
                    buttonBest(txt = "Check IP", ip = ip, tokenState = tokenState, updateIpInfo = { result ->
                        ipInfo = result // 更新查询结果
                    })
                }
            }
        }
        item {
            bestOutlinedCard(ipInfo) // 传递IP信息
        }
    }
}

@Composable
fun bestOutlinedTextField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { androidx.compose.material3.Text("Input IP", color = Color.Black) },
        modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray
        )
    )
}

@Composable
fun buttonBest(txt: String, ip: String, tokenState: MutableState<String>, updateIpInfo: (String) -> Unit) {
    val coroutineScope = rememberCoroutineScope() // 获取协程作用域

    Button(
        onClick = {
            // 在协程中异步执行IP查询
            coroutineScope.launch {
                val ipQueryService = IPQueryService(token = tokenState.value)
                try {
                    val ipInfo = ipQueryService.query(ip)
                    updateIpInfo(ipInfo) // 使用查询结果更新状态
                } catch (e: Exception) {
                    updateIpInfo("Failed to retrieve IP information: ${e.message}")
                }
            }
        },
        colors = ButtonDefaults.buttonColors(Color.Black),
        modifier = Modifier.padding(16.dp)
    ) {
        Text(txt, color = Color.White)
    }
}

@Composable
fun textButtonBest(txt: String, onClear: () -> Unit) {
    androidx.compose.material3.OutlinedButton(
        onClick = onClear,
        modifier = Modifier.padding(16.dp),
    ) {
        Text(txt)
    }
}

@Composable
fun bestOutlinedCard(ipInfo: String) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(16.dp)
            .height(300.dp)
            .width(200.dp),
        border = CardDefaults.outlinedCardBorder(true)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (ipInfo.isNotEmpty()) {
                Text(text = ipInfo) // 显示IP信息
            } else {
                Text(text = "No IP Info Available") // 当没有信息时的占位文本
            }
        }
    }
}

@Composable
fun bestShowDialog(showDialog: MutableState<Boolean>, tokenState: MutableState<String>, onDismiss: () -> Unit = {}) {
    var tempToken by rememberSaveable { mutableStateOf(tokenState.value) }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = {
            showDialog.value = false
            onDismiss()
        },
        title = { Text("Token Setting") },
        text = {
            Column {
                // 这里应使用一个新的TextField来接受tempToken，而不是bestOutlinedTextField，因为需要特定于这个场景
               OutlinedTextField(
                    value = tempToken,
                    onValueChange = { tempToken = it },
                    label = { Text("API Token") },
                   colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Gray
                   )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    tokenState.value = tempToken // 更新令牌状态
                    showDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(Color.Black)
                ) {
                Text("OK", color = Color.White)
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = { showDialog.value = false }) {
                Text("Cancel")
            }
        }
    )
}