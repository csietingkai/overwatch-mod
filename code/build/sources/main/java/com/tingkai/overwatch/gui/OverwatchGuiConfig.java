package com.tingkai.overwatch.gui;

import java.util.ArrayList;
import java.util.List;

import com.tingkai.overwatch.Config;
import com.tingkai.overwatch.Overwatch;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class OverwatchGuiConfig extends GuiConfig
{
	public OverwatchGuiConfig(GuiScreen parent)
	{
		super(parent, getConfigElements(), Overwatch.MODID, false, false, Overwatch.MODNAME + " Configuration");
	}

	private static List<IConfigElement> getConfigElements()
	{
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		list.add(new ConfigElement(Config.config.getCategory(Config.CATEGORY_HERO_TEXTURES).setLanguageKey(Config.CATEGORY_HERO_TEXTURES)));
		list.addAll(new ConfigElement(Config.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
		return list;
	}
}