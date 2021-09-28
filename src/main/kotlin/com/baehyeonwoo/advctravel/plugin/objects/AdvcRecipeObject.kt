package com.baehyeonwoo.advctravel.plugin.objects

import com.baehyeonwoo.advctravel.plugin.AdvcTravelMain
import net.kyori.adventure.text.Component.text
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.plugin.Plugin

object AdvcRecipeObject {
    private fun getInstance(): Plugin {
        return AdvcTravelMain.instance
    }

    fun firework(): Recipe {
        val key = NamespacedKey(getInstance(), "hunter_firework")

        var item = ItemStack(Material.FIREWORK_ROCKET, 3)
        val meta = item.itemMeta as FireworkMeta

        meta.power = 1
        meta.lore(listOf(
            text("헌터 전용 폭죽")
        ))

        item.itemMeta = meta

        val recipe = ShapedRecipe(key, item)

        recipe.shape("G", "A", "P")

        recipe.setIngredient('G', Material.GUNPOWDER)
        recipe.setIngredient('A', Material.ARROW)
        recipe.setIngredient('P', Material.PAPER)

        return recipe
    }
}