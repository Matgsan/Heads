package me.matgsan.heads.comandos;

import me.matgsan.heads.customheads.animatedheads.AnimatedHeadManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Matheus Santos (Matgsan)
 */
public class AnimatedHeadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        AnimatedHeadManager.openAnimatedHeadsInventory(p);
        return true;
    }

}
