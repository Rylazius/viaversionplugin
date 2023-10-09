/*
 * This file is part of ViaForge - https://github.com/FlorianMichael/ViaForge
 * Copyright (C) 2021-2023 FlorianMichael/EnZaXD and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.minusmc.viaversionplugin.injection.mixins;

import net.minecraft.client.gui.*;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.raphimc.vialoader.util.VersionEnum;
import de.florianmichael.viaforge.ViaForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiMultiplayer.class)
public class MixinGuiMultiplayer extends GuiScreen {

    private GuiSlider viaSlider;
    private final List<VersionEnum> versions = VersionEnum.SORTED_VERSIONS;
    private final int protocolsSize = versions.size();

    @Inject(method = "initGui", at = @At("RETURN"))
    public void hookCustomButton(CallbackInfo ci) {
        buttonList.add(viaSlider = new GuiSlider(1337, width - 104, 8, 98, 20, "Version: ", "", 0, protocolsSize - 1, protocolsSize - 1 - getProtocolIndex(ViaForge.targetVersion.getVersion()), false, true,
            guiSlider -> {
                ViaForge.targetVersion = VersionEnum.SORTED_VERSIONS.get(guiSlider.getValueInt());
                this.updatePortalText();
            }));
        this.updatePortalText();
    }

    private void updatePortalText() {
        if (this.viaSlider == null) return;
        this.viaSlider.displayString = "Version: " + ViaForge.targetVersion.getName();
    }

    private int getProtocolIndex(int id) {
        for (int i = 0; i < protocolsSize; i++) {
            if (versions.get(i).getVersion() == id) return i;
        }
        return -1;
    }
}
