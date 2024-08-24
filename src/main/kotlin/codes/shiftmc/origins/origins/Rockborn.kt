package codes.shiftmc.origins.origins

import codes.shiftmc.origins.abilities.BiteSized
import codes.shiftmc.origins.abilities.SharpEater
import codes.shiftmc.origins.origin.Ability
import codes.shiftmc.origins.origin.Impact
import codes.shiftmc.origins.origin.Origin
import codes.shiftmc.origins.util.toMiniMessage
import net.kyori.adventure.text.Component

class Rockborn : Origin {
    override fun identifier(): String =
        "rockborn"

    override fun displayName(): Component =
        "Nascido da Rocha".toMiniMessage()

    override fun impact(): Impact =
        Impact.HIGH

    override fun description(): Component =
        """
            Vc nasceu da pedra, tipo a ametista do Steven Universo
            Vc deve ser bem resistente, né?
            Tu gosta de minerar, e minera bom.
        """.trimIndent().toMiniMessage()

    override fun abilities(): List<Ability> = listOf(
        BiteSized(health = -0.24, movementSpeed = -0.01, playerBlockInteractionRange = 0.0, scale = -0.25),
        SharpEater()
    )

}