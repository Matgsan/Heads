package me.matgsan.heads.customheads.animatedheads;

import java.util.ArrayList;
import java.util.List;
import me.matgsan.heads.Head;
import me.matgsan.heads.customheads.CustomHead;
import me.matgsan.heads.customheads.CustomHeadManager;
import me.matgsan.heads.util.HeadsUtil;
import me.matgsan.heads.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Matheus Santos (Matgsan)
 */
public class AnimatedHeadManager {

    private static final List<AnimatedHead> ANIMATED_HEAD_LIST = new ArrayList();

    public static List<AnimatedHead> getAnimatedHeadList() {
        return ANIMATED_HEAD_LIST;
    }

    public static void setAnimatedHeads(List<AnimatedHead> animatedHeadsList) {
        for (AnimatedHead animatedHead : animatedHeadsList) {
            List<Head> frames = new ArrayList();
            for (String texture : animatedHead.getTextures()) {
                Head frame = new Head(texture) {
                };
                frame.setGameProfile(HeadsUtil.getGameProfile(frame));
                frame.setHeadItem(HeadsUtil.getHeadItem(frame));
                frames.add(frame);
            }
            animatedHead.setFrames(frames);
            ANIMATED_HEAD_LIST.add(animatedHead);
        }
    }

    public static void openAnimatedHeadsInventory(Player p) {
        openAnimatedHeadsInventory(p, 1);
    }

    public static void openAnimatedHeadsInventory(Player p, int page) {
        if (page < 1) {
            page = 1;
        }
        boolean canGoBack = page > 1;
        boolean canGoFoward = false;
        int inventorysize = 54;
        int emptyRow = 1;
        int filledSlots = inventorysize - (emptyRow * 9);
        int startIndex = (page - 1) * filledSlots;
        int endIndex = page * filledSlots;
        if (endIndex > getAnimatedHeadList().size()) {
            endIndex = getAnimatedHeadList().size();
        } else {
            canGoFoward = true;
        }
        int startSlot = 0;
        Inventory inventory = Bukkit.createInventory(null, inventorysize, "Animações");
        for (AnimatedHead head : getAnimatedHeadList().subList(startIndex, endIndex)) {
            ItemStack headItem = head.getRandomFrame().getHeadItem();
            ItemMeta headItemMeta = headItem.getItemMeta();
            boolean hasPermission = p.hasPermission(head.getPermission());
            List<String> lore = new ArrayList();
            lore.add(hasPermission ? ChatColor.GRAY + "Clique para usar." : ChatColor.RED + "Você não tem essa cabeça");
            headItemMeta.setLore(lore);
            headItemMeta.setDisplayName(ChatColor.GOLD + head.getName());
            headItem.setItemMeta(headItemMeta);
            inventory.setItem(startSlot, headItem);
            startSlot++;
        }
        if (canGoBack) {
            ItemStack previous = new ItemStack(Material.ARROW);
            ItemMeta previousMeta = previous.getItemMeta();
            previousMeta.setDisplayName(ChatColor.GOLD + "Voltar para a página " + (page - 1));
            previous.setItemMeta(previousMeta);
            inventory.setItem((inventorysize - 8), previous);
        }
        if (canGoFoward) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.setDisplayName(ChatColor.GOLD + "Ir para a página " + (page + 1));
            next.setItemMeta(nextMeta);
            inventory.setItem((inventorysize - 1), next);
        }
        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta exitMeta = exit.getItemMeta();
        exitMeta.setDisplayName(ChatColor.RED + "Fechar");
        exit.setItemMeta(exitMeta);
        inventory.setItem((inventorysize - 9), exit);
        new InventoryUtil(p, inventory).openInventory();
    }

    public static void onAnimationChoose(InventoryClickEvent event) {
        final ItemStack item = event.getCurrentItem();
        final Player p = (Player) event.getWhoClicked();
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String itemDisplayName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            if (item.getType() == Material.BARRIER) {
                p.closeInventory();
                return;
            }
            if (item.getType() == Material.ARROW) {
                int pagina = 0;
                if (itemDisplayName.startsWith("Voltar para a página") || itemDisplayName.startsWith("Ir para a página")) {
                    try {
                        pagina = Integer.parseInt(itemDisplayName.split("página")[1].replace(" ", ""));
                    } catch (NumberFormatException e) {
                        pagina = 0;
                    }
                }
                if (pagina < 1) {
                    p.sendMessage(ChatColor.RED + "Houve algum problema ao identificar a página que você escolheu, tente mais tarde.");
                    return;
                }
                openAnimatedHeadsInventory(p, pagina);
                return;
            }
            AnimatedHead animation = AnimatedHead.getAnimatedHeadByDisplayName(item.getItemMeta().getDisplayName());
            if (animation == null) {
                p.closeInventory();
                p.sendMessage(ChatColor.RED + "Houve algum problema ao identificar a animação que você escolheu, tente mais tarde.");
                return;
            }
            if (!p.hasPermission(animation.getPermission())) {
                p.closeInventory();
                p.sendMessage(ChatColor.RED + "Você não tem permissão para usar essa animação.");
                return;
            }
            p.closeInventory();
            animation.start(p);
            p.sendMessage(ChatColor.GOLD + "Você agora está usando a animação " + animation.getName() + ".");
        }
    }
}
