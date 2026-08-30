package com.example.cherrypick.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cherrypick.Game
import com.example.cherrypick.Standing
import com.example.cherrypick.localKickoffLabel
import com.example.cherrypick.sampleLeagues
import com.example.cherrypick.sampleStandings
import com.example.cherrypick.sampleWeeks
import kotlin.time.Clock
import kotlinx.coroutines.launch

// picks_schematic.jpg -- the most expressive-heavy screen.
// README constraints: header (TopAppBar + week tabs) is static, only the game
// list below it scrolls; grid lines stay implied via spacing, never drawn.
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PicksScreen(leagueId: String, onBack: () -> Unit) {
    val league = sampleLeagues.firstOrNull { it.id == leagueId }
    val weekNumbers = sampleWeeks.keys.sorted()
    val currentWeekIndex = remember { closestWeekIndex(weekNumbers) }
    val standingsPage = weekNumbers.size
    val pagerState = rememberPagerState(initialPage = currentWeekIndex) { weekNumbers.size + 1 }
    val onStandings = pagerState.currentPage == standingsPage
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(league?.name ?: "League") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                )
                AnimatedVisibility(visible = !onStandings) {
                    // Clamp: mid-swipe, currentPage reaches standingsPage while this row
                    // is still composed and animating out. Unclamped that's an out-of-range
                    // selectedTabIndex -- a crash that only reproduces mid-swipe.
                    PrimaryScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage.coerceAtMost(weekNumbers.lastIndex),
                    ) {
                        weekNumbers.forEachIndexed { index, week ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                                text = { Text("Week $week") },
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            // Segmented control, not two independent buttons -- this is a switcher between
            // two views of one league, not two actions. Selection derives from pagerState,
            // never its own remembered state, so a manual swipe and a tap can't disagree.
            // SegmentedButton over M3E ButtonGroup deliberately: ButtonGroup renders as
            // separate pills, the same shape language as the away/home ToggleButtons above,
            // which would read as "pick Standings to win".
            // View Game is deleted, not rehomed -- the README requirement is deferred.
            HorizontalFloatingToolbar(expanded = true) {
                SingleChoiceSegmentedButtonRow {
                    SegmentedButton(
                        selected = !onStandings,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(currentWeekIndex) } },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    ) { Text("Picks") }
                    SegmentedButton(
                        selected = onStandings,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(standingsPage) } },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    ) { Text("Standings") }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { padding ->
        // verticalAlignment defaults to CenterVertically. A page shorter than the
        // viewport gets centered -- invisible on week pages (games overflow), but it
        // floated the standings table into the middle of the screen with ~650px of
        // dead space above and below. Top-align every page, not just standings.
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxSize().padding(padding),
        ) { page ->
            if (page == standingsPage) {
                StandingsList(sampleStandings[leagueId].orEmpty())
            } else {
                val games = sampleWeeks.getValue(weekNumbers[page])
                WeekGameList(games)
            }
        }
    }
}

@Composable
private fun WeekGameList(games: List<Game>) {
    // Days are implied by spacing between groups, never a drawn rule.
    val gamesByDay = games.groupBy { it.dayOfWeek }
    LazyColumn(
        // Extra bottom padding so the last row isn't hidden behind the FloatingToolbar.
        contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        gamesByDay.forEach { (day, dayGames) ->
            item {
                Text(day, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp))
            }
            items(dayGames, key = Game::id) { game ->
                GameRow(game, modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

// Rank is index + 1, never a stored field -- see Standing in SampleData.kt.
@Composable
private fun StandingsList(standings: List<Standing>) {
    LazyColumn(
        // Same 96dp bottom inset as WeekGameList so the last row clears the toolbar.
        contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        itemsIndexed(standings, key = { _, standing -> standing.member }) { index, standing ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("${index + 1}", modifier = Modifier.weight(0.5f))
                Text(standing.member, modifier = Modifier.weight(2f))
                Text("${standing.wins}-${standing.losses}", modifier = Modifier.weight(1f))
                Text("${standing.points}", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun GameRow(game: Game, modifier: Modifier = Modifier) {
    var picked by rememberSaveable { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(game.localKickoffLabel() + " · " + game.venue)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ToggleButton(
                checked = picked == game.away,
                onCheckedChange = { checked -> picked = if (checked) game.away else null },
                modifier = Modifier.weight(1f),
            ) {
                Text(game.away)
            }
            ToggleButton(
                checked = picked == game.home,
                onCheckedChange = { checked -> picked = if (checked) game.home else null },
                modifier = Modifier.weight(1f),
            ) {
                Text(game.home)
            }
        }
    }
}

// "Current week" heuristic: the sample week whose first game is closest to now.
// Sample data is arbitrary/static, so this just avoids always landing on week 1.
private fun closestWeekIndex(weekNumbers: List<Int>): Int {
    val now = Clock.System.now()
    return weekNumbers.indices.minByOrNull { index ->
        val firstGame = sampleWeeks.getValue(weekNumbers[index]).first()
        val diff = firstGame.startTimeUtc.toEpochMilliseconds() - now.toEpochMilliseconds()
        if (diff < 0) -diff else diff
    } ?: 0
}
