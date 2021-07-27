package io.github.michielproost.betterproximitychat.commands;

import be.betterplugins.core.commands.shortcuts.PlayerBPCommand;
import be.betterplugins.core.messaging.messenger.Messenger;
import be.betterplugins.core.messaging.messenger.MsgEntry;
import io.github.michielproost.betterproximitychat.BetterProximityChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used to handle the BetterProximityChat commands.
 * @author Michiel Proost
 */
public class CommandHandler implements CommandExecutor {

    // The messenger.
    private final Messenger messenger;
    // Map every command to its name.
    private final Map<String, PlayerBPCommand> commands;
    // Help command.
    private final HelpCommand helpCommand;

    /**
     * Create a CommandHandler.
     * @param messenger The messenger.
     * @param plugin The BetterProximityChat plugin.
     * @param config The YAML configuration.
     */
    public CommandHandler(Messenger messenger, BetterProximityChat plugin, YamlConfiguration config )
    {
        // Initialize the messenger.
        this.messenger = messenger;

        // Toggle command.
        PlayerBPCommand toggle = new ToggleCommand( messenger, plugin, config );
        // Reload command.
        PlayerBPCommand reload = new ReloadCommand( messenger, plugin );

        // Create map.
        this.commands = new HashMap<String, PlayerBPCommand>()
        {{
            // Toggle:
            put(toggle.getCommandName(), toggle);
            for ( String alias: toggle.getAliases() ){
                put(alias, toggle);
            }
            // Reload:
            put(reload.getCommandName(), reload);
            for (String alias: reload.getAliases() ){
                put(alias, reload);
            }
        }};

        // Help command.
        this.helpCommand = new HelpCommand( messenger, commands );
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command cmd,
                             @NotNull String label,
                             String[] args)
    {
        // Get name of desired command.
        String commandName = args.length == 0 ? "help" : args[0].toLowerCase();
        // Check if command exists.
        if ( commands.containsKey( commandName ) )
        {
            // Get appropriate command.
            PlayerBPCommand playerBPCommand = commands.get( commandName );
            // Has required permission.
            if (sender.hasPermission( playerBPCommand.getPermission( ) ) )
                // Execute command.
                return playerBPCommand.execute( sender, cmd, args );
            else
                messenger.sendMessage(
                        sender,
                        "permission.required",
                        new MsgEntry( "<Command>", "/bpc " + commandName )
                );
        }
        else
        {
            // Execute help command.
            return helpCommand.execute( sender, cmd, args );
        }
        // Command is used correctly.
        return true;
    }

}