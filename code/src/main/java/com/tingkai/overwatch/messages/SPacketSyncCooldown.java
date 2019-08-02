package com.tingkai.overwatch.messages;

import java.util.UUID;

import com.tingkai.overwatch.key.Keys.KeyBind;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSyncCooldown implements IMessage
{
	private UUID uuid;
	private String keybind;
	private int cooldown;

	public SPacketSyncCooldown()
	{
	}

	public SPacketSyncCooldown(UUID uuid, KeyBind keybind, int cooldown)
	{
		this.uuid = uuid;
		this.keybind = keybind.name();
		this.cooldown = cooldown;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
		this.keybind = ByteBufUtils.readUTF8String(buf);
		this.cooldown = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, this.uuid.toString());
		ByteBufUtils.writeUTF8String(buf, this.keybind);
		buf.writeInt(this.cooldown);
	}

	public static class Handler implements IMessageHandler<SPacketSyncCooldown, IMessage>
	{
		@Override
		public IMessage onMessage(final SPacketSyncCooldown packet, final MessageContext ctx)
		{
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					EntityPlayer player = Minecraft.getMinecraft().player;
					KeyBind keybind = KeyBind.valueOf(packet.keybind);
					if (player != null && keybind != null)
					{
						keybind.setCooldown(player, packet.cooldown);
					}
				}
			});
			return null;
		}
	}
}
