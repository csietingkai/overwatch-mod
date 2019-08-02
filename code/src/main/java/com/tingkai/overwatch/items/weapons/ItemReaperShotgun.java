package com.tingkai.overwatch.items.weapons;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.tingkai.overwatch.Overwatch;
import com.tingkai.overwatch.entities.EntityModThrowable;
import com.tingkai.overwatch.entities.EntityReaperBullet;
import com.tingkai.overwatch.items.armors.ItemModArmor;
import com.tingkai.overwatch.messages.SPacketPotionEffect;
import com.tingkai.overwatch.messages.SPacketSpawnParticle;
import com.tingkai.overwatch.messages.SPacketTriggerAbility;
import com.tingkai.overwatch.potions.ModPotions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemReaperShotgun extends ItemModWeapon
{
	public static HashMap<EntityPlayer, Tuple<Integer, Vec3d>> clientTps = Maps.newHashMap();
	public static HashMap<EntityPlayer, Tuple<Integer, Vec3d>> serverTps = Maps.newHashMap();

	public ItemReaperShotgun()
	{
		super(30);
		this.hasOffhand = true;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void onItemLeftClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		if (this.canUse(player, true) && !world.isRemote && !serverTps.containsKey(player) && !hero.ability1.isSelected(player))
		{
			for (int i = 0; i < 20; i++)
			{
				EntityReaperBullet bullet = new EntityReaperBullet(world, player, hand);
				bullet.setAim(player, player.rotationPitch, player.rotationYaw, 3.0F, 4F, 1F, hand, false);
				world.spawnEntity(bullet);
			}
			Vec3d vec = EntityModThrowable.getShootingPos(player, player.rotationPitch, player.rotationYaw, hand);
			Overwatch.network.sendToAllAround(new SPacketSpawnParticle(0, vec.xCoord, vec.yCoord, vec.zCoord, 0xD93B1A, 0x510D30, 5, 5), new TargetPoint(world.provider.getDimension(), player.posX, player.posY, player.posZ, 128));

			this.subtractFromCurrentAmmo(player, 1, hand);
			if (!player.getCooldownTracker().hasCooldown(this))
			{
				player.getCooldownTracker().setCooldown(this, 11);
			}
			if (world.rand.nextInt(8) == 0)
			{
				player.getHeldItem(hand).damageItem(1, player);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Nullable
	private Vec3d getTeleportPos(EntityPlayer player)
	{
		try
		{
			RayTraceResult result = player.world.rayTraceBlocks(player.getPositionEyes(1), player.getLookVec().scale(Integer.MAX_VALUE), true, true, true);
			if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK && result.hitVec != null)
			{
				BlockPos pos = new BlockPos(result.hitVec.xCoord, result.getBlockPos().getY(), result.hitVec.zCoord);

				double adjustZ = result.sideHit == EnumFacing.SOUTH ? -0.5d : 0;
				double adjustX = result.sideHit == EnumFacing.EAST ? -0.5d : 0;

				pos = pos.add(adjustX, 0, adjustZ);
				IBlockState state = player.world.getBlockState(pos);
				IBlockState state1 = player.world.getBlockState(pos.up());
				IBlockState state2 = player.world.getBlockState(pos.up(2));

				if ((player.world.isAirBlock(pos.up()) || state1.getBlock().getCollisionBoundingBox(state1, player.world, pos.up()) == null || state1.getBlock().getCollisionBoundingBox(state1, player.world, pos.up()) == Block.NULL_AABB) && (player.world.isAirBlock(pos.up(2)) || state2.getBlock().getCollisionBoundingBox(state2, player.world, pos.up(2)) == null || state2.getBlock().getCollisionBoundingBox(state2, player.world, pos.up(2)) == Block.NULL_AABB) && !player.world.isAirBlock(pos) && state.getBlock().getCollisionBoundingBox(state, player.world, pos) != null && state.getBlock().getCollisionBoundingBox(state, player.world, pos) != Block.NULL_AABB && Math.sqrt(result.getBlockPos().distanceSq(player.posX, player.posY, player.posZ)) <= 35)
				{
					return new Vec3d(result.hitVec.xCoord + adjustX, result.getBlockPos().getY() + 1 + (state.getBlock() instanceof BlockFence ? 0.5d : 0), result.hitVec.zCoord + adjustZ);
				}
			}
		}
		catch (Exception e)
		{
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" }) // prevent warning
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, world, entity, itemSlot, isSelected);

		// teleport
		if (entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			if (isSelected && hero.ability1.isSelected(player) && this.canUse(player, true))
			{
				if (world.isRemote)
				{
					Vec3d tpVec = this.getTeleportPos(player);
					if (tpVec != null)
					{
						if (!clientTps.containsKey(player))
						{
							clientTps.put(player, new Tuple(-1, tpVec));
							Overwatch.proxy.spawnParticlesReaperTeleport(world, player, false, 0);
						}
						else
						{
							clientTps.put(player, new Tuple(clientTps.get(player).getFirst(), tpVec));
						}
					}
					else if (clientTps.containsKey(player) && clientTps.get(player).getFirst() == -1)
					{
						clientTps.remove(player);
					}
				}
				if (Overwatch.keys.lmb(player))
				{
					Vec3d tpVec = this.getTeleportPos(player);
					if (tpVec != null && !world.isRemote && player instanceof EntityPlayerMP)
					{
						Overwatch.network.sendToAll(new SPacketTriggerAbility(1, player, Math.floor(tpVec.xCoord) + 0.5d, tpVec.yCoord, Math.floor(tpVec.zCoord) + 0.5d));
						serverTps.put(player, new Tuple(70, new Vec3d(Math.floor(tpVec.xCoord) + 0.5d, tpVec.yCoord, Math.floor(tpVec.zCoord) + 0.5d)));
						PotionEffect effect = new PotionEffect(ModPotions.frozen, 60, 1, false, false);
						player.addPotionEffect(effect);
						Overwatch.network.sendToAll(new SPacketPotionEffect(player, effect));
					}
				}

				if (Overwatch.keys.rmb(player))
				{
					hero.ability1.toggled.remove(player.getPersistentID());
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void clientSide(PlayerTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END && event.side == Side.CLIENT)
		{
			ArrayList<EntityPlayer> toRemove = new ArrayList<EntityPlayer>();
			for (EntityPlayer player : clientTps.keySet())
			{
				if ((player.getHeldItemMainhand() == null || player.getHeldItemMainhand().getItem() != this || !hero.ability1.isSelected(player) || !this.canUse(player, true)) && clientTps.get(player).getFirst() == -1)
				{
					toRemove.add(player);
				}
				else
				{
					if (clientTps.get(player).getFirst() != -1)
					{
						if (Minecraft.getMinecraft().gameSettings.thirdPersonView != 1 && Minecraft.getMinecraft().player == player)
						{
							Minecraft.getMinecraft().gameSettings.thirdPersonView = 1;
						}

						if (clientTps.get(player).getFirst() > 1)
						{
							clientTps.put(player, new Tuple(clientTps.get(player).getFirst() - 1, clientTps.get(player).getSecond()));
						}
						else
						{
							toRemove.add(player);
						}
					}

					if (player.ticksExisted % 2 == 0)
					{
						Overwatch.proxy.spawnParticlesReaperTeleport(player.world, player, false, 1);
					}
					else if (player.ticksExisted % 3 == 0)
					{
						Overwatch.proxy.spawnParticlesReaperTeleport(player.world, player, false, 3);
					}
					Overwatch.proxy.spawnParticlesReaperTeleport(player.world, player, false, 2);
					if (clientTps.get(player).getFirst() > 40 && clientTps.get(player).getFirst() != -1)
					{
						if (player.ticksExisted % 2 == 0)
						{
							Overwatch.proxy.spawnParticlesReaperTeleport(player.world, player, true, 1);
						}
						else if (player.ticksExisted % 3 == 0)
						{
							Overwatch.proxy.spawnParticlesReaperTeleport(player.world, player, true, 3);
						}
						Overwatch.proxy.spawnParticlesReaperTeleport(player.world, player, true, 2);
					}
				}
			}
			for (EntityPlayer player : toRemove)
			{
				if (clientTps.get(player).getFirst() != -1 && Minecraft.getMinecraft().player == player)
				{
					Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
				}
				clientTps.remove(player);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" }) // prevent warning
	@SubscribeEvent
	public void serverSide(WorldTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END && event.world.getTotalWorldTime() % 3 == 0)
		{
			ArrayList<EntityPlayer> toRemove = new ArrayList<EntityPlayer>();
			for (EntityPlayer player : serverTps.keySet())
			{
				if (serverTps.get(player).getFirst() > 1)
				{
					serverTps.put(player, new Tuple(serverTps.get(player).getFirst() - 1, serverTps.get(player).getSecond()));

					if (serverTps.get(player).getFirst() == 40)
					{
						if (player.isRiding())
						{
							player.dismountRidingEntity();
						}
						player.setPositionAndUpdate(serverTps.get(player).getSecond().xCoord, serverTps.get(player).getSecond().yCoord, serverTps.get(player).getSecond().zCoord);
						hero.ability1.keybind.setCooldown(player, 200);
					}
				}
				else
				{
					toRemove.add(player);
				}
			}
			for (EntityPlayer player : toRemove)
				serverTps.remove(player);
		}
	}

	@SubscribeEvent
	public void passiveHeal(LivingHurtEvent event)
	{
		if (event.getSource().getSourceOfDamage() instanceof EntityPlayer)
		{
			EntityPlayer player = ((EntityPlayer) event.getSource().getSourceOfDamage());
			if (!player.world.isRemote && ItemModArmor.SetManager.playersWearingSets.get(player.getPersistentID()) == hero && player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == this)
			{
				try
				{
					float damage = event.getAmount();
					damage = CombatRules.getDamageAfterAbsorb(damage, (float) player.getTotalArmorValue(), (float) player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
					damage = this.applyPotionDamageCalculations(player, event.getSource(), damage);
					if (damage >= 0)
					{
						player.heal(damage * 0.2f);
					}
				}
				catch (Exception e)
				{
				}
			}
		}
	}

	/** Copied from EntityLivingBase bc it's protected */
	private float applyPotionDamageCalculations(EntityPlayer player, DamageSource source, float damage)
	{
		if (source.isDamageAbsolute())
		{
			return damage;
		}
		else
		{
			if (player.isPotionActive(MobEffects.RESISTANCE) && source != DamageSource.OUT_OF_WORLD)
			{
				int i = (player.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
				int j = 25 - i;
				float f = damage * (float) j;
				damage = f / 25.0F;
			}
			if (damage <= 0.0F)
			{
				return 0.0F;
			}
			else
			{
				int k = EnchantmentHelper.getEnchantmentModifierDamage(player.getArmorInventoryList(), source);
				if (k > 0)
				{
					damage = CombatRules.getDamageAfterMagicAbsorb(damage, (float) k);
				}
				return damage;
			}
		}
	}
}