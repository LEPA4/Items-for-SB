package sbitems.skyblockitems.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {

    public static Map<String, ItemStack> CustomItems = new HashMap<>();
    public static Map<ItemStack, String> CustomItemsName = new HashMap<>();
    public static Map<ItemStack, Integer> CustomItemsStackSize = new HashMap<>();
    public static Map<ItemStack, Integer> FoodSaturation = new HashMap<>();
    public static Map<ItemStack, ArrayList<PotionEffect>> FoodEffect = new HashMap<>();
    public static Map<ItemStack, Integer> FoodBars = new HashMap<>();


    public static void init(){
        CreateItem createItem = new CreateItem();
        ArrayList<PotionEffect> lore = new ArrayList<>();
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.SPEED, 15*20, 7);
        lore.add(potionEffect);

        addItem("Wand", createItem.MakeItem("Wand", Material.ARROW, 1, null), 1);
        addItem("Bacon", createItem.FoodItem(createItem.MakeItem("Bacon", Material.PORKCHOP, 2, null), 9, 9, lore), null);
    }

    public static void addItem(String name, ItemStack item, Integer maxStack){
        CustomItems.put(name, item);
        CustomItemsName.put(item, name);
        if(maxStack != null)
            CustomItemsStackSize.put(item, maxStack);
    }



}
