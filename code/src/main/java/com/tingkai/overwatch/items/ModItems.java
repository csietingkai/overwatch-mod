package com.tingkai.overwatch.items;

import java.util.ArrayList;

import com.tingkai.overwatch.Config;
import com.tingkai.overwatch.Overwatch;
import com.tingkai.overwatch.hero.EnumHero;
import com.tingkai.overwatch.items.armors.ItemModArmor;
import com.tingkai.overwatch.items.weapons.ItemGenjiShuriken;
import com.tingkai.overwatch.items.weapons.ItemModWeapon;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems
{
	public static ArrayList<Item> jsonModelItems = new ArrayList<Item>();
	public static ArrayList<Item> objModelItems = new ArrayList<Item>();

	public static Item genji_shuriken_single; // used for projectile
	
	public static Item logo; // use for logo

	public static void preInit()
	{
		for (EnumHero hero : EnumHero.values())
		{
			hero.material = EnumHelper.addArmorMaterial(hero.name.toLowerCase(), Overwatch.MODNAME + ":" + hero.name.toLowerCase(), 20, hero.armorReductionAmounts, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
			hero.helmet = (ItemModArmor) registerItem(new ItemModArmor(hero, hero.material, 0, EntityEquipmentSlot.HEAD), hero.name.toLowerCase() + "_helmet", true, false);
			hero.chestplate = (ItemModArmor) registerItem(new ItemModArmor(hero, hero.material, 0, EntityEquipmentSlot.CHEST), hero.name.toLowerCase() + "_chestplate", true, false);
			hero.leggings = (ItemModArmor) registerItem(new ItemModArmor(hero, hero.material, 0, EntityEquipmentSlot.LEGS), hero.name.toLowerCase() + "_leggings", true, false);
			hero.boots = (ItemModArmor) registerItem(new ItemModArmor(hero, hero.material, 0, EntityEquipmentSlot.FEET), hero.name.toLowerCase() + "_boots", true, false);
			hero.weapon = (ItemModWeapon) registerItem(hero.weapon, hero.name.toLowerCase() + "_weapon", true, true);
			hero.token = (ItemModToken) registerItem(new ItemModToken(), hero.name.toLowerCase() + "_token", true, false);
		}

		genji_shuriken_single = registerItem(new ItemGenjiShuriken(), "genji_shuriken_single", false, true);
		((ItemGenjiShuriken) genji_shuriken_single).hero = EnumHero.GENJI;
		
		logo = registerItem(new Logo(), "logo", false, false);
	}

	private static Item registerItem(Item item, String unlocalizedName, boolean addToTab, boolean usesObjModel)
	{
		if (usesObjModel && Config.useObjModels)
		{
			objModelItems.add(item);
		}
		else
		{
			jsonModelItems.add(item);
		}
		item.setUnlocalizedName(unlocalizedName);
		item.setRegistryName(Overwatch.MODID, unlocalizedName);
		if (addToTab)
		{
			item.setCreativeTab(Overwatch.tab);
			Overwatch.tab.orderedStacks.add(new ItemStack(item));
		}
		GameRegistry.register(item);
		return item;
	}
}