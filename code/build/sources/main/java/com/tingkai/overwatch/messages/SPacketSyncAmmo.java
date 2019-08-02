package com.tingkai.overwatch.messages;

import java.util.UUID;

import com.tingkai.overwatch.items.weapons.ItemModWeapon;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSyncAmmo implements IMessage
{
	private UUID uuid;
	private EnumHand hand;
	private int ammo;
	private EnumHand[] hands;

	public SPacketSyncAmmo()
	{
	}

	public SPacketSyncAmmo(UUID uuid, EnumHand hand, int ammo, EnumHand... hands)
	{
		this.uuid = uuid;
		this.hand = hand;
		this.ammo = ammo;
		this.hands = hands;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
		this.hand = EnumHand.valueOf(ByteBufUtils.readUTF8String(buf));
		this.ammo = buf.readInt();
		int numHands = buf.readInt();
		this.hands = new EnumHand[numHands];
		for (int i = 0; i < numHands; ++i)
		{
			this.hands[i] = EnumHand.valueOf(ByteBufUtils.readUTF8String(buf));
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, uuid.toString());
		ByteBufUtils.writeUTF8String(buf, hand.toString());
		buf.writeInt(this.ammo);
		buf.writeInt(this.hands.length);
		for (EnumHand hand : this.hands)
		{
			ByteBufUtils.writeUTF8String(buf, hand.name());
		}
	}

	public static class Handler implements IMessageHandler<SPacketSyncAmmo, IMessage>
	{
		@Override
		public IMessage onMessage(final SPacketSyncAmmo packet, final MessageContext ctx)
		{
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					EntityPlayer player = Minecraft.getMinecraft().player;
					ItemStack stack = player.getHeldItem(packet.hand);
					if (stack != null && stack.getItem() instanceof ItemModWeapon)
					{
						((ItemModWeapon) stack.getItem()).setCurrentAmmo(player, packet.ammo, packet.hands);
					}
				}
			});
			return null;
		}
	}
}