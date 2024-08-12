package codes.shiftmc.origins

import codes.shiftmc.origins.origin.OriginRegistry
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.entity.Player

object OriginCommand {

    init {
        CommandAPICommand("origin")
            .withSubcommands(set())
            .register()
    }

    private fun set(): CommandAPICommand {
        val arguments = listOf<Argument<*>>(
            PlayerArgument("player"),
            StringArgument("origin").replaceSuggestions(ArgumentSuggestions.strings(
                OriginRegistry.getOrigins().map { it.identifier() }
            ))
        )

        return CommandAPICommand("set")
            .withArguments(arguments)
            .executes(CommandExecutor { sender, args ->
                val player = args["player"] as Player
                val origin = OriginRegistry.getOrigin(args["origin"] as String) ?: run {
                    sender.sendMessage("Invalid origin")
                    return@CommandExecutor
                }

                OriginRegistry.setOrigin(player, origin)
            })
    }
}