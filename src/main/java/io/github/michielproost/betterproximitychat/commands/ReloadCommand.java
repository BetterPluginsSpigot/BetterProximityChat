package io.github.michielproost.betterproximitychat.commands;

import be.betterplugins.core.commands.shortcuts.PlayerBPCommand;
import be.betterplugins.core.messaging.messenger.Messenger;
import io.github.michielproost.betterproximitychat.BetterProximityChat;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Command: /bpc reload | r
 * Toggle between proximity chat on or off.
 * @author Michiel Proost
 */
public class ReloadCommand extends PlayerBPCommand  {

    private final BetterProximityChat plugin;

    /**
     * Reloads BetterProximityChat configuration files.
     * @param messenger The messenger.
     * @param plugin The BetterProximityChat plugin.
     */
    public ReloadCommand(Messenger messenger, BetterProximityChat plugin) {
        super(messenger);
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getCommandName()
    {
        return "reload";
    }

    @Override
    public @NotNull List<String> getAliases()
    {
        return Collections.singletonList("r");
    }

    @Override
    public @NotNull String getPermission()
    {
        return "betterproximitychat.reload";
    }

    @Override
    public boolean execute(@NotNull Player player, @NotNull Command command, @NotNull String[] strings)
    {
        // Reload plugin.
        plugin.reload();
        // Notify player of successful reload.
        messenger.sendMessage( player, "reload.complete" );
        // Command was used correctly.
        return true;
    }

}