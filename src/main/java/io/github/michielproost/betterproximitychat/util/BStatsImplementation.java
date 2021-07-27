package io.github.michielproost.betterproximitychat.util;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * bStats: Collect usage data for the BetterProximityChat plugin.
 * @author Thomas Verschoor, Michiel Proost
 */
public class BStatsImplementation {

    private final JavaPlugin plugin;
    private final YamlConfiguration config;

    /**
     * Create new bStats implementation for BetterProximityChat.
     * @param plugin The BetterProximityChat plugin.
     * @param config The YAML configuration.
     */
    public BStatsImplementation( JavaPlugin plugin, YamlConfiguration config )
    {
        this.plugin = plugin;
        this.config = config;
    }

    /**
     * Run the bStats implementation.
     */
    public void run()
    {
        // bStats addon.
        int pluginId = 12115;
        Metrics metrics = new Metrics( plugin, pluginId );

        /*
         * Custom bStats charts.
         */
        // Language:
        String language = config.getString( "language" );
        metrics.addCustomChart( new SimplePie("language",()-> language ) );
        // Chat range:
        // The maximum range between players in order to receive text messages from each other.
        double chatRange = config.getDouble( "chatRange" );
        String chatRangeText;
        if (chatRange < 5)
            chatRangeText = "[0 - 5[";
        else if (chatRange < 10)
            chatRangeText = "[5 - 10[";
        else if (chatRange < 50)
            chatRangeText = "[10 - 50[";
        else
            chatRangeText = "[50 - inf[";
        metrics.addCustomChart( new SimplePie("chatrange",()-> chatRangeText ) );
        // Noise enabled:
        // Whether noisy messages are turned on or not.
        boolean noiseEnabled = config.getBoolean( "noiseEnabled" );
        metrics.addCustomChart( new SimplePie("noiseenabled",()-> String.valueOf( noiseEnabled ) ) );
        // Welcome message:
        // Whether a welcome message should be shown when a player joins the server.
        boolean welcomeMessage = config.getBoolean( "welcomeMessage" );
        metrics.addCustomChart( new SimplePie("welcomemessage",()-> String.valueOf( welcomeMessage ) ) );
        // Degree of polynomial used for noise calculation:
        int noisePolynomialDegree = config.getInt( "noisePolynomialDegree" );
        String noisePolynomialDegreeText;
        if (noisePolynomialDegree < 5)
            noisePolynomialDegreeText = "[0 - 5[";
        else if (noisePolynomialDegree < 10)
            noisePolynomialDegreeText = "[5 - 10[";
        else if (noisePolynomialDegree < 15)
            noisePolynomialDegreeText = "[10 - 15[";
        else
            noisePolynomialDegreeText = "[15 - 20]";
        metrics.addCustomChart( new SimplePie("noisepolynomialdegree",()-> noisePolynomialDegreeText ) );
    }

}