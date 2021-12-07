package de.dikodam.calendar

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day06KtTest {

    @Test
    fun computePopulationTreeTest() {

        val params = listOf(
//            Triple(1, 0, 1L),  // (1)
//            Triple(1, 1, 1L),  // (0)
//            Triple(1, 2, 2L),  // (6,       8)
//            Triple(1, 3, 2L),  // (5,       7)
//            Triple(1, 4, 2L),  // (4,       6)
//            Triple(1, 5, 2L),  // (3,       5)
//            Triple(1, 6, 2L),  // (2,       4)
//            Triple(1, 7, 2L),  // (1,       3)
//            Triple(1, 8, 2L),  // (0,       2)
//            Triple(1, 9, 3L),  // (6,    8, 1)
//            Triple(1, 10, 3L), // (5,    7, 0)
//            Triple(1, 11, 4L), // (4,    6, 6, 8)
//            Triple(1, 12, 4L), // (3,    5, 5, 7)
//            Triple(1, 13, 4L), // (2,    4, 4, 6)
//            Triple(1, 14, 4L), // (1,    3, 3, 5)
//            Triple(1, 15, 4L), // (0,    2, 2, 4)
//            Triple(1, 16, 5L), // (6, 8, 1, 1, 3)
//            Triple(1, 17, 5L), // (5, 7, 0, 0, 2)
            Triple(1, 18, 6L), // (4, 6, 6, 1, 6, 8, 8)
        )

        for ((fish, cycles, expectedResult) in params) {
            assertThat(computeRes(fish, cycles)).isEqualTo(expectedResult);
        }

    }
}