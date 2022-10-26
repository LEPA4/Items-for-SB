package sbitems.skyblockitems;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sbitems.skyblockitems.events.RegisterEvents;
import sbitems.skyblockitems.items.ItemManager;

public final class SkyblockItems extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new RegisterEvents(), this);
        ItemManager.init();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
    }
}
