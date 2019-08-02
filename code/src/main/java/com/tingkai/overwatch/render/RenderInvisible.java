package com.tingkai.overwatch.render;

import com.tingkai.overwatch.Overwatch;
import com.tingkai.overwatch.entities.EntityModThrowable;
import com.tingkai.overwatch.entities.ModEntities;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderInvisible extends Render<EntityModThrowable>
{
	public RenderInvisible(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityModThrowable entity)
	{
		return new ResourceLocation(Overwatch.MODID, "");
	}

	@Override
	public void doRender(EntityModThrowable entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if (entity.ticksExisted == 0 && entity.getPersistentID().equals(ModEntities.spawningEntityUUID))
		{
			entity.updateFromPacket();
		}
	}
}
