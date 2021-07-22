package io.github.michielproost.betterproximitychat.events;

import be.betterplugins.core.messaging.messenger.Messenger;
import be.betterplugins.core.messaging.messenger.MsgEntry;
import io.github.michielproost.betterproximitychat.BetterProximityChat;
import io.github.michielproost.betterproximitychat.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

/**
 * Listens to events and acts accordingly.
 * @author Michiel Proost
 */
public class EventListener implements Listener {

    private final Messenger messenger;
    private final YamlConfiguration config;
    private final BetterProximityChat plugin;

    /**
     * Create a new EventListener.
     * @param config The YAML configuration.
     * @param messenger The messenger.
     * @param plugin The BetterProximityChat plugin.
     */
    public EventListener(Messenger messenger, YamlConfiguration config, BetterProximityChat plugin)
    {
        this.messenger = messenger;
        this.config = config;
        this.plugin = plugin;
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
        messenger.sendMessage( event.getPlayer(),"state.on" );
        messenger.sendMessage(
                event.getPlayer(),
                "state.chatrange",
                new MsgEntry( "<ChatRange>", config.getDouble( "chatRange") )
        );
        if ( config.getBoolean( "noiseEnabled") )
            messenger.sendMessage(
                    event.getPlayer(),
                    "state.noiserange",
                    new MsgEntry("<NoiseRange>", config.getDouble( "noiseRange") )
            );
    }

    /**
     * Only allow the player to chat with players that are within a certain range.
     * @param event The event.
     */
    @EventHandler( priority = EventPriority.HIGHEST )
    public void onPlayerChat( AsyncPlayerChatEvent event )
    {
        if ( plugin.isProximityChatOn() )
        {
            // The player that send the message.
            Player sender = event.getPlayer();

            // Remove recipients from event.
            event.getRecipients().clear();

            // Get chat range.
            double chatRange = config.getDouble( "chatRange");

            // Get noise range.
            double noiseRange = config.getDouble( "noiseRange");

            // Send message to each nearby player.
            ArrayList<Player> nearbyPlayers = getNearbyPlayers( sender, chatRange );

            // Other players are within range.
            if ( nearbyPlayers.size() > 0 ){
                // Set new recipients.
                event.getRecipients().addAll( nearbyPlayers );
                // Set yourself.
                event.getRecipients().add( sender );
                // Add noise?
                if ( config.getBoolean( "noiseEnabled") )
                {
                    // Only send original message to sender.
                    event.getRecipients().clear();
                    event.getRecipients().add( sender );
                    // For each within range.
                    for (Player player: nearbyPlayers) {
                        // Calculate distance.
                        double distance = sender.getLocation().distance( player.getLocation() );
                        // Calculate chance of error.
                        double chanceError = distance / noiseRange;
                        // Generate new message based on noise.
                        String message = MessageUtil.addNoise( event.getMessage(), chanceError );
                        // Send generated message to nearby player.
                        player.sendMessage( message );
                    }
                }
                // Notify sender about amount of nearby players.
                messenger.sendMessage(
                        sender,
                        "players.found",
                        new MsgEntry("<FoundPlayersAmount>", nearbyPlayers.size())
                );
            } else {
                // Notify sender that there are no nearby players.
                messenger.sendMessage( sender, "players.notfound");
            }
        }
    }

    /**
     * Get the players within a certain range of a given player.
     * @param player The given player.
     * @param range The maximum range between players.
     * @return The nearby players.
     */
    public static ArrayList<Player> getNearbyPlayers( Player player, double range )
    {
        // The nearby players.
        ArrayList<Player> nearby = new ArrayList<>();
        // Loop through all online players.
        for ( Player players: Bukkit.getOnlinePlayers() )
        {
            // Check if player is within range & discard yourself.
            if (player.getLocation().distance( players.getLocation() ) <= range &&
                    !players.getUniqueId().toString().equals( player.getUniqueId().toString() ) )
            {
                // Add to nearby players.
                nearby.add( players );
            }
        }
        return nearby;
    }

}