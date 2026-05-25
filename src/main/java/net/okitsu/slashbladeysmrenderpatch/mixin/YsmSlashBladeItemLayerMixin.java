package net.okitsu.slashbladeysmrenderpatch.mixin;

import com.elfmcys.yesstevemodel.OOoOoO0o0o0o000OO0o0ooo0;
import com.elfmcys.yesstevemodel.o0ooO0ooO00oo0o00Oo00000;
import com.mojang.blaze3d.vertex.PoseStack;
import net.okitsu.slashbladeysmrenderpatch.client.SlashBladeYsmRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.elfmcys.yesstevemodel.o000oOO0000oOOOOo0O0oooO", remap = false)
public abstract class YsmSlashBladeItemLayerMixin {
    @Unique
    private static final ThreadLocal<Float> sbyrp$partialTick = ThreadLocal.withInitial(() -> 0.0F);

    @Inject(
        method = "oOo0OO0O0o000OO0O000oo0o(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILcom/elfmcys/yesstevemodel/OOoOoO0o0o0o000OO0o0ooo0;FFFFFF)V",
        at = @At("HEAD")
    )
    private void sbyrp$capturePartialTick(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                          OOoOoO0o0o0o000OO0o0ooo0 animatable, float limbSwing,
                                          float limbSwingAmount, float partialTick, float ageInTicks,
                                          float netHeadYaw, float headPitch, CallbackInfo ci) {
        sbyrp$partialTick.set(partialTick);
    }

    @Inject(
        method = "oOo0OO0O0o000OO0O000oo0o(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILcom/elfmcys/yesstevemodel/OOoOoO0o0o0o000OO0o0ooo0;FFFFFF)V",
        at = @At("RETURN")
    )
    private void sbyrp$clearPartialTick(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                        OOoOoO0o0o0o000OO0o0ooo0 animatable, float limbSwing,
                                        float limbSwingAmount, float partialTick, float ageInTicks,
                                        float netHeadYaw, float headPitch, CallbackInfo ci) {
        sbyrp$partialTick.remove();
    }

    @Inject(
        method = "oOo0OO0O0o000OO0O000oo0o(Lcom/elfmcys/yesstevemodel/o0ooO0ooO00oo0o00Oo00000;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void sbyrp$renderSlashBladeOnYsmBones(o0ooO0ooO00oo0o00Oo00000 model, LivingEntity entity,
                                                  ItemStack stack, ItemDisplayContext displayContext,
                                                  HumanoidArm arm, PoseStack poseStack,
                                                  MultiBufferSource bufferSource, int packedLight,
                                                  CallbackInfo ci) {
        if (!SlashBladeYsmRenderer.isSlashBlade(stack)) {
            return;
        }

        if (arm == HumanoidArm.LEFT) {
            SlashBladeYsmRenderer.renderRightWaist(model, poseStack, bufferSource, packedLight, stack);
        } else {
            SlashBladeYsmRenderer.renderOnEntity(entity, model, poseStack, bufferSource, packedLight, stack,
                sbyrp$partialTick.get());
        }
        ci.cancel();
    }
}
