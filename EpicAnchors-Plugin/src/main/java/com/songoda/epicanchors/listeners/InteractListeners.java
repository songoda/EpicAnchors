package com.songoda.epicanchors.listeners;

import com.songoda.epicanchors.EpicAnchorsPlugin;
import com.songoda.epicanchors.anchor.EAnchor;
import com.songoda.epicanchors.api.anchor.Anchor;
import com.songoda.epicanchors.utils.Methods;
import com.songoda.epicanchors.utils.ServerVersion;
import com.songoda.epicanchors.utils.version.NMSUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListeners implements Listener {

    private EpicAnchorsPlugin instance;

    public InteractListeners(EpicAnchorsPlugin instance) {
        this.instance = instance;
    }


    @EventHandler(ignoreCancelled = true)
    public void onBlockInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        if (instance.getAnchorManager().getAnchor(event.getClickedBlock().getLocation()) == null) return;

        if (!instance.canBuild(event.getPlayer(), event.getClickedBlock().getLocation())) {
            event.setCancelled(true);
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            instance.bust(event.getClickedBlock().getLocation());
            event.setCancelled(true);
            return;
        }


        Anchor anchor = instance.getAnchorManager().getAnchor(event.getClickedBlock().getLocation());


        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();

        if (item.getType() == (instance.isServerVersionAtLeast(ServerVersion.V1_13) ? Material.ENDER_EYE : Material.valueOf("EYE_OF_ENDER"))
                && Material.valueOf(instance.getConfig().getString("Main.Anchor Block Material")) == (instance.isServerVersionAtLeast(ServerVersion.V1_13) ? Material.END_PORTAL_FRAME : Material.valueOf("ENDER_PORTAL_FRAME"))) {
            event.setCancelled(true);
            return;
        }

        if (item.getType() == Material.valueOf(instance.getConfig().getString("Main.Anchor Block Material"))) {
            if (instance.getTicksFromItem(item) == 0) return;

            anchor.setTicksLeft(anchor.getTicksLeft() + instance.getTicksFromItem(item));

            if (player.getGameMode() != GameMode.CREATIVE)
                Methods.takeItem(player, 1);

            Sound sound = NMSUtil.getVersionNumber() > 8 ? Sound.ENTITY_PLAYER_LEVELUP : Sound.valueOf("LEVEL_UP");
            player.playSound(player.getLocation(), sound, 0.6F, 15.0F);

            if (NMSUtil.getVersionNumber() > 8)
                player.getWorld().spawnParticle(Particle.SPELL_WITCH, anchor.getLocation().add(.5, .5, .5), 100, .5, .5, .5);

            event.setCancelled(true);

            return;
        }

        ((EAnchor) anchor).overview(player);

    }

}
