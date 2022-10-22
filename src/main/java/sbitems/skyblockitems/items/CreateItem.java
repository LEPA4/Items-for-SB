package sbitems.skyblockitems.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;

public class CreateItem {

    public ItemStack MakeItem(String name, Material material){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + name);
        item.setItemMeta(meta);

        return item;
    }



}
