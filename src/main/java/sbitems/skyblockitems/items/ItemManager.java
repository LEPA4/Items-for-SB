package sbitems.skyblockitems.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemManager {

    public static void init(){
        CreateItem createItem = new CreateItem();
        addItem("Wand", createItem.MakeItem("Wand", Material.ARROW), 1);
    }

    public static Map<String, ItemStack> CustomItems = new HashMap<>();
    public static Map<ItemStack, String> CustomItemsName = new HashMap<>();
    public static Map<ItemStack, Integer> CustomItemsStackSize = new HashMap<>();

    public static void addItem(String name, ItemStack item, Integer maxStack){
        CustomItems.put(name, item);
        CustomItemsName.put(item, name);
        CustomItemsStackSize.put(item, maxStack);
    }



}