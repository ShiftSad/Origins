package codes.shiftmc.origins.origin

import net.kyori.adventure.text.Component

interface Origin {

    fun identifier(): String
    fun displayName(): Component
    fun impact(): Impact
    fun description(): Component
    fun abilities(): List<Ability>
}