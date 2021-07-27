package io.github.michielproost.betterproximitychat.commands;

import be.betterplugins.core.commands.shortcuts.PlayerBPCommand;
import be.betterplugins.core.messaging.messenger.Messenger;
import io.github.michielproost.betterproximitychat.BetterProximityChat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Command: /bpc toggle | t
 * Toggle between proximity chat on or off.
 * @author Michiel Proost
 */
public class ToggleCommand extends PlayerBPCommand {

    private final BetterProximityChat plugin;
    private final YamlConfiguration config;

    /**
     * Toggle between proximity chat on or off.
     * @param messenger The messenger.
     * @param plugin The BetterProximityChat plugin.
     * @param config The YAML configuration.
     */
    public ToggleCommand( Messenger messenger, BetterProximityChat plugin, YamlConfiguration config )
    {
        super( messenger );
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public @NotNull String getCommandName()
    {
        return "toggle";
    }

    @Override
    public @NotNull List<String> getAliases()
    {
        return Collections.singletonList("t");
    }

    @Override
    public @NotNull String getPermission()
    {
        return "betterproximitychat.toggle";
    }

    @Override
    public boolean execute( @NotNull Player player, @NotNull Command command, @NotNull String[] strings )
    {
        // Toggle proximity chat.
        plugin.toggleProximityChatOn();

        // Send plugin's state to every player online.
        plugin.sendState( new ArrayList<>( Bukkit.getOnlinePlayers() ) );

        // Command was used correctly.
        return true;
    }

}