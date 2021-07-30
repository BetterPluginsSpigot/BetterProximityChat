package io.github.michielproost.betterproximitychat.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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

/**
 * A GUI in which every player can choose their desired "received message" sound.
 * @author Michiel Proost
 */
public class SoundGUI implements Listener {

    private final Inventory inventory;

    /**
     * Create the preset inventory along with its items.
     */
    public SoundGUI()
    {
        // New inventory with no owner and size nine.
        inventory = Bukkit.createInventory( null, 9, "soundGUI" );

        // Initialize the inventory with preset sounds.
        initializeSounds();
    }

    /**
     * Place all the possible, preset, sounds in the inventory.
     */
    public void initializeSounds()
    {
        inventory.addItem(
                createGuiItem(
                    Material.EGG ,
                    "Chicken Egg",
                        "Click on item to make this your receiving sound."
                )
        );
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
        // Verify that correct item is clicked.
        player.sendMessage( "Your new receiving sound is:" + clickedItem.getItemMeta().getDisplayName() );
    }

    // Cancel dragging in inventory.
    @EventHandler
    public void onInventoryDrag( final InventoryDragEvent event )
    {
        if ( event.getInventory().equals( inventory ) ) {
            event.setCancelled( true );
        }
    }

}