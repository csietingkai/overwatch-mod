package com.tingkai.overwatch;

import com.tingkai.overwatch.items.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OverwatchTab extends CreativeTabs
{
	public NonNullList<ItemStack> orderedStacks = NonNullList.create();

	public OverwatchTab(String label)
	{
		super(label);
	}

	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(ModItems.logo);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(NonNullList<ItemStack> list)
	{
		list.clear();
		list.addAll(orderedStacks);
	}
}