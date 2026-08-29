package com.example.cherrypick

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant
import kotlinx.datetime.TimeZone

// The only non-trivial pure logic in this epic: UTC -> local game-time formatting,
// and the standings seed invariants that let rank stay derived (index + 1) instead
// of a stored field.
class SampleDataTest {

    @Test
    fun localKickoffLabel_convertsUtcToFixedLocalZone() {
        val game = Game(
            id = "test",
            away = "TOR",
            home = "MTL",
            startTimeUtc = Instant.parse("2026-10-08T23:00:00Z"),
            dayOfWeek = "Thursday",
            venue = "Bell Centre",
        )

        // America/New_York is UTC-4 in October (EDT) -> 23:00 UTC == 7:00 PM local.
        assertEquals("7:00 PM", game.localKickoffLabel(TimeZone.of("America/New_York")))
    }

    @Test
    fun sampleStandings_hasAnEntryForEveryLeague() {
        for (league in sampleLeagues) {
            assertTrue(
                sampleStandings.containsKey(league.id),
                "sampleStandings is missing an entry for ${league.id} (${league.name})",
            )
        }
    }

    @Test
    fun sampleStandings_everyListIsSortedByPointsDescending() {
        for ((leagueId, standings) in sampleStandings) {
            val points = standings.map { it.points }
            assertEquals(
                points.sortedDescending(),
                points,
                "sampleStandings[$leagueId] is not sorted by points descending: $points",
            )
        }
    }
}
