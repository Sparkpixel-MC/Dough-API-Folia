package io.github.bakedlibs.dough.skins;

import java.net.URL;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import io.github.bakedlibs.dough.reflection.ReflectionUtils;
import io.github.bakedlibs.dough.versions.MinecraftVersion;
import io.github.bakedlibs.dough.versions.UnknownServerVersionException;
import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

public class CustomGameProfile {

    private static final String PLAYER_NAME = "CS-CoreLib";

    /**
     * The skin's property key.
     */
    private static final String PROPERTY_KEY = "textures";

    private final UUID uuid;
    private final URL skinUrl;
    private final String texture;
    private GameProfile gameProfile;

    CustomGameProfile(@Nonnull UUID uuid, @Nullable String texture, @Nonnull URL url) {
        this.uuid = uuid;
        this.skinUrl = url;
        this.texture = texture;
        if (texture != null) {
            createGameProfile();
        }
    }

    private void createGameProfile() {
        if (this.gameProfile == null) {
            this.gameProfile = new GameProfile(this.uuid, PLAYER_NAME);
            this.gameProfile.properties().put(PROPERTY_KEY, new Property(PROPERTY_KEY, texture));
        }
    }

    void apply(@Nonnull SkullMeta meta) throws NoSuchFieldException, IllegalAccessException, UnknownServerVersionException {
        // setOwnerProfile was added in 1.18, but getOwningPlayer throws a NullPointerException since 1.20.2
        if (MinecraftVersion.get().isAtLeast(MinecraftVersion.parse("1.20"))) {
            PlayerProfile playerProfile = Bukkit.createProfile(this.uuid, PLAYER_NAME);
            PlayerTextures playerTextures = playerProfile.getTextures();
            playerTextures.setSkin(this.skinUrl);
            playerProfile.setTextures(playerTextures);
            meta.setOwnerProfile(playerProfile);
        } else {
            if (this.gameProfile == null) {
                createGameProfile();
            }

            // Forces SkullMeta to properly deserialize and serialize the profile
            ReflectionUtils.setFieldValue(meta, "profile", this.gameProfile);

            meta.setOwningPlayer(meta.getOwningPlayer());

            // Now override the texture again
            ReflectionUtils.setFieldValue(meta, "profile", this.gameProfile);
        }
    }

    /**
     * Get the base64 encoded texture from the underline GameProfile.
     *
     * @return the base64 encoded texture.
     */
    @Nullable
    public String getBase64Texture() {
        return this.texture;
    }

    /**
     * 获取 UUID
     */
    public UUID getId() {
        return this.uuid;
    }

    /**
     * 获取皮肤 URL
     */
    public URL getSkinUrl() {
        return this.skinUrl;
    }

    /**
     * 获取内部的 GameProfile（如果需要）
     */
    @Nullable
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }
}