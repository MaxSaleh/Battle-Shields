package me.maxish0t.battleshields.common.datagen;

import java.util.Objects;
import java.util.function.Consumer;

import me.maxish0t.battleshields.common.init.ModBlocks;
import me.maxish0t.battleshields.common.init.ModItems;
import me.maxish0t.battleshields.common.recipes.ShieldRecipes;

import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.UpgradeRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class BattleShieldsRecipeProvider extends RecipeProvider {

	private static final char SHIELD_CHAR = 'C';
	private static final char MATERIAL_CHAR = 'M';

	public BattleShieldsRecipeProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ModBlocks.SHIELD_STAND.get())
				.define('G', Items.GOLD_INGOT).define('S', Items.SMOOTH_STONE_SLAB)
				.pattern(" S ").pattern(" G ").pattern("SGS")
				.unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT)).save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.IRON_SHIELD.get()).define(SHIELD_CHAR, Items.SHIELD)
				.define(MATERIAL_CHAR, Tags.Items.INGOTS_IRON)
				.pattern("" + MATERIAL_CHAR + SHIELD_CHAR + MATERIAL_CHAR).pattern(" " + MATERIAL_CHAR + " ")
				.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)).save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.GOLD_SHIELD.get()).define(SHIELD_CHAR, Items.SHIELD)
				.define(MATERIAL_CHAR, Tags.Items.INGOTS_GOLD)
				.pattern("" + MATERIAL_CHAR + SHIELD_CHAR + MATERIAL_CHAR).pattern(" " + MATERIAL_CHAR + " ")
				.unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD)).save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.DIAMOND_SHIELD.get()).define(SHIELD_CHAR, Items.SHIELD)
				.define(MATERIAL_CHAR, Tags.Items.GEMS_DIAMOND)
				.pattern("" + MATERIAL_CHAR + SHIELD_CHAR + MATERIAL_CHAR).pattern(" " + MATERIAL_CHAR + " ")
				.unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND)).save(consumer);
		smithingReinforce(consumer, ModItems.DIAMOND_SHIELD.get(), ModItems.NETHERITE_SHIELD.get());
		specialRecipe(consumer);
	}

	private void specialRecipe(Consumer<FinishedRecipe> consumer) {
		SpecialRecipeBuilder.special(ShieldRecipes.SERIALIZER).save(consumer,
				ModReference.MOD_ID + ":dynamic/" + Objects.requireNonNull(ForgeRegistries.RECIPE_SERIALIZERS.getKey(ShieldRecipes.SERIALIZER)).getPath());
	}

	private static void smithingReinforce(Consumer<FinishedRecipe> recipeConsumer, Item itemToReinforce, Item output) {
		UpgradeRecipeBuilder
				.smithing(Ingredient.of(itemToReinforce), Ingredient.of(Items.NETHERITE_INGOT),
						output)
				.unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(recipeConsumer,
						ModReference.MOD_ID + ":smithing/" + Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output.asItem())).getPath());
	}

}
