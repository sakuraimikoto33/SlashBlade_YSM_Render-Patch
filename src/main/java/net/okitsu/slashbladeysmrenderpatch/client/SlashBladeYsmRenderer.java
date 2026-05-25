package net.okitsu.slashbladeysmrenderpatch.client;

import com.elfmcys.yesstevemodel.o0ooO0ooO00oo0o00Oo00000;
import com.elfmcys.yesstevemodel.oOOOOOOO0ooOo0OoOOO0ooOO;
import com.elfmcys.yesstevemodel.ooOO0OoOoO0o0o00oO0oo00o;
import com.mojang.blaze3d.vertex.PoseStack;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class SlashBladeYsmRenderer {
    private static final ResourceLocation BLADE_OBJ = ResourceLocation.fromNamespaceAndPath("slashblade", "model/blade.obj");
    private static final ResourceLocation BLADE_TEXTURE = ResourceLocation.fromNamespaceAndPath("slashblade", "model/blade.png");
    private static final double SLASHBLADE_FRAMES_PER_TICK = 1.5D;
    // Absolute SlashBlade motion-frame windows where YSM's BladeLocator is visible.
    private static final List<LegacyDrawnFrameRange> LEGACY_DRAWN_FRAME_RANGES = List.of(
        frameRange(202.5D, 281.25D),
        frameRange(500.0D, 571.25D),
        frameRange(800.0D, 877.5D),
        frameRange(900.0D, 1008.75D),
        frameRange(700.0D, 763.75D),
        frameRange(710.0D, 765.0D),
        frameRange(405.0D, 452.5D),
        frameRange(1817.25D, 1858.5D),
        frameRange(1830.75D, 1872.0D),
        frameRange(1103.75D, 1126.25D),
        frameRange(1200.0D, 1228.75D),
        frameRange(1303.75D, 1326.25D),
        frameRange(1400.0D, 1431.25D),
        frameRange(1500.0D, 1531.25D),
        frameRange(2008.75D, 2053.75D),
        frameRange(1607.5D, 1658.75D),
        frameRange(1701.25D, 1715.0D),
        frameRange(2101.25D, 2137.5D),
        frameRange(725.0D, 760.0D),
        frameRange(2207.5D, 2282.5D)
    );
    private static final Set<String> LEGACY_DRAWN_FALLBACK_ANIMATIONS = Set.of(
        "slashblade:combo_a3",
        "slashblade:combo_a3_end",
        "slashblade:combo_a3_end2",
        "slashblade:combo_a4",
        "slashblade:combo_a4ex",
        "slashblade:combo_a4ex_end",
        "slashblade:combo_a4_ex_end",
        "slashblade:combo_a5ex",
        "slashblade:combo_b1",
        "slashblade:combo_b1_end",
        "slashblade:combo_b1_end2",
        "slashblade:combo_b2",
        "slashblade:combo_b3",
        "slashblade:combo_b4",
        "slashblade:combo_b5",
        "slashblade:combo_b6",
        "slashblade:combo_b7",
        "slashblade:combo_b7_end",
        "slashblade:combo_b_end",
        "slashblade:combo_b_end2",
        "slashblade:combo_c",
        "slashblade:aerial_cleave_landing",
        "slashblade:aerial_cleave_loop",
        "slashblade:aerial_rave_a1",
        "slashblade:aerial_rave_a1_end",
        "slashblade:aerial_rave_a2",
        "slashblade:aerial_rave_a2_end",
        "slashblade:aerial_rave_a3",
        "slashblade:aerial_rave_b3",
        "slashblade:aerial_rave_b4",
        "slashblade:rapid_slash",
        "slashblade:rapid_slash_end",
        "slashblade:upperslash",
        "slashblade:upperslash_jump",
        "slashblade:upperslash_jump_end",
        "slashblade:rising_star",
        "slashblade:rising_star_end",
        "slashblade:wave_edge_vertical",
        "slashblade:drive_vertical",
        "slashblade:sakura_end_left",
        "slashblade:sakura_end_right",
        "slashblade:sakura_end_finish",
        "slashblade:drive_horizontal",
        "slashblade:circle_slash",
        "slashblade:circle_slash_end",
        "slashblade:void_slash",
        "slashblade:void_slash_sheath",
        "slashblade_addon:water_drive",
        "slashblade_addon:spiral_edge",
        "slashblade_addon:fire_spiral"
    );

    private static Reflection reflection;
    private static boolean unavailable;

    private SlashBladeYsmRenderer() {
    }

    public static boolean isSlashBlade(ItemStack stack) {
        if (stack.isEmpty() || unavailable) {
            return false;
        }

        try {
            return getBladeState(stack).isPresent();
        } catch (ReflectiveOperationException | LinkageError | RuntimeException ignored) {
            unavailable = true;
            return false;
        }
    }

    public static void renderOnEntity(LivingEntity entity, o0ooO0ooO00oo0o00Oo00000 model, PoseStack poseStack,
                                      MultiBufferSource bufferSource, int packedLight, ItemStack stack,
                                      float partialTick) {
        if (stack.isEmpty() || unavailable) {
            return;
        }

        try {
            Optional<?> bladeState = getBladeState(stack);
            if (bladeState.isEmpty()) {
                return;
            }

            List<ooOO0OoOoO0o0o00oO0oo00o> leftWaistBones = model.O0oo0O0O0O0oO0oooOOo00o0();
            List<ooOO0OoOoO0o0o00oO0oo00o> bladeBones = model.O0O00oOooOo000oo00OoO0Oo();
            List<ooOO0OoOoO0o0o00oO0oo00o> sheathBones = model.O000OOo000oOOo0OO0oo0o0O();
            List<ooOO0OoOoO0o0o00oO0oo00o> leftHandBones = model.Oo0O0OoOo0O0oOoo0000O0oO();
            List<ooOO0OoOoO0o0o00oO0oo00o> rightHandBones = model.OoooO0OO0000O00oo0Oo00OO();

            List<ooOO0OoOoO0o0o00oO0oo00o> effectiveLeftWaistBones =
                leftWaistBones.isEmpty() ? leftHandBones : leftWaistBones;

            if (bladeBones.isEmpty() || sheathBones.isEmpty() || leftWaistBones.isEmpty()) {
                List<ooOO0OoOoO0o0o00oO0oo00o> effectiveBladeBones =
                    bladeBones.isEmpty() ? rightHandBones : bladeBones;
                List<ooOO0OoOoO0o0o00oO0oo00o> effectiveSheathBones =
                    sheathBones.isEmpty() ? leftHandBones : sheathBones;
                renderBladeLegacyFallback(entity, bladeState.get(), model, poseStack, bufferSource, packedLight, stack,
                    partialTick, effectiveLeftWaistBones, effectiveBladeBones, effectiveSheathBones);
                return;
            }

            renderBladeWithBones(bladeState.get(), poseStack, bufferSource, packedLight, stack,
                effectiveLeftWaistBones, bladeBones, sheathBones);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException ignored) {
            unavailable = true;
        }
    }

    public static void renderRightWaist(o0ooO0ooO00oo0o00Oo00000 model, PoseStack poseStack,
                                        MultiBufferSource bufferSource, int packedLight, ItemStack stack) {
        if (stack.isEmpty() || unavailable) {
            return;
        }

        try {
            if (getBladeState(stack).isEmpty()) {
                return;
            }

            poseStack.pushPose();
            try {
                List<ooOO0OoOoO0o0o00oO0oo00o> rightWaistBones = model.oo0o0Oooo0OOO00OOo0OO000();
                if (!rightWaistBones.isEmpty()) {
                    applyLocatorTransform(rightWaistBones, poseStack);
                } else {
                    poseStack.translate(0.25D, 1.25D, 0.0D);
                    poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(5.0F));
                }

                poseStack.translate(0.0D, 0.0D, -0.7D);
                poseStack.scale(0.01F, 0.01F, 0.01F);
                poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-90.0F));
                poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180.0F));
                renderBladeOnly(poseStack, bufferSource, packedLight, stack);
            } finally {
                poseStack.popPose();
            }
        } catch (ReflectiveOperationException | LinkageError | RuntimeException ignored) {
            unavailable = true;
        }
    }

    private static void renderBladeWithBones(Object bladeState, PoseStack poseStack, MultiBufferSource bufferSource,
                                             int packedLight, ItemStack stack,
                                             List<ooOO0OoOoO0o0o00oO0oo00o> leftWaistBones,
                                             List<ooOO0OoOoO0o0o00oO0oo00o> bladeBones,
                                             List<ooOO0OoOoO0o0o00oO0oo00o> sheathBones)
        throws ReflectiveOperationException {
        ResourceLocation texture = getTexture(bladeState);
        Object model = getBladeModel(bladeState);
        String bladePart = getBladePart(bladeState);

        if (isVisibleLocator(leftWaistBones)) {
            renderBladeAndSheathAtLocator(leftWaistBones, stack, model, bladePart, texture, poseStack, bufferSource,
                packedLight);
        }

        if (isVisibleLocator(bladeBones)) {
            renderBladeAtLocator(bladeBones, stack, model, bladePart, texture, poseStack, bufferSource, packedLight);
        }

        if (isVisibleLocator(sheathBones)) {
            renderSheathAtLocator(sheathBones, stack, model, texture, poseStack, bufferSource, packedLight);
        }
    }

    private static void renderBladeLegacyFallback(LivingEntity entity, Object bladeState,
                                                  o0ooO0ooO00oo0o00Oo00000 model,
                                                  PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                                  ItemStack stack, float partialTick,
                                                  List<ooOO0OoOoO0o0o00oO0oo00o> leftWaistBones,
                                                  List<ooOO0OoOoO0o0o00oO0oo00o> bladeBones,
                                                  List<ooOO0OoOoO0o0o00oO0oo00o> sheathBones)
        throws ReflectiveOperationException {
        ResourceLocation texture = getTexture(bladeState);
        Object bladeModel = getBladeModel(bladeState);
        String bladePart = getBladePart(bladeState);

        if (isBladeDrawnForLegacy(entity, partialTick) && !bladeBones.isEmpty()) {
            if (!sheathBones.isEmpty()) {
                renderSheathAtLocator(sheathBones, stack, bladeModel, texture, poseStack, bufferSource, packedLight);
            } else {
                renderSheathAtLocator(leftWaistBones, stack, bladeModel, texture, poseStack, bufferSource, packedLight);
            }
            renderBladeAtLocator(bladeBones, stack, bladeModel, bladePart, texture, poseStack, bufferSource,
                packedLight);
            return;
        }

        renderBladeOnWaist(entity, model, poseStack, bufferSource, packedLight, stack, partialTick, leftWaistBones);
    }

    private static void renderBladeOnWaist(LivingEntity entity, o0ooO0ooO00oo0o00Oo00000 model,
                                           PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                           ItemStack stack, float partialTick,
                                           List<ooOO0OoOoO0o0o00oO0oo00o> leftWaistBones)
        throws ReflectiveOperationException {
        Optional<?> bladeState = getBladeState(stack);
        if (bladeState.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        try {
            if (!applyLocatorTransform(leftWaistBones, poseStack)) {
                poseStack.translate(-0.25D, 1.25D, 0.0D);
                poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(20.0F));
            }

            poseStack.translate(0.0D, 0.0D, -0.7D);
            poseStack.scale(0.01F, 0.01F, 0.01F);
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-90.0F));
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180.0F));

            Object state = bladeState.get();
            ResourceLocation texture = getTexture(state);
            Object bladeModel = getBladeModel(state);
            String bladePart = getBladePart(state);
            renderSheath(stack, bladeModel, texture, poseStack, bufferSource, packedLight);

            renderBlade(stack, bladeModel, bladePart, texture, poseStack, bufferSource, packedLight);
        } finally {
            poseStack.popPose();
        }
    }

    private static void renderBladeOnly(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                        ItemStack stack) throws ReflectiveOperationException {
        Optional<?> bladeState = getBladeState(stack);
        if (bladeState.isEmpty()) {
            return;
        }

        Object state = bladeState.get();
        ResourceLocation texture = getTexture(state);
        Object model = getBladeModel(state);
        renderBladeAndSheath(stack, model, getBladePart(state), texture, poseStack, bufferSource, packedLight);
    }

    private static void renderBladeAndSheath(ItemStack stack, Object model, String bladePart, ResourceLocation texture,
                                             PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
        throws ReflectiveOperationException {
        renderBlade(stack, model, bladePart, texture, poseStack, bufferSource, packedLight);
        renderSheath(stack, model, texture, poseStack, bufferSource, packedLight);
    }

    private static void renderBladeAndSheathAtLocator(List<ooOO0OoOoO0o0o00oO0oo00o> locatorBones,
                                                      ItemStack stack, Object model, String bladePart,
                                                      ResourceLocation texture, PoseStack poseStack,
                                                      MultiBufferSource bufferSource, int packedLight)
        throws ReflectiveOperationException {
        if (locatorBones.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        try {
            applyLocatorTransform(locatorBones, poseStack);
            poseStack.translate(0.0D, 0.025D, -0.6D);
            poseStack.scale(0.01F, 0.01F, 0.01F);
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-90.0F));
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180.0F));
            renderBladeAndSheath(stack, model, bladePart, texture, poseStack, bufferSource, packedLight);
        } finally {
            poseStack.popPose();
        }
    }

    private static void renderBladeAtLocator(List<ooOO0OoOoO0o0o00oO0oo00o> locatorBones, ItemStack stack,
                                             Object model, String bladePart, ResourceLocation texture,
                                             PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
        throws ReflectiveOperationException {
        if (locatorBones.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        try {
            applyLocatorTransform(locatorBones, poseStack);
            poseStack.translate(0.0D, 0.035D, 0.0D);
            poseStack.scale(0.01F, 0.01F, 0.01F);
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-90.0F));
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180.0F));
            renderBlade(stack, model, bladePart, texture, poseStack, bufferSource, packedLight);
        } finally {
            poseStack.popPose();
        }
    }

    private static void renderSheathAtLocator(List<ooOO0OoOoO0o0o00oO0oo00o> locatorBones, ItemStack stack,
                                              Object model, ResourceLocation texture, PoseStack poseStack,
                                              MultiBufferSource bufferSource, int packedLight)
        throws ReflectiveOperationException {
        if (locatorBones.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        try {
            applyLocatorTransform(locatorBones, poseStack);
            poseStack.translate(0.0D, 0.025D, -0.6D);
            poseStack.scale(0.01F, 0.01F, 0.01F);
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-90.0F));
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180.0F));
            renderSheath(stack, model, texture, poseStack, bufferSource, packedLight);
        } finally {
            poseStack.popPose();
        }
    }

    private static void renderBlade(ItemStack stack, Object model, String bladePart, ResourceLocation texture,
                                    PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
        throws ReflectiveOperationException {
        renderOverrided(stack, model, bladePart, texture, poseStack, bufferSource, packedLight);
        renderOverridedLuminous(stack, model, bladePart + "_luminous", texture, poseStack, bufferSource, packedLight);
    }

    private static void renderSheath(ItemStack stack, Object model, ResourceLocation texture, PoseStack poseStack,
                                     MultiBufferSource bufferSource, int packedLight)
        throws ReflectiveOperationException {
        renderOverrided(stack, model, "sheath", texture, poseStack, bufferSource, packedLight);
        renderOverridedLuminous(stack, model, "sheath_luminous", texture, poseStack, bufferSource, packedLight);
    }

    private static boolean isVisibleLocator(List<ooOO0OoOoO0o0o00oO0oo00o> bones) {
        if (bones.isEmpty()) {
            return false;
        }

        ooOO0OoOoO0o0o00oO0oo00o bone = bones.get(bones.size() - 1);
        return bone.OoooO0OO0000O00oo0Oo00OO() != 0.0F
            || bone.oOOO00ooO0oOOoOOo0OoOOOo() != 0.0F
            || bone.ooOooOO0oO00o00o0o0oOOoO() != 0.0F;
    }

    private static boolean applyLocatorTransform(List<ooOO0OoOoO0o0o00oO0oo00o> locatorBones,
                                                 PoseStack poseStack) {
        if (locatorBones.isEmpty()) {
            return false;
        }

        oOOOOOOO0ooOo0OoOOO0ooOO.oOo0OO0O0o000OO0O000oo0o(poseStack, locatorBones);
        return true;
    }

    private static boolean isBladeDrawnForLegacy(LivingEntity entity, float partialTick) {
        SlashBladeAnimationBridge.ComboAnimationState animationState =
            SlashBladeAnimationBridge.getComboAnimationState(entity);
        if (animationState.startFrame() < 0) {
            return LEGACY_DRAWN_FALLBACK_ANIMATIONS.contains(animationState.animationName())
                || LEGACY_DRAWN_FALLBACK_ANIMATIONS.contains(animationState.rawName());
        }

        double frame = animationState.startFrame()
            + (Math.max(0.0D, animationState.ticks() + partialTick) * SLASHBLADE_FRAMES_PER_TICK);
        for (LegacyDrawnFrameRange range : LEGACY_DRAWN_FRAME_RANGES) {
            if (range.contains(frame)) {
                return true;
            }
        }
        return false;
    }

    private static LegacyDrawnFrameRange frameRange(double startInclusive, double endExclusive) {
        return new LegacyDrawnFrameRange(startInclusive, endExclusive);
    }

    private static Optional<?> getBladeState(ItemStack stack) throws ReflectiveOperationException {
        ensureReflection();
        return (Optional<?>) reflection.bladeStateOf.invoke(null, stack);
    }

    private static ResourceLocation getTexture(Object bladeState) throws ReflectiveOperationException {
        Optional<?> texture = (Optional<?>) reflection.getTexture.invoke(bladeState);
        return texture.isPresent() ? (ResourceLocation) texture.get() : BLADE_TEXTURE;
    }

    private static Object getBladeModel(Object bladeState) throws ReflectiveOperationException {
        Optional<?> modelLocation = (Optional<?>) reflection.getModel.invoke(bladeState);
        Object manager = reflection.getBladeModelManager.invoke(null);
        return reflection.getBladeModel.invoke(manager, modelLocation.isPresent() ? modelLocation.get() : BLADE_OBJ);
    }

    private static String getBladePart(Object bladeState) throws ReflectiveOperationException {
        return (Boolean) reflection.isBroken.invoke(bladeState) ? "blade_damaged" : "blade";
    }

    private static long getLastActionTime(Object bladeState) throws ReflectiveOperationException {
        return (Long) reflection.getLastActionTime.invoke(bladeState);
    }

    private static void renderOverrided(ItemStack stack, Object model, String target, ResourceLocation texture,
                                        PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
        throws ReflectiveOperationException {
        invokeRender(reflection.renderOverrided, stack, model, target, texture, poseStack, bufferSource, packedLight);
    }

    private static void renderOverridedLuminous(ItemStack stack, Object model, String target, ResourceLocation texture,
                                                PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
        throws ReflectiveOperationException {
        invokeRender(reflection.renderOverridedLuminous, stack, model, target, texture, poseStack, bufferSource, packedLight);
    }

    private static void invokeRender(Method method, ItemStack stack, Object model, String target, ResourceLocation texture,
                                     PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
        throws ReflectiveOperationException {
        method.invoke(null, stack, model, target, texture, poseStack, bufferSource, packedLight);
    }

    private static void ensureReflection() throws ReflectiveOperationException {
        if (reflection != null) {
            return;
        }

        Class<?> bladeStateAccess = Class.forName("mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess");
        Class<?> slashBladeState = Class.forName("mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState");
        Class<?> bladeModelManager = Class.forName("mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager");
        Class<?> wavefrontObject = Class.forName("mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject");
        Class<?> bladeRenderState = Class.forName("mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState");

        reflection = new Reflection(
            bladeStateAccess.getMethod("of", ItemStack.class),
            slashBladeState.getMethod("getTexture"),
            slashBladeState.getMethod("getModel"),
            slashBladeState.getMethod("isBroken"),
            slashBladeState.getMethod("getLastActionTime"),
            bladeModelManager.getMethod("getInstance"),
            bladeModelManager.getMethod("getModel", ResourceLocation.class),
            bladeRenderState.getMethod("renderOverrided", ItemStack.class, wavefrontObject, String.class,
                ResourceLocation.class, PoseStack.class, MultiBufferSource.class, int.class),
            bladeRenderState.getMethod("renderOverridedLuminous", ItemStack.class, wavefrontObject, String.class,
                ResourceLocation.class, PoseStack.class, MultiBufferSource.class, int.class)
        );
    }

    private record Reflection(Method bladeStateOf, Method getTexture, Method getModel, Method isBroken,
                              Method getLastActionTime, Method getBladeModelManager, Method getBladeModel,
                              Method renderOverrided, Method renderOverridedLuminous) {
    }

    private record LegacyDrawnFrameRange(double startInclusive, double endExclusive) {
        private boolean contains(double frame) {
            return startInclusive <= frame && frame < endExclusive;
        }
    }
}
