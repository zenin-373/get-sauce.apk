package com.getsauce.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF0B0D12)
private val Panel = Color(0xFF151923)
private val Accent = Color(0xFF8B7CFF)
private val Muted = Color(0xFF9AA3B2)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GetSauceApp() }
    }
}

@Composable
private fun GetSauceApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var query by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Paste a link or search for a title to get started.") }
    MaterialTheme(colorScheme = darkColorScheme(primary = Accent, background = Ink, surface = Panel, onBackground = Color.White, onSurface = Color.White)) {
        Scaffold(
            containerColor = Ink,
            bottomBar = {
                NavigationBar(containerColor = Panel, contentColor = Color.White) {
                    NavigationBarItem(selectedTab == 0, { selectedTab = 0 }, icon = { Icon(Icons.Default.Search, null) }, label = { Text("Search") })
                    NavigationBarItem(selectedTab == 1, { selectedTab = 1 }, icon = { Icon(Icons.Default.Download, null) }, label = { Text("Downloads") })
                    NavigationBarItem(selectedTab == 2, { selectedTab = 2 }, icon = { Icon(Icons.Default.History, null) }, label = { Text("History") })
                }
            }
        ) { padding ->
            when (selectedTab) {
                0 -> SearchScreen(query, { query = it }, status, {
                    status = if (query.isBlank()) "Enter a URL or search term first."
                    else "Search UI is ready. Downloader engine integration is the next step."
                }, Modifier.padding(padding))
                1 -> EmptyScreen(Icons.Default.Download, "No downloads yet", "Your active and completed downloads will appear here.", Modifier.padding(padding))
                else -> EmptyScreen(Icons.Default.History, "Nothing in history", "Your previous searches will appear here.", Modifier.padding(padding))
            }
        }
    }
}

@Composable
private fun SearchScreen(query: String, onQueryChange: (String) -> Unit, status: String, onSearch: () -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().padding(horizontal = 20.dp), contentPadding = PaddingValues(top = 30.dp, bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = Accent.copy(alpha = 0.16f), shape = RoundedCornerShape(14.dp)) {
                    Icon(Icons.Default.Downloading, null, tint = Accent, modifier = Modifier.padding(12.dp).size(28.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("GET-SAUCE", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.5.sp)
                    Text("Search • Extract • Download", color = Muted, fontSize = 12.sp)
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Find something", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Text("Paste a supported URL or search by keyword.", color = Muted)
            }
        }
        item {
            OutlinedTextField(
                value = query, onValueChange = onQueryChange, modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Paste URL or enter keywords…", color = Muted) },
                leadingIcon = { Icon(Icons.Default.Link, null, tint = Muted) },
                shape = RoundedCornerShape(16.dp), minLines = 2,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Accent, unfocusedBorderColor = Color(0xFF343B49), focusedTextColor = Color.White, unfocusedTextColor = Color.White, cursorColor = Accent)
            )
            Spacer(Modifier.height(12.dp))
            Button(onClick = onSearch, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp)) {
                Icon(Icons.Default.Search, null)
                Spacer(Modifier.width(8.dp))
                Text("Search / Extract", fontWeight = FontWeight.Bold)
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Supported sources", fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SourceChip("HStream")
                        SourceChip("Oppai Stream")
                    }
                    Text("More extractors can be added as the engine is integrated.", color = Muted, fontSize = 12.sp)
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Accent.copy(alpha = 0.12f)), shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, null, tint = Accent)
                    Spacer(Modifier.width(10.dp))
                    Text(status, color = Color.White, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun SourceChip(label: String) {
    Surface(color = Color(0xFF272D3A), shape = RoundedCornerShape(50)) {
        Text(label, modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp), fontSize = 12.sp, color = Color.White)
    }
}

@Composable
private fun EmptyScreen(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().padding(28.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(icon, null, tint = Accent, modifier = Modifier.size(48.dp))
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Muted)
        }
    }
}
