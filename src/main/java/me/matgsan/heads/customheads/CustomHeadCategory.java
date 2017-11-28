package me.matgsan.heads.customheads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
public enum CustomHeadCategory {

    ALPHABET("Alfabeto"),
    BLOCKS("Blocos e Objetos"),
    FOOD("Comida e Bebidas"),
    COLORS("Cores"),
    SPECIALS("Especiais"),
    MOBS("Mobs"),
    CHARACTERS("Personagens e Jogos"),
    MISC("Diversos");
    private final String name;

    private CustomHeadCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public List<CustomHead> getHeads() {
        List<CustomHead> heads = new ArrayList();
        for (CustomHead head : CustomHeadManager.getCustomHeadList()) {
            if (head.getCategory() == this) {
                heads.add(head);
            }
        }
        return heads;
    }

    public CustomHead getRandomHead() {
        List<CustomHead> heads = getHeads();
        if (heads.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(heads.size());
        return heads.get(index);
    }

    public static CustomHeadCategory getCustomHeadCategoryByDisplayName(String displayName) {
        displayName = ChatColor.stripColor(displayName);
        for (final CustomHeadCategory category : values()) {
            if (category.getName().equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        return null;
    }

    public static void openInventory(Player p, CustomHeadCategory category) {
        openInventory(p, category, 1);
    }

    public static void openInventory(Player p, CustomHeadCategory category, int page) {
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
        if (endIndex > category.getHeads().size()) {
            endIndex = category.getHeads().size();
        } else {
            canGoFoward = true;
        }
        int startSlot = 0;
        Inventory inventory = Bukkit.createInventory(null, inventorysize, "Cabeças - " + category.getName());
        for (CustomHead head : category.getHeads().subList(startIndex, endIndex)) {
            ItemStack headItem = head.getHeadItem();
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
        exitMeta.setDisplayName(ChatColor.RED + "Voltar para o menu");
        exit.setItemMeta(exitMeta);
        inventory.setItem((inventorysize - 9), exit);
        new InventoryUtil(p, inventory).openInventory();
    }

    public static void onHeadClick(InventoryClickEvent event) {
        final ItemStack item = event.getCurrentItem();
        final Player p = (Player) event.getWhoClicked();
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String itemDisplayName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            if (item.getType() == Material.BARRIER) {
                CustomHeadManager.openCustomHeadsMenu(p);
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
                ItemStack firstItem = event.getInventory().getContents()[0];
                if (firstItem == null || !firstItem.hasItemMeta() || !firstItem.getItemMeta().hasDisplayName()) {
                    p.sendMessage(ChatColor.RED + "Houve algum problema ao identificar o que você escolheu, tente mais tarde.");
                    return;
                }
                CustomHead head = CustomHead.getCustomHeadByDisplayName(firstItem.getItemMeta().getDisplayName());
                if (head == null) {
                    p.sendMessage(ChatColor.RED + "Houve algum problema ao identificar a categoria que você está, tente mais tarde.");
                    return;
                }
                openInventory(p, head.getCategory(), pagina);
                return;
            }
            CustomHead head = CustomHead.getCustomHeadByDisplayName(item.getItemMeta().getDisplayName());
            if (head == null) {
                p.closeInventory();
                p.sendMessage(ChatColor.RED + "Houve algum problema ao identificar a cabeça que você escolheu, tente mais tarde.");
                return;
            }
            if (!p.hasPermission(head.getPermission())) {
                p.closeInventory();
                p.sendMessage(ChatColor.RED + "Você não tem permissão para usar essa cabeça.");
                return;
            }
            p.closeInventory();
            p.getInventory().setHelmet(head.getHeadItem());
            p.sendMessage(ChatColor.GOLD + "Você agora está usando a cabeça " + head.getName() + ".");
        }
    }
}
