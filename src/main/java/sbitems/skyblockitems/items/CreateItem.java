package sbitems.skyblockitems.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class CreateItem {

    public ItemStack MakeItem(String name, Material material, Integer modelData, @Nullable ArrayList<String> lore){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + name);
        if(modelData != null)
            meta.setCustomModelData(modelData);
        if(lore != null)
            meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }



}
