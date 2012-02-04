package me.lyneira.MachinaBuilder;

import java.util.logging.Logger;

import me.lyneira.MachinaCore.ConfigurationManager;
import me.lyneira.MachinaCore.MachinaCore;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main Plugin.
 * 
 * @author Lyneira
 */
public class MachinaBuilder extends JavaPlugin {
    final static Logger log = Logger.getLogger("Minecraft");
    static PluginManager pluginManager;

    public final void onEnable() {
        pluginManager = this.getServer().getPluginManager();
        PluginDescriptionFile pdf = getDescription();
        log.info(pdf.getName() + " version " + pdf.getVersion() + " is now enabled.");
        
        ConfigurationManager config = new ConfigurationManager(this);
        Builder.loadConfiguration(config.getAll());

        MachinaCore.plugin.registerBlueprint(Blueprint.instance);
    }

    public final void onDisable() {
        PluginDescriptionFile pdf = getDescription();
        log.info(pdf.getName() + " is now disabled.");

        MachinaCore.plugin.unRegisterBlueprint(Blueprint.instance);
    }
}
