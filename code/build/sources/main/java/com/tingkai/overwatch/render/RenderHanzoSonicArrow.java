package com.tingkai.overwatch.render;

import com.tingkai.overwatch.Overwatch;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderHanzoSonicArrow extends RenderHanzoArrow {
	public RenderHanzoSonicArrow(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation(Overwatch.MODID, "textures/entity/hanzo_sonic_arrow.png");
	}
}
