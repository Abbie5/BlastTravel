package foundationgames.blasttravel.mixin;

import com.mojang.blaze3d.glfw.Window;
import foundationgames.blasttravel.entity.CannonEntity;
import foundationgames.blasttravel.util.BTUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Shadow @Final private MinecraftClient client;
	@Unique private float blasttravel$cannonOverlayScale = 0.5f;

	@ModifyArg(method = "renderSpyglassOverlay", index = 0,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawTexture(Lnet/minecraft/util/Identifier;IIIFFIIII)V"))
	private Identifier blasttravel$allowCustomOverlays(Identifier old) {
		if (BTUtil.TEXTURE_OVERRIDE != null) {
			return BTUtil.TEXTURE_OVERRIDE;
		}
		return old;
	}

	@Inject(method = "render", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void blasttravel$renderCannonOverlay(GuiGraphics gui, float tickDelta, CallbackInfo ci, Window window, TextRenderer x, float frameDuration) {
		if (this.client.player.getVehicle() instanceof CannonEntity) {
			this.blasttravel$cannonOverlayScale = MathHelper.lerp(0.5f * frameDuration, this.blasttravel$cannonOverlayScale, 1.125f);
			if (this.client.options.getPerspective().isFirstPerson()) {
				BTUtil.renderHudOverlay((InGameHud)(Object)this, gui, this.blasttravel$cannonOverlayScale, BTUtil.CANNON_OVERLAY_TEX);
			}
		} else {
			this.blasttravel$cannonOverlayScale = 0.5f;
		}
	}
}
