package sbitems.skyblockitems.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class CreateItem {

    public ItemStack MakeItem(String name, Material material, Integer modelData, @Nullable ArrayList<String> lore){
        // Creates the item and returns it
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

    public ItemStack FoodItem(ItemStack item, Integer saturation, Integer hunger, @Nullable ArrayList<PotionEffect> effects){
        // Adds the food values to the list and returns the newly created item
        if(effects != null)
            if(!ItemManager.FoodEffect.containsKey(item))
                ItemManager.FoodEffect.put(item, effects);
            else ItemManager.FoodEffect.replace(item, effects);
        if(!ItemManager.FoodSaturation.containsKey(item))
            ItemManager.FoodSaturation.put(item, saturation);
        else ItemManager.FoodSaturation.replace(item, saturation);
        if(!ItemManager.FoodBars.containsKey(item))
            ItemManager.FoodBars.put(item, hunger);
        else ItemManager.FoodBars.replace(item, hunger);

        return item;
    }



}
