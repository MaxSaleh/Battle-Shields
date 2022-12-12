package me.maxish0t.battleshields.common.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;

import java.util.UUID;

public class AbstractSteve extends AbstractClientPlayer {

    private static final GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "fakesteve-bs");

    public AbstractSteve(ClientLevel clientLevel) {
        super(clientLevel, gameProfile, Minecraft.getInstance().getProfileKeyPairManager().profilePublicKey().orElse(null));
    }

}
