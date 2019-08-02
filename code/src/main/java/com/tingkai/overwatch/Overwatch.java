package com.tingkai.overwatch;

import java.io.File;

import com.tingkai.overwatch.command.CommandDev;
import com.tingkai.overwatch.command.CommandOverwatch;
import com.tingkai.overwatch.key.Keys;
import com.tingkai.overwatch.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Overwatch.MODID, version = Overwatch.VERSION, name = Overwatch.MODNAME, guiFactory = "com.tingkai.overwatch.gui.GuiFactory")
public class Overwatch
{
	public static final String MODNAME = "Overwatch";
	public static final String MODID = "overwatch";
	public static final String VERSION = "1.11.2";
	@Mod.Instance(MODID)
	public static Overwatch instance;
	public static OverwatchTab tab = new OverwatchTab("tabOverwatch");
	public static final String CLIENT_PROXY = "com.tingkai.overwatch.proxy.ClientProxy";
	public static final String SERVER_PROXY = "com.tingkai.overwatch.proxy.CommonProxy";
	@SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
	public static CommonProxy proxy;
	public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	public static final Keys keys = new Keys();
	public static File configFile;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandOverwatch());
		event.registerServerCommand(new CommandDev());
	}
}
