/*
 * This file is part of ViaMCP - https://github.com/FlorianMichael/ViaMCP
 * Copyright (C) 2021-2023 FlorianMichael/EnZaXD and contributors

 */
package net.minusmc.viaversionplugin.injection.forge.mixins.gui;

import net.minecraft.client.gui.*;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.raphimc.vialoader.util.VersionEnum;
import cc.paimonmc.viamcp.ViaMCP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.ArrayList;
@Mixin(GuiMultiplayer.class)
public class MixinGuiMultiplayer extends GuiScreen {

    private GuiSlider viaSlider;
    private final List<VersionEnum> versions = new ArrayList<>();
    private int protocolsSize;

    @Inject(method = "initGui", at = @At("RETURN"))
    public void hookCustomButton(CallbackInfo ci) {
        versions.add(VersionEnum.r1_8);
        versions.add(VersionEnum.r1_10);
        versions.add(VersionEnum.r1_12_2);
        versions.add(VersionEnum.r1_16_4tor1_16_5);
        versions.add(VersionEnum.r1_17_1);
        protocolsSize = versions.size();

        buttonList.add(viaSlider = new GuiSlider(1337, width - 104, 8, 98, 20, "Version: ", "", 0, protocolsSize - 1, getProtocolIndex(ViaMCP.targetVersion.getVersion()), false, true,
            guiSlider -> {
                ViaMCP.targetVersion = versions.get(guiSlider.getValueInt());
                this.updatePortalText();
            }));
        this.updatePortalText();
    }

    private void updatePortalText() {
        if (this.viaSlider == null) return;
        this.viaSlider.displayString = "Version: " + ViaMCP.targetVersion.getName();
    }

    private int getProtocolIndex(int id) {
        for (int i = 0; i < protocolsSize; i++) {
            if (versions.get(i).getVersion() == id) return i;
        }
        return -1;
    }
}
