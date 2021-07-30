package io.github.michielproost.betterproximitychat.commands;

import be.betterplugins.core.commands.shortcuts.PlayerBPCommand;
import be.betterplugins.core.messaging.messenger.Messenger;
import io.github.michielproost.betterproximitychat.events.SoundGUI;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Command: /bpc sound | s
 * Opens the sound GUI in which the player can choose their desired "received message" sound.
 * @author Michiel Proost
 */
public class SoundCommand extends PlayerBPCommand {

    private final SoundGUI soundGUI;

    /**
     * Opens the sound GUI in which the player can choose their desired "received message" sound.
     * @param messenger The messenger.
     * @param soundGUI The sound GUI.
     */
    public SoundCommand( Messenger messenger, SoundGUI soundGUI )
    {
        super( messenger );
        this.soundGUI = soundGUI;
    }

    @Override
    public @NotNull String getCommandName()
    {
        return "sound";
    }

    @Override
    public @NotNull List<String> getAliases()
    {
        return Collections.singletonList("s");
    }

    @Override
    public @NotNull String getPermission() {
        return "betterproximitychat.sound";
    }

    @Override
    public boolean execute(@NotNull Player player, @NotNull Command command, @NotNull String[] strings)
    {
        // Open sound GUI.
        soundGUI.openSoundGUI( player );
        // Command was used correctly.
        return true;
    }

}