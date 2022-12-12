package me.maxish0t.battleshields.common.init;

import me.maxish0t.battleshields.common.entity.AbstractSteve;
import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ModReference.MOD_ID);

    /**
     * Bibliosteve Entity
     */
    public static final RegistryObject<EntityType<AbstractSteve>> FAKESTEVE_BS = register("fakesteve-bs",
            EntityType.Builder.<AbstractSteve>of((type, world) -> new AbstractSteve((ClientLevel) world), MobCategory.MISC)
                    .sized(1F, 0.4F).fireImmune()
                    .clientTrackingRange(4)
                    .updateInterval(40));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(name));
    }

}
