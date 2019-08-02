package com.tingkai.overwatch.entities;

import com.tingkai.overwatch.Overwatch;
import com.tingkai.overwatch.items.weapons.ItemModWeapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityAnaBullet extends EntityModThrowable
{
	private static final DataParameter<Boolean> HEAL = EntityDataManager.<Boolean> createKey(EntityAnaBullet.class, DataSerializers.BOOLEAN);

	public EntityAnaBullet(World worldIn)
	{
		super(worldIn);
		this.setSize(0.1f, 0.1f);
	}

	public EntityAnaBullet(World worldIn, EntityLivingBase throwerIn, boolean heal)
	{
		super(worldIn, throwerIn);
		this.getDataManager().set(HEAL, heal);
		this.setNoGravity(true);
		this.lifetime = 40;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(HEAL, false);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.world.isRemote && this.ticksExisted > 1)
		{
			int numParticles = (int) ((Math.abs(motionX) + Math.abs(motionY) + Math.abs(motionZ)) * 30d);
			for (int i = 0; i < numParticles; ++i)
			{
				Overwatch.proxy.spawnParticlesTrail(this.world, this.posX + (this.prevPosX - this.posX) * i / numParticles + world.rand.nextDouble() * 0.05d, this.posY + (this.prevPosY - this.posY) * i / numParticles + world.rand.nextDouble() * 0.05d, this.posZ + (this.prevPosZ - this.posZ) * i / numParticles + world.rand.nextDouble() * 0.05d, 0, 0, 0, 0xFFFCC7, 0xEAE7B9, 0.5f, 8, 1);
			}
		}
	}

	@Override
	protected void onImpact(RayTraceResult result)
	{
		super.onImpact(result);

		if (result.entityHit instanceof EntityLivingBase && this.getThrower() instanceof EntityPlayer && result.entityHit != this.getThrower() && ((EntityLivingBase) result.entityHit).getHealth() > 0)
		{
			if (!this.world.isRemote)
			{
				if (this.getDataManager().get(HEAL))
				{
					((EntityLivingBase) result.entityHit).heal(75 * ItemModWeapon.damageScale);
					((WorldServer) result.entityHit.world).spawnParticle(EnumParticleTypes.HEART, result.entityHit.posX + 0.5d, result.entityHit.posY + 0.5d, result.entityHit.posZ + 0.5d, 10, 0.4d, 0.4d, 0.4d, 0d, new int[0]);
				}
				else
				{
					((EntityLivingBase) result.entityHit).attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) this.getThrower()), 60F * ItemModWeapon.damageScale);
				}
			}
			this.setDead();
		}
	}
}
