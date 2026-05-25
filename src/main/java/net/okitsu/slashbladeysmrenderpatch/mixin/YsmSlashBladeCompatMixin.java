package net.okitsu.slashbladeysmrenderpatch.mixin;

import com.elfmcys.yesstevemodel.O000o00000000OOoO0OooOO0;
import com.elfmcys.yesstevemodel.O0o0ooO0oOoOoo0O000O0000;
import com.elfmcys.yesstevemodel.Oo0Oo0O0OoOoO0oooO00O0o0;
import com.elfmcys.yesstevemodel.ooOoOOooOoooOoooo0oO0Oo0;
import net.okitsu.slashbladeysmrenderpatch.client.SlashBladeAnimationBridge;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.elfmcys.yesstevemodel.o0000o000ooO00O00o0O0ooO", remap = false)
public abstract class YsmSlashBladeCompatMixin {
    @Inject(method = "oOoo00O0o0oO0o0oO00OO0O0()Z", at = @At("HEAD"), cancellable = true)
    private static void sbyrp$isLoaded(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(SlashBladeAnimationBridge.isAvailable());
    }

    @Inject(method = "OO000o0ooOooooOOOOO0Ooo0()Z", at = @At("HEAD"), cancellable = true)
    private static void sbyrp$hasNewApi(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(SlashBladeAnimationBridge.isAvailable());
    }

    @Inject(
        method = "oOo0OO0O0o000OO0O000oo0o(Lnet/minecraft/world/item/ItemStack;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void sbyrp$isSlashBlade(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(SlashBladeAnimationBridge.isSlashBlade(stack));
    }

    @Inject(
        method = "oOo0OO0O0o000OO0O000oo0o(Lcom/elfmcys/yesstevemodel/Oo0Oo0O0OoOoO0oooO00O0o0;)Ljava/lang/String;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void sbyrp$getComboAnimationName(Oo0Oo0O0OoOoO0oooO00O0o0<?> event,
                                                    CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(SlashBladeAnimationBridge.getComboAnimationName(event));
    }

    @Inject(
        method = "oOo0OO0O0o000OO0O000oo0o(Lnet/minecraft/world/entity/LivingEntity;Lcom/elfmcys/yesstevemodel/Oo0Oo0O0OoOoO0oooO00O0o0;Ljava/lang/String;Lcom/elfmcys/yesstevemodel/ooOoOOooOoooOoooo0oO0Oo0;)Lcom/elfmcys/yesstevemodel/O0o0ooO0oOoOoo0O000O0000;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void sbyrp$handleSlashBladeAnim(LivingEntity entity, Oo0Oo0O0OoOoO0oooO00O0o0<?> event,
                                                   String animation,
                                                   ooOoOOooOoooOoooo0oO0Oo0 loopType,
                                                   CallbackInfoReturnable<O0o0ooO0oOoOoo0O000O0000> cir) {
        if (entity == null || event == null || animation == null || animation.isBlank()
            || event.OOo0O00Ooo00O0Ooo0OoOo0o() == null
            || !SlashBladeAnimationBridge.isSlashBlade(entity.getItemInHand(InteractionHand.MAIN_HAND))) {
            return;
        }

        String slashBladeAnimation = "slashblade:" + animation;
        String animationToPlay = SlashBladeAnimationBridge.hasAnimation(event, slashBladeAnimation)
            ? slashBladeAnimation
            : animation;

        event.OOo0O00Ooo00O0Ooo0OoOo0o().oOo0OO0O0o000OO0O000oo0o(animationToPlay, loopType);
        cir.setReturnValue(O0o0ooO0oOoOoo0O000O0000.oOo0OO0O0o000OO0O000oo0o);
    }

    @Inject(
        method = "oOo0OO0O0o000OO0O000oo0o(Lcom/elfmcys/yesstevemodel/O000o00000000OOoO0OooOO0;)Ljava/lang/Object;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void sbyrp$getMolangSlashBladeAnimation(O000o00000000OOoO0OooOO0<?> context,
                                                           CallbackInfoReturnable<Object> cir) {
        cir.setReturnValue(SlashBladeAnimationBridge.getComboAnimationName(context));
    }
}
