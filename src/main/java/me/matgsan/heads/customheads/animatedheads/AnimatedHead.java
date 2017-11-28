package me.matgsan.heads.customheads.animatedheads;

import java.util.List;
import java.util.Random;
import me.matgsan.heads.Head;
import me.matgsan.heads.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Matheus Santos (Matgsan)
 */
public class AnimatedHead {

    private final String name;
    private final String permission;
    private final List<String> textures;
    private transient List<Head> frames;
    private final int interval;

    public AnimatedHead(String name, String permission, List<String> textures, int interval) {
        this.name = name;
        this.permission = permission;
        this.textures = textures;
        this.interval = interval;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getTextures() {
        return textures;
    }

    public List<Head> getFrames() {
        return frames;
    }

    public void setFrames(List<Head> frames) {
        this.frames = frames;
    }

    public int getInterval() {
        return interval;
    }

    public Head getRandomFrame() {
        List<Head> randomFrame = getFrames();
        Random random = new Random();
        int index = random.nextInt(randomFrame.size());
        return randomFrame.get(index);
    }

    public void start(Player p) {
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            int index = 0;
            int times = 2;

            @Override
            public void run() {
                if (times == 0) {
                    this.cancel();
                    p.getInventory().setHelmet(null);
                    return;
                }
                Head head = getFrames().get(index);
                p.getInventory().setHelmet(head.getHeadItem());
                index++;
                if (index >= getFrames().size()) {
                    index = 0;
                    times--;
                }
            }
        };
        bukkitRunnable.runTaskTimerAsynchronously(Main.getInstance(), 0L, interval);
    }

    public static AnimatedHead getAnimatedHeadByDisplayName(String displayName) {
        displayName = ChatColor.stripColor(displayName);
        for (AnimatedHead animatedHead : AnimatedHeadManager.getAnimatedHeadList()) {
            if (animatedHead.getName().equalsIgnoreCase(displayName)) {
                return animatedHead;
            }
        }
        return null;
    }
}
