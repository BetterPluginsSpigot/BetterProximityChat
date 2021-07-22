package io.github.michielproost.betterproximitychat;

import be.betterplugins.core.messaging.logging.BPLogger;
import be.betterplugins.core.messaging.messenger.Messenger;
import be.dezijwegel.betteryaml.BetterLang;
import be.dezijwegel.betteryaml.OptionalBetterYaml;
import io.github.michielproost.betterproximitychat.commands.CommandHandler;
import io.github.michielproost.betterproximitychat.events.EventListener;
import io.github.michielproost.betterproximitychat.util.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.Optional;
import java.util.logging.Level;

/**
 * BetterProximityChat Plugin:
 * A plugin in which you are only able to chat with players in the server
 * that are within a certain distance of your location (configurable).
 * @author Michiel Proost
 */
public class BetterProximityChat extends JavaPlugin {

    // Proximity chat is on by default.
    boolean proximityChatOn = true;

    /**
     * Constructor required for MockBukkit.
     */
    public BetterProximityChat(){
        super();
    }

    /**
     * Constructor required for MockBukkit.
     */
    protected BetterProximityChat(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
    {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable()
    {
        super.onEnable();

        // Plugin ID for bStats.
        int pluginId = 12115;
        Metrics metrics = new Metrics(this, pluginId );

        // Get configuration from BetterYaml.
        OptionalBetterYaml optionalConfig = new OptionalBetterYaml("config.yml", this);
        Optional<YamlConfiguration> loadResult = optionalConfig.getYamlConfiguration();

        // Configuration is not found.
        if ( !loadResult.isPresent() )
        {
            // Configuration was not found.
            Bukkit.getLogger().severe("[BPC] Configuration was not found. Disabling plugin.");
            // Disable plugin.
            getServer().getPluginManager().disablePlugin( this );
            // Move out of method.
            return;
        }

        // The configuration.
        YamlConfiguration config = loadResult.get();

        // The language.
        String language = config.getString( "language" );

        // Get localisation.
        BetterLang localisation = new BetterLang("lang.yml", language + ".yml", this);

        double chatRange = config.getDouble("chatRange");
        String chatRangeText;
        if (chatRange < 5)
            chatRangeText = "[0 - 5[";
        else if (chatRange >= 5 && chatRange < 10)
            chatRangeText = "[5 - 10[";
        else if (chatRange >= 10 && chatRange < 50)
            chatRangeText = "[10 - 50[";
        else
            chatRangeText = "[50 - inf[";
        metrics.addCustomChart( new SimplePie("chatrange",()-> chatRangeText ) );

        // Create messenger.
        Messenger messenger =
                new Messenger(
                        localisation.getMessages(),
                        new BPLogger(Level.WARNING),
                        ChatColor.YELLOW + "[BPC] " + ChatColor.BLUE
                );

        // Register listener.
        EventListener eventListener = new EventListener( messenger, config, this );
        this.getServer().getPluginManager().registerEvents( eventListener, this );

        // Register commands.
        CommandHandler commandHandler = new CommandHandler( messenger, this, config );
        this.getCommand("betterproximitychat").setExecutor( commandHandler );

        // Start UpdateChecker in a separate thread to not completely block the server.
        Thread updateChecker = new UpdateChecker(this);
        updateChecker.start();

    }

    @Override
    public void onDisable()
    {
        HandlerList.unregisterAll( this );
        super.onDisable();
    }

    /**
     * Reloads BetterProximityChat plugin.
     */
    public void reload()
    {
        this.onDisable();
        this.onEnable();
    }

    /**
     * Toggle between proximity chat on or off.
     */
    public void toggleProximityChatOn()
    {
        proximityChatOn = !proximityChatOn;
    }

    /**
     * Returns whether or not proximity chat is on.
     * @return Whether or not proximity chat is on.
     */
    public boolean isProximityChatOn() {
        return proximityChatOn;
    }

}