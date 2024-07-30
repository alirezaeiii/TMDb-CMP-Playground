package utils

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun initializeNapier() {
    Napier.base(DebugAntilog())
}