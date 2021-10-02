package com.baehyeonwoo.advctravel.plugin.events

import com.baehyeonwoo.advctravel.plugin.AdvcTravelMain
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.EntityResurrectEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import kotlin.random.Random

class AdvcBanItemEvent: Listener {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    private val server = getInstance().server

    private val config = getInstance().config

    private val administrator = requireNotNull(config.getString("administrator").toString())

    private val runner = requireNotNull(config.getString("runner").toString())

    private val adminTeam = server.scoreboardManager.mainScoreboard.getTeam("Admin")

    // 코드를 읽을 줄 아시는 개발자분들은 대충 보고 넘어가주세요 ;ㅁ;

    @EventHandler
    fun onPlayerCraft(e: CraftItemEvent){
        val p = e.whoClicked
        val item = e.recipe.result

        if (p.uniqueId.toString() !in runner && p.uniqueId.toString() !in administrator) {
            when (item.type){
                Material.FIREWORK_ROCKET -> e.isCancelled = !item.itemMeta.hasLore()
                Material.END_CRYSTAL -> e.isCancelled = true
                else -> e.isCancelled = false
            }
        }
    }

    @EventHandler
    fun onBreakBlock(e: BlockBreakEvent){
        val p = e.player
        val b = e.block

        if (p.uniqueId.toString() !in runner && p.uniqueId.toString() !in administrator) {
            if(b.type == Material.ENDER_CHEST ||  b.type.toString().endsWith("SHULKER_BOX")){
                e.isDropItems = false
            }
        }
    }

    @EventHandler
    fun onPlaceBlock(e: BlockPlaceEvent){
        val p = e.player
        val b = e.block

        if (p.uniqueId.toString() !in runner && p.uniqueId.toString() !in administrator) {
            if(p.world == Bukkit.getWorld("world_the_end")){
                if(b.type == Material.OBSIDIAN){
                    e.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun onEntityExplode(e: EntityExplodeEvent){
        e.blockList().removeAll { x -> x.blockData.material.toString().endsWith("SHULKER_BOX") }
    }

    @EventHandler
    fun onPlayerUseBucket(e: PlayerBucketEmptyEvent){
        val p = e.player
        val b = e.bucket

        if (p.uniqueId.toString() !in runner && p.uniqueId.toString() !in administrator) {
            if(p.world == Bukkit.getWorld("world_the_end")) {
                if (b == Material.LAVA_BUCKET) {
                    e.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun onPlayerResurrect(e: EntityResurrectEvent){
        val p = e.entity

        if (p is Player){
            if (p.uniqueId.toString() !in runner && p.uniqueId.toString() !in administrator) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onEntityDeath(e: EntityDeathEvent){
        val dead = e.entity
        val p = dead.killer

        if (p is Player) {
            if (p.uniqueId.toString() !in runner && p.uniqueId.toString() !in administrator) {
                if (dead.type == EntityType.EVOKER) {
                    e.drops.clear()
                    e.drops.add(ItemStack(Material.EMERALD, Random.nextInt(3) +1))
                }
            }
        }
    }

    @EventHandler
    fun onEntityDamagedByEntity(e: EntityDamageByEntityEvent){
        val entity = e.entity
        val damager = e.damager

        if(damager is Player){
            if (damager.uniqueId.toString() !in runner && damager.uniqueId.toString() !in administrator) {
                if(entity.type == EntityType.ENDER_CRYSTAL){
                    e.isCancelled = true
                }
            }
        }
    }
}