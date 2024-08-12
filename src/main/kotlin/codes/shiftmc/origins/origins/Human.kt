package codes.shiftmc.origins.origins

import codes.shiftmc.origins.origin.Ability
import codes.shiftmc.origins.origin.Impact
import codes.shiftmc.origins.origin.Origin
import codes.shiftmc.origins.util.toMiniMessage
import net.kyori.adventure.text.Component

class Human : Origin {
    override fun identifier(): String =
        "none"

    override fun displayName(): Component =
        "Humano".toMiniMessage()

    override fun impact(): Impact =
        Impact.NONE

    override fun description(): Component =
        """
            Você é um humano, que chato.
        """.trimIndent().toMiniMessage()

    override fun abilities(): List<Ability> {
        return emptyList()
    }
}