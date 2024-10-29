package net.hexvolt.pocketwatchery;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Pocketwatchery.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> POCKETWATCH_OPEN = SOUND_EVENTS.register(
            "pocketwatch_open",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Pocketwatchery.MODID, "pocketwatch_open"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> POCKETWATCH_CLOSE = SOUND_EVENTS.register(
            "pocketwatch_close",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Pocketwatchery.MODID, "pocketwatch_close"))
    );

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
