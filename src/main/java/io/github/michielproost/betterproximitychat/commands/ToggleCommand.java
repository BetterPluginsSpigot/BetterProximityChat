package io.github.michielproost.betterproximitychat.commands;

import be.betterplugins.core.commands.shortcuts.PlayerBPCommand;
import be.betterplugins.core.messaging.messenger.Messenger;
import be.betterplugins.core.messaging.messenger.MsgEntry;
import io.github.michielproost.betterproximitychat.BetterProximityChat;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Command: /bpc toggle | t
 * Toggle between proximity chat on or off.
 * @author Michiel Proost
 */
public class ToggleCommand extends PlayerBPCommand {

    private BetterProximityChat plugin;

    /**
     * Toggle between proximity chat on or off.
     * @param messenger The messenger.
     * @param plugin The BetterProximityChat plugin.
     */
    public ToggleCommand( Messenger messenger, BetterProximityChat plugin )
    {
        super( messenger );
        this.plugin = plugin;
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
    public boolean execute(@NotNull Player player, @NotNull Command command, @NotNull String[] strings)
    {
        // Toggle proximity chat.
        plugin.toggleProximityChatOn();

        // Notify player about state of proximity chat.
        if ( plugin.isProximityChatOn( ) )
            messenger.sendMessage(
                    player,
                    "state.info",
                    new MsgEntry( "<ProximityChatState>", "on" )
            );
        else
            messenger.sendMessage(
                    player,
                    "state.info",
                    new MsgEntry( "<ProximityChatState>", "off" )
            );

        // Command was used correctly.
        return true;
    }

}