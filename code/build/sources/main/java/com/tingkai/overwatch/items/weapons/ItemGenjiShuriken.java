package com.tingkai.overwatch.items.weapons;

import com.tingkai.overwatch.entities.EntityGenjiShuriken;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemGenjiShuriken extends ItemModWeapon
{
	public ItemGenjiShuriken()
	{
		super(40);
	}

	@Override
	public void onItemLeftClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		if (!player.world.isRemote && this.canUse(player, true) && player.ticksExisted % 3 == 0)
		{
			EntityGenjiShuriken shuriken = new EntityGenjiShuriken(player.world, player);
			shuriken.setAim(player, player.rotationPitch, player.rotationYaw, 3F, 1.0F, 1F, hand, false);
			player.world.spawnEntity(shuriken);
			this.subtractFromCurrentAmmo(player, 1, hand);
			if (!player.getCooldownTracker().hasCooldown(this) && this.getCurrentAmmo(player) % 3 == 0 && this.getCurrentAmmo(player) != this.getMaxAmmo(player))
			{
				player.getCooldownTracker().setCooldown(this, 15);
			}
			if (player.world.rand.nextInt(24) == 0)
			{
				player.getHeldItem(hand).damageItem(1, player);
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (!player.world.isRemote && this.canUse(player, true))
		{
			for (int i = 0; i < Math.min(3, this.getCurrentAmmo(player)); i++)
			{
				EntityGenjiShuriken shuriken = new EntityGenjiShuriken(player.world, player);
				shuriken.setAim(player, player.rotationPitch, player.rotationYaw + (1 - i) * 8, 3F, 1.0F, 0F, hand, false);
				player.world.spawnEntity(shuriken);
			}
			this.subtractFromCurrentAmmo(player, 3, hand);
			if (world.rand.nextInt(8) == 0)
			{
				player.getHeldItem(hand).damageItem(1, player);
			}
			if (!player.getCooldownTracker().hasCooldown(this))
			{
				player.getCooldownTracker().setCooldown(this, 15);
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}

}
