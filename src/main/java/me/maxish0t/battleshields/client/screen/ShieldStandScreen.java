package me.maxish0t.battleshields.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.maxish0t.battleshields.common.inventory.ShieldStandMenu;
import me.maxish0t.battleshields.network.BattleShieldNetwork;
import me.maxish0t.battleshields.network.packets.RotateBlockPacket;
import me.maxish0t.battleshields.utilities.ModReference;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ShieldStandScreen extends AbstractContainerScreen<ShieldStandMenu> {

    private static final ResourceLocation SHIELD_STAND =
            new ResourceLocation(ModReference.MOD_ID, "textures/screen/shield_stand.png");
    private final BlockPos blockPos;

    public ShieldStandScreen(ShieldStandMenu shieldStandMenu, Inventory inventory, Component component) {
        super(shieldStandMenu, inventory, component);
        this.blockPos = shieldStandMenu.blockPos;
    }

    @Override
    protected void init() {
        super.init();

        if (this.minecraft == null)
            return;

        int w = (width - imageWidth) / 2;
        int h = (height - imageHeight) / 2;

        this.addRenderableWidget(new Button(w + 110, h + 5, 60, 20, Component.translatable("screen.battleshields.rotate"),
                (press) -> BattleShieldNetwork.CHANNEL.sendToServer(new RotateBlockPacket(this.blockPos)), (button, poseStack, x, y) ->
                ShieldStandScreen.this.renderTooltip(poseStack,
                        ShieldStandScreen.this.minecraft.font.split(Component.translatable("screen.battleshields.rotate_info"),
                                Math.max(ShieldStandScreen.this.width / 2 - 43, 170)), x, y)));
    }

    @Override
    protected void renderLabels(@NotNull PoseStack poseStack, int var1, int var2) {
        this.font.draw(poseStack, Component.translatable("item.minecraft.shield"), 8, 6, 4210752);
        this.font.draw(poseStack, Component.translatable("container.inventory"), 8, imageHeight - 96 + 2, 4210752);
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        if (minecraft == null)
            return;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SHIELD_STAND);
        this.minecraft.getTextureManager().bindForSetup(SHIELD_STAND);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }
}
