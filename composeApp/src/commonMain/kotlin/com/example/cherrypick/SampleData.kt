package com.example.cherrypick

import androidx.compose.ui.graphics.Color
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

// Plain top-level vals -- no repository, no interface, no DI. There is exactly one
// "implementation" of this data (sample) and nothing to inject it into yet.
// skipped: Firestore. Wire dev.gitlive:firebase-firestore in when the leagues/users
// tables in the README ("Being built") actually exist.

enum class PickStatus(val emoji: String, val label: String, val tint: Color) {
    AllPicksMade("✅", "All picks made", Color(0xFF2E7D32)),
    MakePicksNow("❎", "Make picks now", Color(0xFFC62828)),
    PickedLate("🫠", "Picked late", Color(0xFFF9A825)),
    MissedPicks("😢", "Missed picks", Color(0xFF1565C0)),
    GamesStarted("😲", "Games Started!", Color(0xFFEF6C00)),
}

data class League(val id: String, val name: String, val status: PickStatus)

data class Game(
    val id: String,
    val away: String,
    val home: String,
    val startTimeUtc: Instant,
    val dayOfWeek: String,
    val venue: String,
)

val sampleLeagues: List<League> = listOf(
    League("league-1", "Office Pool", PickStatus.AllPicksMade),
    League("league-2", "Beer League Buddies", PickStatus.MakePicksNow),
    League("league-3", "Family Feud", PickStatus.PickedLate),
    League("league-4", "College Friends", PickStatus.MissedPicks),
    League("league-5", "Rec League Rivals", PickStatus.GamesStarted),
)

val sampleWeeks: Map<Int, List<Game>> = mapOf(
    1 to listOf(
        Game("g1-1", "TOR", "MTL", Instant.parse("2026-10-08T23:00:00Z"), "Thursday", "Bell Centre"),
        Game("g1-2", "BOS", "NYR", Instant.parse("2026-10-09T00:00:00Z"), "Thursday", "Madison Square Garden"),
        Game("g1-3", "EDM", "CGY", Instant.parse("2026-10-10T01:00:00Z"), "Friday", "Scotiabank Saddledome"),
        Game("g1-4", "COL", "VGK", Instant.parse("2026-10-10T02:00:00Z"), "Friday", "T-Mobile Arena"),
        Game("g1-5", "TBL", "FLA", Instant.parse("2026-10-10T23:00:00Z"), "Friday", "Amerant Bank Arena"),
        Game("g1-6", "CHI", "DET", Instant.parse("2026-10-11T00:00:00Z"), "Friday", "Little Caesars Arena"),
    ),
    2 to listOf(
        Game("g2-1", "VAN", "SEA", Instant.parse("2026-10-15T02:00:00Z"), "Wednesday", "Climate Pledge Arena"),
        Game("g2-2", "DAL", "NSH", Instant.parse("2026-10-15T23:00:00Z"), "Wednesday", "Bridgestone Arena"),
        Game("g2-3", "CAR", "NJD", Instant.parse("2026-10-16T00:00:00Z"), "Thursday", "Prudential Center"),
        Game("g2-4", "PHI", "OTT", Instant.parse("2026-10-16T23:30:00Z"), "Thursday", "Canadian Tire Centre"),
        Game("g2-5", "BUF", "ANA", Instant.parse("2026-10-17T02:00:00Z"), "Friday", "Honda Center"),
        Game("g2-6", "LAK", "SJS", Instant.parse("2026-10-17T02:30:00Z"), "Friday", "SAP Center"),
    ),
    3 to listOf(
        Game("g3-1", "MIN", "WPG", Instant.parse("2026-10-22T00:00:00Z"), "Wednesday", "Canada Life Centre"),
        Game("g3-2", "STL", "CBJ", Instant.parse("2026-10-22T23:00:00Z"), "Wednesday", "Nationwide Arena"),
        Game("g3-3", "NYI", "WSH", Instant.parse("2026-10-23T00:00:00Z"), "Thursday", "Capital One Arena"),
        Game("g3-4", "PIT", "TOR", Instant.parse("2026-10-23T23:00:00Z"), "Thursday", "Scotiabank Arena"),
        Game("g3-5", "MTL", "BOS", Instant.parse("2026-10-24T23:00:00Z"), "Friday", "TD Garden"),
        Game("g3-6", "EDM", "VAN", Instant.parse("2026-10-25T02:00:00Z"), "Friday", "Rogers Arena"),
    ),
)

data class Standing(
    val member: String,
    val wins: Int,
    val losses: Int,
    val points: Int,
)

// Rank is deliberately not a field -- it's list index + 1, so a stored rank can
// never contradict the sort order. Every list here must stay pre-sorted by points
// descending; commonTest/SampleDataTest enforces it.
//
// Scoring: 1 point per correct pick, +3 for a perfect week. That's why points run
// ahead of wins -- seeded directly, since there's no scoring engine yet.
val sampleStandings: Map<String, List<Standing>> = mapOf(
    // Office Pool -- runaway leader.
    "league-1" to listOf(
        Standing("Adam M.", 34, 14, 40),
        Standing("Priya K.", 27, 21, 30),
        Standing("Jordan T.", 25, 23, 27),
        Standing("Sam W.", 22, 26, 24),
        Standing("Liam O.", 18, 30, 19),
    ),
    // Beer League Buddies -- tight three-way race.
    "league-2" to listOf(
        Standing("Casey R.", 26, 22, 29),
        Standing("Devon P.", 26, 22, 28),
        Standing("Morgan L.", 25, 23, 27),
        Standing("Riley S.", 21, 27, 23),
    ),
    // Family Feud -- small league, mid-pack.
    "league-3" to listOf(
        Standing("Grandpa Joe", 24, 24, 26),
        Standing("Aunt Rina", 23, 25, 25),
        Standing("Cousin Dez", 20, 28, 21),
    ),
    // College Friends -- big roster, wide spread.
    "league-4" to listOf(
        Standing("Nate F.", 32, 16, 38),
        Standing("Owen B.", 29, 19, 33),
        Standing("Zoe H.", 26, 22, 29),
        Standing("Ines C.", 24, 24, 26),
        Standing("Mateo V.", 20, 28, 22),
        Standing("Yusuf A.", 16, 32, 17),
    ),
    // Rec League Rivals -- two neck-and-neck at the top, one clear last.
    "league-5" to listOf(
        Standing("Harper Q.", 28, 20, 31),
        Standing("Toni G.", 27, 21, 31),
        Standing("Blake N.", 22, 26, 24),
        Standing("Reese D.", 13, 35, 15),
    ),
)

// README: "times should be set to the user's local time." UTC -> local conversion,
// covered by a commonTest asserting a known Instant against a fixed TimeZone.
fun Game.localKickoffLabel(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val local = startTimeUtc.toLocalDateTime(timeZone)
    val hour24 = local.hour
    val period = if (hour24 < 12) "AM" else "PM"
    val hour12 = when (hour24 % 12) {
        0 -> 12
        else -> hour24 % 12
    }
    val minute = local.minute.toString().padStart(2, '0')
    return "$hour12:$minute $period"
}
