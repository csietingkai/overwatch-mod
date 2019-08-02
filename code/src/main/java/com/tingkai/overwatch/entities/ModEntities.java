package com.tingkai.overwatch.entities;

import java.util.UUID;

import com.tingkai.overwatch.Overwatch;
import com.tingkai.overwatch.messages.SPacketSyncSpawningEntity;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities
{
	public static UUID spawningEntityUUID;
	public static SPacketSyncSpawningEntity spawningEntityPacket;

	public static void registerEntities()
	{
		int id = 0;
		EntityRegistry.registerModEntity(new ResourceLocation(Overwatch.MODID, "reaper_pellet"), EntityReaperBullet.class, "reaper_pellet", id++, Overwatch.instance, 64, 20, false);
		EntityRegistry.registerModEntity(new ResourceLocation("arrow"), EntityHanzoArrow.class, "hanzo_arrow", id++, Overwatch.instance, 64, 20, false);
		EntityRegistry.registerModEntity(new ResourceLocation("arrow"), EntityHanzoSonicArrow.class, "hanzo_sonic_arrow", id++, Overwatch.instance, 64, 20, false);
		EntityRegistry.registerModEntity(new ResourceLocation("arrow"), EntityHanzoScatterArrow.class, "hanzo_scatter_arrow", id++, Overwatch.instance, 64, 1, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Overwatch.MODID, "ana_bullet"), EntityAnaBullet.class, "ana_bullet", id++, Overwatch.instance, 64, 20, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Overwatch.MODID, "genji_shuriken"), EntityGenjiShuriken.class, "genji_shuriken", id++, Overwatch.instance, 64, 20, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Overwatch.MODID, "tracer_bullet"), EntityTracerBullet.class, "tracer_bullet", id++, Overwatch.instance, 64, 20, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Overwatch.MODID, "mccree_bullet"), EntityMcCreeBullet.class, "mccree_bullet", id++, Overwatch.instance, 64, 20, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Overwatch.MODID, "soldier76_bullet"), EntitySoldier76Bullet.class, "soldier76_bullet", id++, Overwatch.instance, 64, 20, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Overwatch.MODID, "soldier76_helix_rocket"), EntitySoldier76HelixRocket.class, "soldier76_helix_rocket", id++, Overwatch.instance, 64, 20, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Overwatch.MODID, "bastion_bullet"), EntityBastionBullet.class, "bastion_bullet", id++, Overwatch.instance, 64, 20, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Overwatch.MODID, "mei_blast"), EntityMeiBlast.class, "mei_blast", id++, Overwatch.instance, 64, 20, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Overwatch.MODID, "mei_icicle"), EntityMeiIcicle.class, "mei_icicle", id++, Overwatch.instance, 64, 20, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Overwatch.MODID, "widowmaker_bullet"), EntityWidowmakerBullet.class, "widowmaker_bullet", id++, Overwatch.instance, 64, 20, false);
	}
}
