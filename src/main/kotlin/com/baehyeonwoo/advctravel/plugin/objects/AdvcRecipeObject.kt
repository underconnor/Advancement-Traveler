package com.baehyeonwoo.advctravel.plugin.objects

import com.baehyeonwoo.advctravel.plugin.AdvcTravelMain
import net.kyori.adventure.text.Component.text
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.plugin.Plugin

object AdvcRecipeObject {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    fun elytra(): Recipe{
        val key = NamespacedKey(getInstance(), "hunter_elytra")

        val item = ItemStack(Material.ELYTRA)
        val meta = item.itemMeta as Damageable

        meta.damage = Material.ELYTRA.maxDurability - 1

        item.itemMeta = meta

        val recipe = ShapedRecipe(key, item)

        recipe.shape("PAP", "PPP", "APA")

        recipe.setIngredient('A', Material.AIR)
        recipe.setIngredient('P', Material.PHANTOM_MEMBRANE)


        return recipe
    }
}