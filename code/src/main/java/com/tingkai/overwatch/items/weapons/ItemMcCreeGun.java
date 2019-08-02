package com.tingkai.overwatch.items.weapons;

import com.tingkai.overwatch.Overwatch;
import com.tingkai.overwatch.entities.EntityMcCreeBullet;
import com.tingkai.overwatch.entities.EntityModThrowable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemMcCreeGun extends ItemModWeapon
{
	public ItemMcCreeGun()
	{
		super(30);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 20;
	}

	@Override
	public void onItemLeftClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		if (this.canUse(player, true))
		{
			if (!world.isRemote)
			{
				EntityMcCreeBullet bullet = new EntityMcCreeBullet(world, player);
				bullet.setAim(player, player.rotationPitch, player.rotationYaw, 5.0F, 0.3F, 0F, hand, true);
				world.spawnEntity(bullet);

				this.subtractFromCurrentAmmo(player, 1, hand);
				if (!player.getCooldownTracker().hasCooldown(this))
				{
					player.getCooldownTracker().setCooldown(this, 10);
				}
				if (world.rand.nextInt(6) == 0)
				{
					player.getHeldItem(hand).damageItem(1, player);
				}
			}
			else
			{
				Vec3d vec = EntityModThrowable.getShootingPos(player, player.rotationPitch, player.rotationYaw, hand);
				Overwatch.proxy.spawnParticlesSpark(world, vec.xCoord, vec.yCoord, vec.zCoord, 0xFFEF89, 0x5A575A, 5, 1);
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
	{
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase entity, int count)
	{
		if (entity instanceof EntityPlayer && count % 2 == 0 && this.canUse((EntityPlayer) entity, true))
		{
			EnumHand hand = null;
			for (EnumHand hand2 : EnumHand.values())
			{
				if (((EntityPlayer) entity).getHeldItem(hand2) == stack)
				{
					hand = hand2;
				}
			}
			if (!entity.world.isRemote && hand != null)
			{
				EntityMcCreeBullet bullet = new EntityMcCreeBullet(entity.world, entity);
				bullet.setAim((EntityPlayer) entity, entity.rotationPitch, entity.rotationYaw, 5.0F, 1.5F, 1F, hand, true);
				entity.world.spawnEntity(bullet);
				if (count == this.getMaxItemUseDuration(stack))
				{
					this.subtractFromCurrentAmmo((EntityPlayer) entity, 1, hand);
				}
				else
				{
					this.subtractFromCurrentAmmo((EntityPlayer) entity, 1);
				}
				if (entity.world.rand.nextInt(25) == 0)
				{
					entity.getHeldItem(hand).damageItem(1, entity);
				}
			}
			else if (hand != null)
			{
				entity.rotationPitch--;
				Vec3d vec = EntityModThrowable.getShootingPos(entity, entity.rotationPitch, entity.rotationYaw, hand);
				Overwatch.proxy.spawnParticlesSpark(entity.world, vec.xCoord, vec.yCoord, vec.zCoord, 0xFFEF89, 0x5A575A, 5, 1);
			}
		}
	}
}
