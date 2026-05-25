package net.okitsu.slashbladeysmrenderpatch.client;

import com.elfmcys.yesstevemodel.O000o00000000OOoO0OooOO0;
import com.elfmcys.yesstevemodel.Oo0Oo0O0OoOoO0oooO00O0o0;
import com.elfmcys.yesstevemodel.OoO0oo0o0o0oOoo0oOOO0Ooo;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class SlashBladeAnimationBridge {
    private static Reflection reflection;
    private static boolean unavailable;

    private SlashBladeAnimationBridge() {
    }

    public static boolean isAvailable() {
        if (unavailable) {
            return false;
        }

        try {
            ensureReflection();
            return true;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException ignored) {
            unavailable = true;
            return false;
        }
    }

    public static boolean isSlashBlade(ItemStack stack) {
        if (stack == null || stack.isEmpty() || unavailable) {
            return false;
        }

        try {
            return getBladeState(stack).isPresent();
        } catch (ReflectiveOperationException | LinkageError | RuntimeException ignored) {
            unavailable = true;
            return false;
        }
    }

    public static String getComboAnimationName(Oo0Oo0O0OoOoO0oooO00O0o0<?> event) {
        if (event == null) {
            return "";
        }

        OoO0oo0o0o0oOoo0oOOO0Ooo<?> animatable = event.oOoo00O0o0oO0o0oO00OO0O0();
        if (animatable == null) {
            return "";
        }

        Entity entity = animatable.ooo00OoO00OOOO0oOooOo0Oo();
        if (entity instanceof LivingEntity livingEntity) {
            return getComboAnimationName(livingEntity);
        }
        return "";
    }

    public static String getComboAnimationName(O000o00000000OOoO0OooOO0<?> context) {
        if (context == null) {
            return "";
        }

        Object entity = context.oOo0OO0O0o000OO0O000oo0o();
        if (entity instanceof LivingEntity livingEntity) {
            return getComboAnimationName(livingEntity);
        }
        return "";
    }

    public static String getComboAnimationName(LivingEntity entity) {
        return getComboAnimationState(entity).animationName();
    }

    public static ComboAnimationState getComboAnimationState(LivingEntity entity) {
        if (entity == null || unavailable) {
            return ComboAnimationState.EMPTY;
        }

        ItemStack mainHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
        if (mainHand.isEmpty()) {
            return ComboAnimationState.EMPTY;
        }

        try {
            Optional<?> state = getBladeState(mainHand);
            if (state.isEmpty()) {
                return ComboAnimationState.EMPTY;
            }

            Object comboTicks = reflection.peekCurrentComboStateTicks.invoke(state.get(), entity);
            if (!(comboTicks instanceof Map.Entry<?, ?> entry) || entry.getValue() == null) {
                return ComboAnimationState.EMPTY;
            }

            int ticks = entry.getKey() instanceof Number number ? number.intValue() : 0;
            int startFrame = getComboStartFrame(entry.getValue());
            String rawName = entry.getValue().toString();
            return new ComboAnimationState(normalizeComboName(rawName, entity), rawName, ticks, startFrame);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException ignored) {
            unavailable = true;
            return ComboAnimationState.EMPTY;
        }
    }

    public static boolean hasAnimation(Oo0Oo0O0OoOoO0oooO00O0o0<?> event, String animationName) {
        if (event == null || animationName == null || animationName.isBlank()) {
            return false;
        }

        OoO0oo0o0o0oOoo0oOOO0Ooo<?> animatable = event.oOoo00O0o0oO0o0oO00OO0O0();
        return animatable != null && animatable.Oo0O0OoOo0O0oOoo0000O0oO(animationName) != null;
    }

    private static String normalizeComboName(String comboName, LivingEntity entity) {
        return switch (comboName) {
            case "slashblade:combo_a4_ex" -> "slashblade:combo_a4ex";
            case "slashblade:judgement_cut" ->
                entity.onGround() ? comboName : "slashblade:judgement_cut_slash_air";
            case "slashblade:judgement_cut_slash_just2" ->
                entity.onGround() ? comboName : "slashblade:judgement_cut_slash_air_just2";
            default -> comboName;
        };
    }

    private static Optional<?> getBladeState(ItemStack stack) throws ReflectiveOperationException {
        ensureReflection();
        return (Optional<?>) reflection.bladeStateOf.invoke(null, stack);
    }

    private static int getComboStartFrame(Object comboLocation) throws ReflectiveOperationException {
        Object registry = reflection.comboStateRegistry.get(null);
        if (!(registry instanceof Registry<?> comboRegistry) || !(comboLocation instanceof ResourceLocation location)) {
            return -1;
        }

        Object comboState = comboRegistry.get(location);
        if (comboState == null) {
            return -1;
        }
        return (Integer) reflection.comboStateGetStartFrame.invoke(comboState);
    }

    private static void ensureReflection() throws ReflectiveOperationException {
        if (reflection != null) {
            return;
        }

        Class<?> bladeStateAccess = Class.forName("mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess");
        Class<?> slashBladeState = Class.forName("mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState");
        Class<?> comboStateRegistry = Class.forName("mods.flammpfeil.slashblade.registry.ComboStateRegistry");
        Class<?> comboState = Class.forName("mods.flammpfeil.slashblade.registry.combo.ComboState");

        Field registry = comboStateRegistry.getField("REGISTRY");

        reflection = new Reflection(
            bladeStateAccess.getMethod("of", ItemStack.class),
            slashBladeState.getMethod("peekCurrentComboStateTicks", LivingEntity.class),
            registry,
            comboState.getMethod("getStartFrame")
        );
    }

    public record ComboAnimationState(String animationName, String rawName, int ticks, int startFrame) {
        private static final ComboAnimationState EMPTY = new ComboAnimationState("", "", 0, -1);
    }

    private record Reflection(Method bladeStateOf, Method peekCurrentComboStateTicks, Field comboStateRegistry,
                              Method comboStateGetStartFrame) {
    }
}
