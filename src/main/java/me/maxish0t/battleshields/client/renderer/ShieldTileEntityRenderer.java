package me.maxish0t.battleshields.client.renderer;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;

import me.maxish0t.battleshields.client.model.ShieldTextures;
import me.maxish0t.battleshields.common.init.ModItems;
import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(value = Dist.CLIENT, modid = ModReference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ShieldTileEntityRenderer extends BlockEntityWithoutLevelRenderer {

	public static ShieldTileEntityRenderer instance;
	
	public ShieldTileEntityRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
		super(p_172550_, p_172551_);
	}
	
	@SubscribeEvent
	public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
		instance = new ShieldTileEntityRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
				Minecraft.getInstance().getEntityModels());
		event.registerReloadListener(instance);
	}

	@Override
	public void renderByItem(ItemStack stack, @NotNull TransformType transformType, PoseStack matrixStack,
							 @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		matrixStack.pushPose();
		matrixStack.scale(1, -1, -1);
		boolean flag = stack.getTagElement("BlockEntityTag") != null;
		Material rendermaterial = flag ? ModelBakery.SHIELD_BASE
				: ModelBakery.NO_PATTERN_SHIELD;

		Item shield = stack.getItem();
		if (shield == ModItems.IRON_SHIELD.get()) {
			rendermaterial = flag ? ShieldTextures.LOCATION_IRON_SHIELD_BASE
					: ShieldTextures.LOCATION_IRON_SHIELD_BASE_NOPATTERN;
		} else if (shield == ModItems.GOLD_SHIELD.get()) {
			rendermaterial = flag ? ShieldTextures.LOCATION_GOLD_SHIELD_BASE
					: ShieldTextures.LOCATION_GOLD_SHIELD_BASE_NOPATTERN;
		} else if (shield == ModItems.DIAMOND_SHIELD.get()) {
			rendermaterial = flag ? ShieldTextures.LOCATION_DIAMOND_SHIELD_BASE
					: ShieldTextures.LOCATION_DIAMOND_SHIELD_BASE_NOPATTERN;
		} else if (shield == ModItems.NETHERITE_SHIELD.get()) {
			rendermaterial = flag ? ShieldTextures.LOCATION_NETHERITE_SHIELD_BASE
					: ShieldTextures.LOCATION_NETHERITE_SHIELD_BASE_NOPATTERN;
		}

		VertexConsumer vertexConsumer = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(
				buffer, shieldModel.renderType(rendermaterial.atlasLocation()), true, stack.hasFoil()));
		this.shieldModel.handle().render(matrixStack, vertexConsumer, combinedLight, combinedOverlay, 1.0F,
				1.0F, 1.0F, 1.0F);
		if (flag) {
			List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(stack),
					BannerBlockEntity.getItemPatterns(stack));
			BannerRenderer.renderPatterns(matrixStack, buffer, combinedLight, combinedOverlay,
					this.shieldModel.plate(), rendermaterial, false, list, stack.hasFoil());
		} else {
			this.shieldModel.plate().render(matrixStack, vertexConsumer, combinedLight, combinedOverlay, 1.0F,
					1.0F, 1.0F, 1.0F);
		}
		matrixStack.popPose();
	}

}
