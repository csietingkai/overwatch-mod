package com.tingkai.overwatch.messages;

import java.util.UUID;

import com.tingkai.overwatch.Overwatch;
import com.tingkai.overwatch.hero.Ability;
import com.tingkai.overwatch.hero.EnumHero;
import com.tingkai.overwatch.items.armors.ItemModArmor;
import com.tingkai.overwatch.items.weapons.ItemModWeapon;
import com.tingkai.overwatch.key.Keys.KeyBind;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketSyncKeys implements IMessage
{
	private boolean isKeyPressed;
	private UUID uuid;
	private String keyName;

	public CPacketSyncKeys()
	{
	}

	public CPacketSyncKeys(String keyName, boolean isKeyPressed, UUID uuid)
	{
		this.keyName = keyName;
		this.isKeyPressed = isKeyPressed;
		this.uuid = uuid;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.keyName = ByteBufUtils.readUTF8String(buf);
		this.isKeyPressed = buf.readBoolean();
		this.uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, keyName);
		buf.writeBoolean(this.isKeyPressed);
		ByteBufUtils.writeUTF8String(buf, uuid.toString());
	}

	public static class Handler implements IMessageHandler<CPacketSyncKeys, IMessage>
	{
		@Override
		public IMessage onMessage(final CPacketSyncKeys packet, final MessageContext ctx)
		{
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.world;
			mainThread.addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					EntityPlayer player = ctx.getServerHandler().playerEntity;
					if (packet.keyName.equals("Hero Information"))
					{
						Overwatch.keys.heroInformation.put(packet.uuid, packet.isKeyPressed);
					}
					else if (packet.keyName.equals("Reload"))
					{
						Overwatch.keys.reload.put(packet.uuid, packet.isKeyPressed);
					}
					else if (packet.keyName.equals("Ability 1"))
					{
						Overwatch.keys.ability1.put(packet.uuid, packet.isKeyPressed);
					}
					else if (packet.keyName.equals("Ability 2"))
					{
						Overwatch.keys.ability2.put(packet.uuid, packet.isKeyPressed);
					}
					else if (packet.keyName.equals("Ultimate"))
					{
						Overwatch.keys.ultimate.put(packet.uuid, packet.isKeyPressed);
					}
					else if (packet.keyName.equals("Alt Weapon"))
					{
						ItemStack main = player.getHeldItemMainhand();
						if (main != null && main.getItem() instanceof ItemModWeapon)
						{
							EnumHero hero = ((ItemModWeapon) main.getItem()).hero;
							hero.playersUsingAlt.put(packet.uuid, packet.isKeyPressed);
						}
					}
					else if (packet.keyName.equals("LMB"))
					{
						Overwatch.keys.lmb.put(packet.uuid, packet.isKeyPressed);
					}
					else if (packet.keyName.equals("RMB"))
					{
						Overwatch.keys.rmb.put(packet.uuid, packet.isKeyPressed);
					}
					else if (packet.keyName.equals("Toggle Ability 1"))
					{
						EnumHero hero = ItemModArmor.SetManager.playersWearingSets.get(packet.uuid);
						if (hero != null)
						{
							for (Ability ability : new Ability[] { hero.ability1, hero.ability2, hero.ability3 })
							{
								if (ability.isToggleable && ability.keybind == KeyBind.ABILITY_1 && ability.keybind.getCooldown(player) == 0)
								{
									hero.weapon.toggle(player, ability, packet.isKeyPressed);
								}
							}
						}
					}
					else if (packet.keyName.equals("Toggle Ability 2"))
					{
						EnumHero hero = ItemModArmor.SetManager.playersWearingSets.get(packet.uuid);
						if (hero != null)
						{
							for (Ability ability : new Ability[] { hero.ability1, hero.ability2, hero.ability3 })
							{
								if (ability.isToggleable && ability.keybind == KeyBind.ABILITY_2 && ability.keybind.getCooldown(player) == 0)
								{
									hero.weapon.toggle(player, ability, packet.isKeyPressed);
								}
							}
						}
					}
				}
			});
			return null;
		}
	}
}