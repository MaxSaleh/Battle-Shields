package me.maxish0t.battleshields.common.item;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import me.maxish0t.battleshields.common.config.Config;
import me.maxish0t.battleshields.client.renderer.ShieldTileEntityRenderer;

import me.maxish0t.battleshields.common.init.ModCreativeTabs;
import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.FORGE, modid = ModReference.MOD_ID)
public class BattleShieldItem extends ShieldItem {

	private final Supplier<Integer> damageReduction;
	@SuppressWarnings("deprecation")
	private final LazyLoadedValue<Ingredient> repairMaterial;

	public BattleShieldItem(ConfigValue<Integer> damageReduction, String repairTag, int durability, boolean fireProof) {
		this(damageReduction, () -> getTagIngredient(repairTag), durability, fireProof);
	}

	@SuppressWarnings("deprecation")
	public BattleShieldItem(Supplier<Integer> damageReduction, Supplier<Ingredient> repairMaterial, int durability, boolean fireProof) {
		super((fireProof ? new Properties().fireResistant() : new Properties())
				.tab(ModCreativeTabs.MAIN_TAB)
				.durability(durability));
		this.damageReduction = damageReduction;
		this.repairMaterial = new LazyLoadedValue<>(repairMaterial);
		DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
	}

	/**
	 * Required for one jar to work in 1.18.1 and 1.18.2.
	 * A reflection based hack that should not be used unless necessary.
	 * 
	 * @param name	The name of the item tag to get.
	 * @return	The ingredient representing the item tag for the given name.
	 */
	private static Ingredient getTagIngredient(String name) {
		Ingredient ingredient = null;

		try {
			final Method getAllTags = ObfuscationReflectionHelper.findMethod(ItemTags.class, "m_13193_");
			final Object allTags = getAllTags.invoke(null);
			final Class<?> TagCollection = Class.forName("net.minecraft.tags.TagCollection");
			final Method getTag = ObfuscationReflectionHelper.findMethod(TagCollection, "m_13404_", ResourceLocation.class);
			Object tag = getTag.invoke(allTags, new ResourceLocation(name));	
			final Class<?> Tag = Class.forName("net.minecraft.tags.Tag");
			final Method of = ObfuscationReflectionHelper.findMethod(Ingredient.class, "m_43911_", Tag);
			ingredient = (Ingredient) of.invoke(null, tag);
		} catch (ObfuscationReflectionHelper.UnableToFindMethodException e) {
			// Seems like we are in 1.18.2
		} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		if (ingredient == null) {// Seems like we are in 1.18.2
			try {
				final Method bind = ObfuscationReflectionHelper.findMethod(ItemTags.class, "m_203854_", String.class);
				Object tag = bind.invoke(null, name);
				final Class<?> TagKey = Class.forName("net.minecraft.tags.TagKey");
				final Method of = ObfuscationReflectionHelper.findMethod(Ingredient.class, "m_204132_", TagKey);
				ingredient = (Ingredient) of.invoke(null, tag);
			} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		return ingredient;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions () {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return ShieldTileEntityRenderer.instance;
			}
		});
	}

	/**
	 * Gets the percentage of the damage received this shield blocks.
	 * 
	 * @return The percentage of the damage received this shield blocks.
	 */
	public int getDamageReduction() {
		return damageReduction.get();
	}

	@Override
	public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
		return repairMaterial.get().test(repair);
	}

	@SubscribeEvent
	public static void onShieldBlock(ShieldBlockEvent e) {
		if (Config.enableDamageReduction.get()) {
			float damage = e.getOriginalBlockedDamage();
			LivingEntity victim = e.getEntity();
			DamageSource source = e.getDamageSource();
			
			if (source.isProjectile()) {
				return;
			}

			float f1 = 0.0f;
			if (damage > 0.0F && victim.isDamageSourceBlocked(source)) {
				f1 = damage;
				float reduction = 1f;

				Item shield = victim.getUseItem().getItem();
				if (shield instanceof BattleShieldItem) {
					reduction = ((BattleShieldItem) shield).getDamageReduction() / 100f;
				} else if (shield == Items.SHIELD || (!Config.customShieldMaxReduction.get()
						&& victim.getUseItem().getUseAnimation() == UseAnim.BLOCK)) {
					reduction = Config.defaultDamageReduction.get() / 100f;
				}

				if (reduction < 1f) {
					f1 = damage * reduction;
				}

				int level = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.THORNS, victim.getUseItem());
				if (level > 0 && reduction == 1f) {
					Enchantments.THORNS.doPostHurt(victim, Objects.requireNonNull(source.getEntity()), level);
				}
			}
			e.setBlockedDamage(f1);
		}
	}

}
