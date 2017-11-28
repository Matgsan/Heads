package me.matgsan.heads.util;

import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Matheus Santos (Matgsan)
 */
public class InventoryUtil {

    protected Player player;
    protected Inventory inventory;

    public InventoryUtil(final Player player, final Inventory inventory) {
        this.player = player;
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void openInventory() {
        final Inventory currentInventory = this.player.getOpenInventory().getTopInventory();
        final EntityPlayer ePlayer = ((CraftPlayer) this.player).getHandle();
        final Container defaultContainer = ePlayer.defaultContainer;
        final Container activeContainer = ePlayer.activeContainer;
        if (currentInventory != null && currentInventory.getTitle().equals(this.inventory.getTitle()) && this.inventory.getSize() == currentInventory.getSize()) {
            for (int i = 0; i < this.inventory.getSize(); ++i) {
                final ItemStack newItem = this.inventory.getItem(i);
                final ItemStack currentItem = currentInventory.getItem(i);
                if (currentItem == null || newItem == null || !newItem.isSimilar(currentItem)) {
                    if (newItem != null || currentItem != null) {
                        currentInventory.setItem(i, newItem);
                    }
                }
            }
            return;
        }
        if (activeContainer == defaultContainer) {
            this.player.openInventory(this.inventory);
        } else {
            CraftEventFactory.handleInventoryCloseEvent((EntityHuman) ePlayer);
            ePlayer.activeContainer = defaultContainer;
            this.player.openInventory(this.inventory);
        }
    }
}
