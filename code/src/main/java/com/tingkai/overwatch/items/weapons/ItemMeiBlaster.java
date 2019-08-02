package com.tingkai.overwatch.items.weapons;

import com.tingkai.overwatch.Overwatch;
import com.tingkai.overwatch.entities.EntityMeiBlast;
import com.tingkai.overwatch.entities.EntityMeiIcicle;
import com.tingkai.overwatch.entities.EntityModThrowable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemMeiBlaster extends ItemModWeapon
{
	public ItemMeiBlaster()
	{
		super(30);
	}

	@Override
	public void onItemLeftClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		if (this.canUse(player, true) && !world.isRemote)
		{
			EntityMeiBlast bullet = new EntityMeiBlast(world, player);
			bullet.setAim(player, player.rotationPitch, player.rotationYaw, 2F, 0.3F, 2.5F, hand, false);
			world.spawnEntity(bullet);
			this.subtractFromCurrentAmmo(player, 1);
			if (world.rand.nextInt(200) == 0)
			{
				player.getHeldItem(hand).damageItem(1, player);
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		// shoot
		if (this.canUse(player, true))
		{
			if (!world.isRemote)
			{
				EntityMeiIcicle icicle = new EntityMeiIcicle(world, player);
				icicle.setAim(player, player.rotationPitch, player.rotationYaw, 2F, 0.2F, 0F, hand, false);
				world.spawnEntity(icicle);
				if (!player.getCooldownTracker().hasCooldown(this))
				{
					player.getCooldownTracker().setCooldown(this, 24);
				}
				if (world.rand.nextInt(8) == 0)
				{
					player.getHeldItem(hand).damageItem(1, player);
				}
				this.subtractFromCurrentAmmo(player, 25, hand);
			}
			else
			{
				Vec3d vec = EntityModThrowable.getShootingPos(player, player.rotationPitch, player.rotationYaw, hand);
				Overwatch.proxy.spawnParticlesSpark(world, vec.xCoord, vec.yCoord, vec.zCoord, 0x2B9191, 0x2B9191, 3, 3);
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}
}