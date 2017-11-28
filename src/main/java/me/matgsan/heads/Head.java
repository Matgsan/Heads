package me.matgsan.heads;

import com.mojang.authlib.GameProfile;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Matheus Santos (Matgsan)
 */
public abstract class Head {

    private final String texture;
    private transient GameProfile profile;
    private transient ItemStack headItem;

    public Head(String texture) {
        this.texture = texture;
    }

    public String getTexture() {
        return texture;
    }

    public GameProfile getGameProfile() {
        return profile;
    }

    public void setGameProfile(GameProfile profile) {
        this.profile = profile;
    }

    public ItemStack getHeadItem() {
        return headItem;
    }

    public void setHeadItem(ItemStack headItem) {
        this.headItem = headItem;
    }
}
