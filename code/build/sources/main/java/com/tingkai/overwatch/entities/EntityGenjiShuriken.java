package com.tingkai.overwatch.entities;

import com.tingkai.overwatch.Overwatch;
import com.tingkai.overwatch.items.weapons.ItemModWeapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityGenjiShuriken extends EntityModThrowable
{

	public EntityGenjiShuriken(World worldIn)
	{
		super(worldIn);
		this.setSize(0.1f, 0.1f);
	}

	public EntityGenjiShuriken(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
		this.setNoGravity(true);
		this.lifetime = 40;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.world.isRemote)
		{
			int numParticles = (int) ((Math.abs(motionX) + Math.abs(motionY) + Math.abs(motionZ)) * 30d);
			for (int i = 0; i < numParticles; ++i)
			{
				Overwatch.proxy.spawnParticlesTrail(this.world, this.posX + (this.prevPosX - this.posX) * i / numParticles + world.rand.nextDouble() * 0.05d, this.posY + this.height / 2 + (this.prevPosY - this.posY) * i / numParticles + world.rand.nextDouble() * 0.05d, this.posZ + (this.prevPosZ - this.posZ) * i / numParticles + world.rand.nextDouble() * 0.05d, 0, 0, 0, 0xC8E682, 0x709233, 0.5f, 4, 1);
			}
		}
	}

	@Override
	protected void onImpact(RayTraceResult result)
	{
		super.onImpact(result);

		if (this.getThrower() instanceof EntityPlayer && result.entityHit != this.getThrower())
		{
			if (result.entityHit instanceof EntityLivingBase && ((EntityLivingBase) result.entityHit).getHealth() > 0 && !this.world.isRemote)
			{
				((EntityLivingBase) result.entityHit).attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) this.getThrower()), 28F * ItemModWeapon.damageScale);
				((EntityLivingBase) result.entityHit).hurtResistantTime = 0;
			}

			Overwatch.proxy.spawnParticlesSpark(world, result.entityHit == null ? result.hitVec.xCoord : posX, result.entityHit == null ? result.hitVec.yCoord : posY, result.entityHit == null ? result.hitVec.zCoord : posZ, 0xC8E682, 0x709233, 5, 5);

			this.setDead();
		}
	}
}
