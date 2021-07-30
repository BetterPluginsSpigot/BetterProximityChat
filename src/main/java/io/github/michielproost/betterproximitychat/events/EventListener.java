package io.github.michielproost.betterproximitychat.events;

import be.betterplugins.core.messaging.messenger.Messenger;
import be.betterplugins.core.messaging.messenger.MsgEntry;
import io.github.michielproost.betterproximitychat.BetterProximityChat;
import io.github.michielproost.betterproximitychat.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Collections;

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
        // Display welcome message based on given configuration.
        if ( config.getBoolean( "welcomeMessage" ) )
            messenger.sendMessage(
                    event.getPlayer(),
                    "player.join",
                    new MsgEntry( "<PlayerName>", event.getPlayer().getDisplayName() )
            );
        // // Send plugin's state to player.
        plugin.sendState( Collections.singletonList( event.getPlayer() ) );
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

            // Send message to each nearby player.
            ArrayList<Player> nearbyPlayers = getNearbyPlayers( sender, config.getDouble( "chatRange" ) );

            // Other players are within range.
            if ( nearbyPlayers.size() > 0 ){
                // Send message without distance to sender.
                sender.sendMessage(
                        ChatColor.YELLOW + "<" + sender.getDisplayName() + "> "
                                + ChatColor.WHITE + event.getMessage() );
                // For each within range.
                for (Player receiver: nearbyPlayers) {
                    // Build ProximityChat message.
                    String message = buildMessage( sender, receiver, event.getMessage() );
                    // Send message to nearby player.
                    receiver.sendMessage( message );
                    if ( config.getBoolean( "soundsEnabled" ) ){
                        String receivingSound = config.getString( "receivingSound" );
                        try {
                            // Play chosen "message received" sound.
                            receiver.playSound( receiver.getLocation(), Sound.valueOf( receivingSound ), 1.0F, 1.0F );
                        } catch ( IllegalArgumentException exception ){
                            // Play default "message received" sound.
                            receiver.playSound( receiver.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F );
                        }
                    }
                }
                // Notify sender about amount of nearby players.
                messenger.sendMessage(
                        sender,
                        "players.found",
                        new MsgEntry("<FoundPlayersAmount>", nearbyPlayers.size() )
                );
            } else {
                // Notify sender that there are no nearby players.
                messenger.sendMessage( sender, "players.notfound" );
            }
        }
    }

    /**
     * Build ProximityChat message.
     * @param sender The sender of the message.
     * @param receiver The receiver of the message.
     * @param message The send message.
     * @return The BetterProximityChat message.
     */
    public String buildMessage(Player sender, Player receiver, String message )
    {
        // Distance between sender & receiver.
        double distance = sender.getLocation().distance( receiver.getLocation() );
        // Noisy messages are enabled.
        if ( config.getBoolean( "noiseEnabled") )
        {
            // Get degree of noise polynomial.
            int degree = config.getInt( "noisePolynomialDegree" );
            // Limit degree.
            degree = Math.max( degree, 1 );
            degree = Math.min( degree, 20 );
            // Calculate chance of error.
            double chanceError = distance / config.getDouble( "chatRange" );
            chanceError = Math.pow( chanceError, degree );
            // Generate new message based on noise.
            message = MessageUtil.addNoise( message, chanceError );
        }
        // Add username to message.
        message = ChatColor.YELLOW + "<" + sender.getDisplayName() + "> " + ChatColor.WHITE + message;
        // Add distance to message.
        return ChatColor.DARK_GREEN + "[" + String.format("%.1f", distance) + "] " + message;
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