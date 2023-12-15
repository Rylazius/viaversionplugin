/*
 * This file is part of ViaMCP - https://github.com/FlorianMichael/ViaMCP
 * Copyright (C) 2021-2023 FlorianMichael/EnZaXD and contributors

 */
package net.minusmc.viaversionplugin.injection.forge.mixins.network;

import io.netty.channel.Channel;
import net.minecraft.network.NetworkManager;
import net.raphimc.vialoader.netty.CompressionReorderEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Shadow private Channel channel;

    @Inject(method = "setCompressionTreshold", at = @At("RETURN"))
    public void reOrderPipeline(int p_setCompressionTreshold_1_, CallbackInfo ci) {
        channel.pipeline().fireUserEventTriggered(CompressionReorderEvent.INSTANCE);
    }
}
