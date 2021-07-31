package io.github.michielproost.betterproximitychat.events;

import io.github.michielproost.betterproximitychat.BetterProximityChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A GUI in which every player can choose their desired notification sound.
 * @author Michiel Proost
 */
public class SoundGUI implements Listener {
    
    private static final Material[] materials = {
            Material.EGG, Material.SNOWBALL, Material.COOKED_CHICKEN, Material.CAT_SPAWN_EGG,
            Material.CREEPER_HEAD, Material.BROWN_MUSHROOM, Material.SLIME_BLOCK, Material.NOTE_BLOCK,
            Material.BEE_NEST
    };
    private static final String[] names = {
            "Chicken Egg", "Snowball Throw", "Chicken Hurt", "Cat Purr", "Creeper Hurt",
            "Mushroom Eat", "Slime Squish", "Note Bell", "Bee Sting"
    };
    private static final Sound[] sounds = {
            Sound.ENTITY_CHICKEN_EGG, Sound.ENTITY_SNOWBALL_THROW, Sound.ENTITY_CHICKEN_HURT,
            Sound.ENTITY_CAT_PURR, Sound.ENTITY_CREEPER_HURT, Sound.ENTITY_MOOSHROOM_EAT,
            Sound.ENTITY_SLIME_SQUISH, Sound.BLOCK_NOTE_BLOCK_BELL, Sound.ENTITY_BEE_STING
    };

    private final Inventory inventory;
    private final HashMap<String, Sound> soundMap;

    private final BetterProximityChat plugin;

    /**
     * Create the preset inventory along with its items.
     * @param plugin The BetterProximityChat plugin.
     * @param language The required language.
     */
    public SoundGUI( BetterProximityChat plugin, String language )
    {
        this.plugin = plugin;

        // New inventory with no owner and size nine.
        inventory = Bukkit.createInventory( null, 9, "soundGUI" );

        // Initialize the inventory with preset sounds & return map.
        soundMap = initializeSounds( language );
    }

    /**
     * Place all the possible, preset, sounds in the inventory.
     * Return map linking every item's name to its appropriate sound.
     * @param language The required language.
     */
    public HashMap<String, Sound> initializeSounds( String language )
    {
        String[] lore;
        if ( language.equals("en_us") ) {
            lore = new String[]{
                    "-------------------------------",
                    "   Click on the icon to make   ",
                    " your new notification sound.  ",
                    "-------------------------------"
            };
        } else {
            lore = new String[]{
                    "------------------------------------",
                    "    Klik op het pictogram om dit    ",
                    " uw nieuwe meldingsgeluid te maken. ",
                    "------------------------------------"
            };
        }
        HashMap<String, Sound> soundMap = new HashMap<>();
        for (int i = 0; i < inventory.getSize(); i++)
        {
            // Add item to inventory.
            inventory.addItem(
              createGuiItem(
                    materials[i], names[i],
                      lore[0], lore[1], ChatColor.YELLOW + names[i], lore[2], lore[3]
              )
            );
            soundMap.put( names[i], sounds[i] );
        }
        return soundMap;
    }

    /**
     * Create a GUI item with a custom name & description.
     * @param material The desired material.
     * @param name The desired name.
     * @param lore The item's lore.
     * @return The newly created GUI item.
     */
    protected ItemStack createGuiItem( final Material material, final String name, final String... lore)
    {
        // Create a new ItemStack of the given material.
        final ItemStack item = new ItemStack( material, 1 );
        // Get the ItemMeta.
        final ItemMeta meta = item.getItemMeta();
        // Set the name of the item.
        meta.setDisplayName( name );
        // Set the lore of the item.
        meta.setLore( Arrays.asList( lore ) );
        // Set the new ItemMeta.
        item.setItemMeta(meta);
        // Return the newly created item.
        return item;
    }

    /**
     * Open the sound GUI.
     * @param entity The player that wants to access the GUI.
     */
    public void openSoundGUI(final HumanEntity entity )
    {
        entity.openInventory( inventory );
    }

    /**
     * When a player selects a sound in the GUI.
     * @param event The event.
     */
    @EventHandler
    public void onInventoryClick( final InventoryClickEvent event )
    {
        // Different inventory, do nothing.
        if ( event.getInventory() != inventory )
            return;
        event.setCancelled( true );
        // Get clicked item.
        final ItemStack clickedItem = event.getCurrentItem();
        // Verify that current item is not null
        if ( clickedItem == null || clickedItem.getType().isAir() )
            return;
        // Get player that clicked on the item.
        final Player player = (Player) event.getWhoClicked();
        // Get the item's display name.
        String clickedItemName = clickedItem.getItemMeta().getDisplayName();
        // Set player's new notification sound.
        plugin.setPlayerSound( player, soundMap.get( clickedItemName ) );
        // Play sound to player.
        player.playSound( player.getLocation(), soundMap.get( clickedItemName ), 1.0F, 1.0F );
    }

    /**
     * Cancel dragging in the sound GUI.
     * @param event The event.
     */
    @EventHandler
    public void onInventoryDrag( final InventoryDragEvent event )
    {
        if ( event.getInventory().equals( inventory ) ) {
            event.setCancelled( true );
        }
    }

}