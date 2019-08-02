package com.tingkai.overwatch.render;

import com.tingkai.overwatch.Overwatch;
import com.tingkai.overwatch.entities.EntityTracerBullet;
import com.tingkai.overwatch.entities.ModEntities;
import com.tingkai.overwatch.model.ModelTracerBullet;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderTracerBullet extends Render<EntityTracerBullet>
{
	private final ModelTracerBullet TRACER_BULLET_MODEL = new ModelTracerBullet();

	public RenderTracerBullet(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTracerBullet entity)
	{
		return new ResourceLocation(Overwatch.MODID, "textures/entity/tracer_bullet.png");
	}

	@Override
	public void doRender(EntityTracerBullet entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if (entity.ticksExisted == 0 && entity.getPersistentID().equals(ModEntities.spawningEntityUUID))
		{
			entity.updateFromPacket();
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.scale(0.1F, 0.1F, 0.1F);
		GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-entity.rotationPitch, 1.0F, 0.0F, 0.0F);
		this.bindEntityTexture(entity);
		this.TRACER_BULLET_MODEL.render(entity, 0, 0, 0, 0, entity.rotationPitch, 0.5f);
		GlStateManager.popMatrix();
	}
}
