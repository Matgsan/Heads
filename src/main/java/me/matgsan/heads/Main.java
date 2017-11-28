package me.matgsan.heads;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import me.matgsan.heads.comandos.AnimatedHeadCommand;
import me.matgsan.heads.comandos.CustomHeadCommand;
import me.matgsan.heads.customheads.CustomHead;
import me.matgsan.heads.customheads.CustomHeadManager;
import me.matgsan.heads.customheads.animatedheads.AnimatedHead;
import me.matgsan.heads.customheads.animatedheads.AnimatedHeadManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Matheus Santos (Matgsan)
 */
public class Main extends JavaPlugin {

    private static Main instance;
    private static Gson gson;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().log(Level.INFO, "Baixando cabeças...");
        gson = new GsonBuilder().setPrettyPrinting().create();
        registerCommands();
        registerListeners();
        loadHeads();
        loadAnimations();
    }

    public static Gson getGson() {
        return gson;
    }

    public static Main getInstance() {
        return instance;
    }

    private void registerCommands() {
        this.getCommand("animatedhead").setExecutor(new AnimatedHeadCommand());
        this.getCommand("customhead").setExecutor(new CustomHeadCommand());
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new Listeners(), this);
    }

    private void loadHeads() {
        Type listType = new TypeToken<List<CustomHead>>() {
        }.getType();
        InputStream in = getClass().getResourceAsStream("/heads.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        List<CustomHead> headsList = getGson().fromJson(reader, listType);
        System.out.println(headsList.size() + " cabeças carregadas.");
        CustomHeadManager.setCustomHeadList(headsList);
    }

    private void loadAnimations() {
        Type listType = new TypeToken<List<AnimatedHead>>() {
        }.getType();
        InputStream in = getClass().getResourceAsStream("/animations.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        List<AnimatedHead> headsList = getGson().fromJson(reader, listType);
        System.out.println(headsList.size() + " animações carregadas.");
        AnimatedHeadManager.setAnimatedHeads(headsList);
    }
}
