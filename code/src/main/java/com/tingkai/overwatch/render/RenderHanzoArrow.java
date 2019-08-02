package com.tingkai.overwatch.render;

import com.tingkai.overwatch.Overwatch;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("rawtypes") // prevent warning
public class RenderHanzoArrow extends RenderArrow
{
	public RenderHanzoArrow(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return new ResourceLocation(Overwatch.MODID, "textures/entity/hanzo_arrow.png");
	}

}
