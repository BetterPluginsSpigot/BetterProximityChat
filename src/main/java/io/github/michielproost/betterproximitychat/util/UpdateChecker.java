package io.github.michielproost.betterproximitychat.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
 * Checks for updates online and informs the user if there is a new version available or not.
 * This is done in a separate thread to NOT block the server.
 * @author Dieter Nuytemans, Thomas Verschoor
 */
public class UpdateChecker extends Thread {

    private final Plugin plugin;

    public UpdateChecker(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run()
    {
        // URL of the BetterProximityChat resource.
        URL url = null;

        try
        {
            // Get the latest version of the BetterProximityChat resource.
            url = new URL("https://api.spigotmc.org/legacy/update.php?resource=94441");
        }
        catch (MalformedURLException ignored) {}

        // Make connection with Spigot API.
        URLConnection conn = null;

        try
        {
            conn = Objects.requireNonNull( url ).openConnection();
        }
        catch (IOException | NullPointerException e)
        {
            Bukkit.getLogger().info(ChatColor.RED + "An error occurred while retrieving the latest version.");
        }

        try
        {
            // Read out the version.
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(conn).getInputStream()));

            // Get the latest version.
            String latestVersion = reader.readLine();

            // Get the current version.
            String currentVersion = plugin.getDescription().getVersion();

            // Check if the current and latest version are the same or not.
            if (latestVersion.equalsIgnoreCase( currentVersion ))
            {
                // Message that you are using the latest version.
                Bukkit.getLogger().info("[BetterProximityChat] You are using the latest version: " + currentVersion);
            }
            else {
                // Message that you're using an older version of BetterProximityChat.
                Bukkit.getLogger().info("[BetterProximityChat] You are using " + currentVersion + " and the latest release is " + latestVersion);
            }
        }
        catch (IOException | NullPointerException e)
        {
            // Error messaging.
            Bukkit.getLogger().info(ChatColor.RED + "An error occurred while retrieving the latest version!");
        }
    }

}