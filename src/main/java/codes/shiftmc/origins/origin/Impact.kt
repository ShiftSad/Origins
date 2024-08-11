package codes.shiftmc.origins.origin

import net.kyori.adventure.text.format.NamedTextColor

enum class Impact(
    val impactValue: Int,
    val translationKey: String,
    val color: NamedTextColor,
) {
    NONE(0, "nenhum", NamedTextColor.GRAY),
    LOW(1, "baixo", NamedTextColor.GREEN),
    MEDIUM(2, "médio", NamedTextColor.YELLOW),
    HIGH(3, "alto", NamedTextColor.RED);
}
