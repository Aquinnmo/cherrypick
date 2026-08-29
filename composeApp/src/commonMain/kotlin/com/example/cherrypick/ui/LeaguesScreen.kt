package com.example.cherrypick.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cherrypick.League
import com.example.cherrypick.sampleLeagues

// leagues_schematic.jpg -- README calls the AI mockup "pretty bad", schematic wins.
// TopAppBar with an overflow menu top-right, LazyColumn of Cards below.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaguesScreen(onLeagueClick: (String) -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Leagues") },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Text("⋮")
                    }
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(text = { Text("Settings") }, onClick = { menuExpanded = false })
                        DropdownMenuItem(text = { Text("Sign out") }, onClick = { menuExpanded = false })
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(sampleLeagues, key = League::id) { league ->
                LeagueCard(league = league, onClick = { onLeagueClick(league.id) })
            }
        }
    }
}

@Composable
private fun LeagueCard(league: League, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(league.name, modifier = Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(league.status.emoji)
                Text(
                    " ${league.status.label}",
                    color = league.status.tint,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
    }
}
