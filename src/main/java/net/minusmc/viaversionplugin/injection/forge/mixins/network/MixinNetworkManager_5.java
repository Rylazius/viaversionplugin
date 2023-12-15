/*
 * This file is part of ViaMCP - https://github.com/FlorianMichael/ViaMCP
 * Copyright (C) 2021-2023 FlorianMichael/EnZaXD and contributors

 */
package net.minusmc.viaversionplugin.injection.forge.mixins.network;

import cc.paimonmc.viamcp.ViaMCP;
import cc.paimonmc.viamcp.handler.CommonTransformer;
import cc.paimonmc.viamcp.handler.MCPDecodeHandler;
import cc.paimonmc.viamcp.handler.MCPEncodeHandler;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.NetworkManager$5")
public class MixinNetworkManager_5 {

    @Inject(method = "initChannel", at = @At(value = "TAIL"), remap = false)
    private void onInitChannel(Channel channel, CallbackInfo ci) {
        if (channel instanceof SocketChannel && ViaMCP.getInstance().getVersion() != ViaMCP.PROTOCOL_VERSION) {
            final UserConnection user = new UserConnectionImpl(channel, true);
            new ProtocolPipelineImpl(user);

            channel.pipeline().addBefore("encoder", CommonTransformer.HANDLER_ENCODER_NAME, new MCPEncodeHandler(user)).addBefore("decoder", CommonTransformer.HANDLER_DECODER_NAME, new MCPDecodeHandler(user));
        }
    }
}
