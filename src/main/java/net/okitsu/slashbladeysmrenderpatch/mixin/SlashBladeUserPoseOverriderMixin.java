package net.okitsu.slashbladeysmrenderpatch.mixin;

import com.elfmcys.yesstevemodel.OoooO0OO0000O00oo0Oo00OO;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mods.flammpfeil.slashblade.event.client.UserPoseOverrider", remap = false)
public abstract class SlashBladeUserPoseOverriderMixin {
    @Inject(method = "onRenderPlayerEventPre", at = @At("HEAD"), cancellable = true, require = 0)
    private void sbyrp$skipYsmPlayerPoseOverride(RenderLivingEvent.Pre<?, ?> event, CallbackInfo ci) {
        if (event.isCanceled() || sbyrp$isYsmRenderer(event.getRenderer()) || sbyrp$isYsmModelActive(event.getEntity())) {
            ci.cancel();
        }
    }

    private static boolean sbyrp$isYsmRenderer(LivingEntityRenderer<?, ?> renderer) {
        for (Class<?> type = renderer.getClass(); type != null; type = type.getSuperclass()) {
            if (type.getName().startsWith("com.elfmcys.yesstevemodel.")) {
                return true;
            }
        }
        return false;
    }

    private static boolean sbyrp$isYsmModelActive(LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return false;
        }

        try {
            return OoooO0OO0000O00oo0Oo00OO.oOoo00O0o0oO0o0oO00OO0O0(player)
                .map(OoooO0OO0000O00oo0Oo00OO::oOooO00OOooOO0ooo000o0o0)
                .orElse(false);
        } catch (LinkageError | RuntimeException ignored) {
            return false;
        }
    }
}
