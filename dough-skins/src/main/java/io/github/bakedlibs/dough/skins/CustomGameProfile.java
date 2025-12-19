package io.github.bakedlibs.dough.skins;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URL;
import java.util.UUID;

public class CustomGameProfile {

    private static final String PLAYER_NAME = "CS-CoreLib";

    private final UUID uuid;
    private final String base64Texture;
    private final URL skinUrl;
    private final PlayerProfile playerProfile;

    CustomGameProfile(@Nonnull UUID uuid, @Nullable String base64Texture, @Nonnull URL url) {
        this.uuid = uuid;
        this.base64Texture = base64Texture;
        this.skinUrl = url;
        this.playerProfile = createPlayerProfile();
    }

    @Nonnull
    private PlayerProfile createPlayerProfile() {
        PlayerProfile profile = Bukkit.createProfile(this.uuid, PLAYER_NAME);
        PlayerTextures textures = profile.getTextures();

        // 关键：使用传入的 URL 设置皮肤
        textures.setSkin(this.skinUrl);
        profile.setTextures(textures);

        return profile;
    }

    void apply(@Nonnull SkullMeta meta) {
        meta.setOwnerProfile(this.playerProfile);
    }

    @Nullable
    public String getBase64Texture() {
        return this.base64Texture;
    }

    @Nonnull
    public UUID getId() {
        return this.uuid;
    }

    @Nonnull
    public URL getSkinUrl() {
        return this.skinUrl;
    }

    @Nonnull
    public PlayerProfile getPlayerProfile() {
        return this.playerProfile;
    }
}