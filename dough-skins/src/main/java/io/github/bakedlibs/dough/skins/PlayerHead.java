package io.github.bakedlibs.dough.skins;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.lang.Validate;
import java.util.function.Consumer;

public final class PlayerHead {

    private PlayerHead() {}

    public static @Nonnull ItemStack getItemStack(@Nonnull OfflinePlayer player) {
        Validate.notNull(player, "The player can not be null!");
        return getItemStack(meta -> meta.setOwningPlayer(player));
    }

    public static @Nonnull ItemStack getItemStack(@Nonnull PlayerSkin skin) {
        Validate.notNull(skin, "The skin can not be null!");
        return getItemStack(meta -> {
            skin.getProfile().apply(meta);
        });
    }

    private static @Nonnull ItemStack getItemStack(@Nonnull Consumer<SkullMeta> consumer) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        consumer.accept(meta);
        item.setItemMeta(meta);
        return item;
    }

    @ParametersAreNonnullByDefault
    public static void setSkin(Block block, PlayerSkin skin, boolean sendBlockUpdate) {
        Validate.notNull(block, "Block cannot be null");
        Validate.notNull(skin, "Skin cannot be null");

        Material material = block.getType();
        if (material != Material.PLAYER_HEAD && material != Material.PLAYER_WALL_HEAD) {
            throw new IllegalArgumentException("Cannot update a head texture. Expected a Player Head, received: " + material);
        }

        if (!(block.getState() instanceof Skull skullBlock)) {
            System.err.println("Could not update the skin of a player head. The block state is not a skull.");
            return;
        }
        skullBlock.setOwnerProfile(skin.getProfile().getPlayerProfile());
        skullBlock.update(true, sendBlockUpdate);
    }
}