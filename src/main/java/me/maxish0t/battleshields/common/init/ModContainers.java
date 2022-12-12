package me.maxish0t.battleshields.common.init;

import me.maxish0t.battleshields.common.inventory.ShieldStandMenu;
import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ModReference.MOD_ID);

    /**
     * Shield Stand
     */
    public static final RegistryObject<MenuType<ShieldStandMenu>> SHIELD_STAND = CONTAINERS.register("shield_stand",
            () -> IForgeMenuType.create(ShieldStandMenu::new));

}
