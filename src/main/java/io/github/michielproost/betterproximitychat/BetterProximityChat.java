package io.github.michielproost.betterproximitychat;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

/**
 * BetterProximityChat Plugin:
 * A plugin in which you are only able to chat with players in the server
 * that are within a certain distance of your location (configurable).
 * @author Michiel Proost
 */
public class BetterProximityChat extends JavaPlugin {

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
    }

    @Override
    public void onDisable()
    {
        super.onDisable();
    }

}