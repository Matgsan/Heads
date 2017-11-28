package me.matgsan.heads;

import me.matgsan.heads.customheads.CustomHead;
import me.matgsan.heads.customheads.CustomHeadCategory;
import me.matgsan.heads.customheads.CustomHeadManager;
import me.matgsan.heads.customheads.animatedheads.AnimatedHead;
import me.matgsan.heads.customheads.animatedheads.AnimatedHeadManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Matheus Santos (Matgsan)
 */
public class Listeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final ItemStack item = event.getCurrentItem();
        final Inventory inventory = event.getInventory();
        if (item == null) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        final String title = inventory.getTitle().toLowerCase();
        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                final String itemName = item.getItemMeta().getDisplayName();
                if (CustomHead.getCustomHeadByDisplayName(itemName) != null || AnimatedHead.getAnimatedHeadByDisplayName(itemName) != null) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
        if (title.startsWith("cabeças") || title.startsWith("animações")) {
            event.setCancelled(true);
            if (title.startsWith("cabeças")) {
                if (title.startsWith("cabeças - menu")) {
                    CustomHeadManager.onMenuClick(event);
                } else {

                    CustomHeadCategory.onHeadClick(event);
                }
            } else {
                AnimatedHeadManager.onAnimationClick(event);
            }
        }
    }
}
