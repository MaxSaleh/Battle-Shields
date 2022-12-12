package me.maxish0t.battleshields.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import me.maxish0t.battleshields.common.entity.AbstractSteve;
import me.maxish0t.battleshields.common.entity.blockentity.ShieldStandBlockEntity;
import me.maxish0t.battleshields.common.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ShieldStandRenderer implements BlockEntityRenderer<ShieldStandBlockEntity> {

    private final Minecraft minecraft = Minecraft.getInstance();
    private AbstractClientPlayer steve;

    public ShieldStandRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(@NotNull ShieldStandBlockEntity shieldStandBlockEntity, float partialTicks, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        if (steve == null) {
            BlockPos blockPos = shieldStandBlockEntity.getBlockPos();
            steve = new AbstractSteve(minecraft.level);
            steve.setPos(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        }

        if (Minecraft.getInstance().level == null) return;

        AbstractSteve abstractSteve = new AbstractSteve(minecraft.level);
        abstractSteve.setInvisible(true);

        // TODO
        /**
        float degreeAngle = 0.0F;
        switch (shieldStandBlockEntity.getAngle()) {
            case SOUTH -> degreeAngle = 90.0F;
            case WEST -> degreeAngle = 180.0F;
            case NORTH -> degreeAngle = 270.0F;
        }**/

        abstractSteve.getInventory().setItem(0, new ItemStack(ModItems.DIAMOND_SHIELD.get()));

        poseStack.pushPose();
        poseStack.translate(1.35F, 1.5F, 0.2F);
        poseStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), 90.0F, true));
        poseStack.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F), -72.0F, true));
        poseStack.mulPose(new Quaternion(new Vector3f(0.0F, 0.0F, 1.0F), 5.0F, true));
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.getRenderer(abstractSteve).render(abstractSteve, 0F, 0F,
                poseStack, bufferSource, combinedLightIn));
        poseStack.popPose();
    }
}
