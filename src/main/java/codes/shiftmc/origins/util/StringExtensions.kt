package codes.shiftmc.origins.util

import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

fun String.toMiniMessage(resolver: TagResolver) =
    MiniMessage.builder().build().deserialize(this, resolver)

fun String.toMiniMessage() =
    MiniMessage.builder().build().deserialize(this)

fun String.isEmailValid() =
    this.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$"))

fun String.isUUID() =
    this.matches(Regex("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}\$"))

fun String.isValidURL() =
    this.matches(Regex("^(http|https)://[^ \n]*\$"))
