package me.matgsan.heads.comandos;

import me.matgsan.heads.customheads.CustomHeadManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Matheus Santos (Matgsan)
 */
public class CustomHeadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        CustomHeadManager.openCustomHeadsMenu(p);
        return true;
    }

}
