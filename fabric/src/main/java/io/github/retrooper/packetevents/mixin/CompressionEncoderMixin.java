package io.github.retrooper.packetevents.mixin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.CompressionEncoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CompressionEncoder.class)
public interface CompressionEncoderMixin {
  @Invoker(value = "encode") void packetevents_encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out);
}
