package me.matgsan.heads.customheads;

import java.util.ArrayList;
import java.util.List;
import me.matgsan.heads.util.HeadsUtil;
import me.matgsan.heads.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Matheus Santos (Matgsan)
 */
public class CustomHeadManager {

    private static final List<CustomHead> CUSTOM_HEAD_LIST = new ArrayList();

    public static List<CustomHead> getCustomHeadList() {
        return CUSTOM_HEAD_LIST;
    }

    public static void setCustomHeadList(List<CustomHead> customHeadsList) {
        for (CustomHead head : customHeadsList) {
            head.setGameProfile(HeadsUtil.getGameProfile(head));
            head.setHeadItem(HeadsUtil.getHeadItem(head));
            CUSTOM_HEAD_LIST.add(head);
        }
    }

    public static void openCustomHeadsMenu(Player p) {
        int startSlot = 0;
        Inventory inventory = Bukkit.createInventory(null, 9, "Cabeças - Menu");
        for (CustomHeadCategory categorias : CustomHeadCategory.values()) {
            if (categorias.getHeads().isEmpty()) {
                continue;
            }
            ItemStack randomHead = categorias.getRandomHead().getHeadItem();
            ItemMeta randomHeadMeta = randomHead.getItemMeta();
            randomHeadMeta.setDisplayName(ChatColor.GOLD + categorias.getName());
            randomHead.setItemMeta(randomHeadMeta);
            inventory.setItem(startSlot, randomHead);
            startSlot++;
        }
        new InventoryUtil(p, inventory).openInventory();
    }

    public static void onCustomHeadMenuClick(InventoryClickEvent event) {
        final ItemStack item = event.getCurrentItem();
        final Player p = (Player) event.getWhoClicked();
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            CustomHeadCategory category = CustomHeadCategory.getCustomHeadCategoryByDisplayName(item.getItemMeta().getDisplayName());
            if (category == null) {
                p.closeInventory();
                p.sendMessage(ChatColor.RED + "Houve algum problema ao identificar a categoria que você escolheu, tente mais tarde.");
                return;
            }
            CustomHeadCategory.openInventory(p, category);
        }
    }
}
