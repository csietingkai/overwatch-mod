package com.tingkai.overwatch.messages;

import java.util.UUID;

import com.tingkai.overwatch.hero.Ability;
import com.tingkai.overwatch.hero.EnumHero;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSyncAbilityUses implements IMessage
{
	private UUID uuid;
	private String hero;
	private int ability;
	private int uses;

	public SPacketSyncAbilityUses()
	{
	}

	public SPacketSyncAbilityUses(UUID uuid, EnumHero hero, int ability, int uses, boolean playSound)
	{
		this.uuid = uuid;
		this.hero = hero.name();
		this.ability = ability;
		this.uses = uses;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
		this.hero = ByteBufUtils.readUTF8String(buf);
		this.ability = buf.readInt();
		this.uses = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, this.uuid.toString());
		ByteBufUtils.writeUTF8String(buf, this.hero);
		buf.writeInt(this.ability);
		buf.writeInt(this.uses);
	}

	public static class Handler implements IMessageHandler<SPacketSyncAbilityUses, IMessage>
	{
		@Override
		public IMessage onMessage(final SPacketSyncAbilityUses packet, final MessageContext ctx)
		{
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					EntityPlayer player = Minecraft.getMinecraft().player;
					EnumHero hero = EnumHero.valueOf(packet.hero);
					Ability ability = null;
					if (packet.ability == 1)
					{
						ability = hero.ability1;
					}
					else if (packet.ability == 2)
					{
						ability = hero.ability2;
					}
					else if (packet.ability == 3)
					{
						ability = hero.ability3;
					}

					if (player != null && ability != null)
					{
						ability.multiAbilityUses.put(player.getPersistentID(), packet.uses);
					}
				}
			});
			return null;
		}
	}

}
