package me.matgsan.heads.customheads;

import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.Random;
import me.matgsan.heads.Head;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Matheus Santos (Matgsan)
 */
public class CustomHead extends Head {

    private final String name;
    private final String permission;
    private final CustomHeadCategory category;
    private transient GameProfile profile;
    private transient ItemStack headItem;

    public CustomHead(String name, CustomHeadCategory category, String texture, String permission) {
        super(texture);
        this.name = name;
        this.category = category;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public CustomHeadCategory getCategory() {
        return category;
    }

    public static CustomHead getRandomCustomHead() {
        List<CustomHead> heads = CustomHeadManager.getCustomHeadList();
        Random random = new Random();
        int index = random.nextInt(heads.size());
        return heads.get(index);
    }

    public static CustomHead getCustomHeadByDisplayName(String displayName) {
        displayName = ChatColor.stripColor(displayName);
        for (CustomHead customHead : CustomHeadManager.getCustomHeadList()) {
            if (customHead.getName().equalsIgnoreCase(displayName)) {
                return customHead;
            }
        }
        return null;
    }
}
