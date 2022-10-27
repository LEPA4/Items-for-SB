package sbitems.skyblockitems;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sbitems.skyblockitems.events.RegisterEvents;
import sbitems.skyblockitems.items.ItemManager;
import sbitems.skyblockitems.items.LunchBox;

public final class SkyblockItems extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Registers the event handlers
        getServer().getPluginManager().registerEvents(new RegisterEvents(), this);
        getServer().getPluginManager().registerEvents(new LunchBox(), this);

        // Basically reads and adds the items --> calls the class that calls the class that creates items.
        ItemManager.init();
    }

    @Override
    public void onDisable() {
        // IDK what this does. I just added it bc it sounded cool.
        HandlerList.unregisterAll((Plugin) this);
    }
}
