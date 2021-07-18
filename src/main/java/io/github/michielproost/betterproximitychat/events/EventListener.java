package io.github.michielproost.betterproximitychat.events;

import be.betterplugins.core.messaging.messenger.Messenger;
import be.betterplugins.core.messaging.messenger.MsgEntry;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listen to events and acts accordingly.
 * @author Michiel Proost
 */
public class EventListener implements Listener {

    private final Messenger messenger;
    private final YamlConfiguration config;

    /**
     * Create a new EventListener.
     * @param config The YAML configuration.
     * @param messenger The messenger.
     */
    public EventListener(Messenger messenger, YamlConfiguration config)
    {
        this.messenger = messenger;
        this.config = config;
    }

    /**
     * Show welcoming message to every player that joins the server.
     * @param event The event.
     */
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event)
    {
        // Display message based on given configuration.
        if ( config.getBoolean( "welcomeMessage" ) )
            messenger.sendMessage(
                    event.getPlayer(),
                    "player.join",
                    new MsgEntry( "<PlayerName>", event.getPlayer().getDisplayName() )
            );
    }

}