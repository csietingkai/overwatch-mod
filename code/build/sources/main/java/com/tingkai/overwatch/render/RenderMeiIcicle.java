package com.tingkai.overwatch.render;

import com.tingkai.overwatch.Overwatch;
import com.tingkai.overwatch.entities.EntityMeiIcicle;
import com.tingkai.overwatch.entities.ModEntities;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMeiIcicle extends RenderOBJModel<EntityMeiIcicle>
{
	public RenderMeiIcicle(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMeiIcicle entity)
	{
		return new ResourceLocation(Overwatch.MODID, "textures/entity/mei_icicle.png");
	}

	@Override
	protected ResourceLocation getEntityModel()
	{
		return new ResourceLocation(Overwatch.MODID, "entity/mei_icicle.obj");
	}

	@Override
	protected void preRender(EntityMeiIcicle entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if (entity.ticksExisted == 0 && entity.getPersistentID().equals(ModEntities.spawningEntityUUID))
		{
			entity.updateFromPacket();
		}

		GlStateManager.translate(0, 0.06d, 0.5d);
		GlStateManager.scale(2, 2, 2);
	}
}
