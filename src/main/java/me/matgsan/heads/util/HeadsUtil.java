package me.matgsan.heads.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;
import me.matgsan.heads.Head;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 *
 * @author Matheus Santos (Matgsan)
 */
public class HeadsUtil {

    public static GameProfile getGameProfile(Head head) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedURL = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", head.getTexture()).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedURL)));
        return profile;
    }

    public static ItemStack getHeadItem(Head head) {
        ItemStack headItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) headItem.getItemMeta();
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, head.getGameProfile());
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        headItem.setItemMeta(headMeta);
        return headItem;
    }
}
