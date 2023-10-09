/*
 * This file is part of ViaForge - https://github.com/FlorianMichael/ViaForge
 * Copyright (C) 2021-2023 FlorianMichael/EnZaXD and contributors

 */
package de.florianmichael.viaforge;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.protocols.base.BaseVersionProvider;
import net.minecraft.client.Minecraft;
import net.raphimc.vialoader.ViaLoader;
import net.raphimc.vialoader.impl.platform.ViaBackwardsPlatformImpl;
import net.raphimc.vialoader.impl.platform.ViaRewindPlatformImpl;
import net.raphimc.vialoader.impl.viaversion.VLInjector;
import net.raphimc.vialoader.impl.viaversion.VLLoader;
import net.raphimc.vialoader.netty.VLLegacyPipeline;
import net.raphimc.vialoader.util.VersionEnum;

public class ViaForge {
    public final static VersionEnum NATIVE_VERSION = VersionEnum.r1_8;

    public static VersionEnum targetVersion = VersionEnum.r1_8;

    public static void start() {
        VersionEnum.SORTED_VERSIONS.remove(VersionEnum.r1_7_6tor1_7_10);
        VersionEnum.SORTED_VERSIONS.remove(VersionEnum.r1_7_2tor1_7_5);
        ViaLoader.init(
                null,
                new VLLoader() {
                    @Override
                    public void load() {
                        super.load();
                        Via.getManager().getProviders().use(VersionProvider.class, new BaseVersionProvider() {
                            @Override
                            public int getClosestServerProtocol(UserConnection connection) throws Exception {
                                if (connection.isClientSide() && !Minecraft.getMinecraft().isSingleplayer()) {
                                    return targetVersion.getVersion();
                                }
                                return super.getClosestServerProtocol(connection);
                            }
                        });
                    }
                },
                new VLInjector() {
                    @Override
                    public String getDecoderName() {
                        return VLLegacyPipeline.VIA_DECODER_NAME;
                    }

                    @Override
                    public String getEncoderName() {
                        return VLLegacyPipeline.VIA_ENCODER_NAME;
                    }
                },
                null,
                ViaBackwardsPlatformImpl::new, ViaRewindPlatformImpl::new
        );
    }
}
