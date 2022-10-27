package sbitems.skyblockitems.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

        // Adds items, i dont know what to say abt this
        addItem("Wand", createItem.MakeItem("Wand", Material.ARROW, 1, null), 1);
        addItem("Bacon", createItem.FoodItem(createItem.MakeItem("Bacon", Material.PORKCHOP, 2, null), 9, 9, lore), null);
        addItem("Lunch Box", getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTk2NzQ4MDgyZmYzYjE3ODMyY2M1OTUyYTRhOTdmYjljNjE0Yjc2YjdmYzNhODFiYjYzNDU0ZjI4OWZjZWYifX19", "Lunch Box"), 1);
    }

    public static void addItem(String name, ItemStack item, Integer maxStack){
        // Adds the item to maps
        CustomItems.put(name, item);
        CustomItemsName.put(item, name);
        if(maxStack != null)
            CustomItemsStackSize.put(item, maxStack);
    }

    public static ItemStack getHead(String texture, String name) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        GameProfile gameProfile = new GameProfile(null, name);

        gameProfile.getProperties().put("textures", new Property("textures", texture));
        try {
            assert skullMeta != null;
            Method method = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            method.setAccessible(true);
            method.invoke(skullMeta, gameProfile);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        skullMeta.setDisplayName(name);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

}
