package com.i0dev.globalcurrency.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * This is a chainable builder for {@link ItemStack}s in {@link Bukkit}
 * <br>
 * Example Usage:<br>
 * {@code ItemStack is = new ItemBuilder(Material.LEATHER_HELMET).amount(2).data(4).durability(4).enchantment(Enchantment.ARROW_INFINITE).enchantment(Enchantment.LUCK, 2).name(ChatColor.RED + "the name").lore(ChatColor.GREEN + "line 1").lore(ChatColor.BLUE + "line 2").color(Color.MAROON);
 *
 * @author MiniDigger, computerwizjared
 * @version 1.2
 */
public class ItemBuilder extends ItemStack {
    /**
     * Initializes the builder with the given {@link Material}
     *
     * @param mat the {@link Material} to start the builder from
     * @since 1.0
     */
    public ItemBuilder(final Material mat) {
        super(mat);
    }

    /**
     * Inits the builder with the given {@link ItemStack}
     *
     * @param is the {@link ItemStack} to start the builder from
     * @since 1.0
     */
    public ItemBuilder(final ItemStack is) {
        super(is);
    }

    /**
     * Changes the amount of the {@link ItemStack}
     *
     * @param amount the new amount to set
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder amount(final int amount) {
        setAmount(amount);
        return this;
    }

    /**
     * Changes the display name of the {@link ItemStack}
     *
     * @param name the new display name to set
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder name(final String name) {
        final ItemMeta meta = getItemMeta();
        meta.setDisplayName(Utils.color(name));
        setItemMeta(meta);
        return this;
    }

    /**
     * Adds a new line to the lore of the {@link ItemStack}
     *
     * @param texts the new line to add
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder setNewLore(final List<String> texts) {
        final ItemMeta meta = getItemMeta();
        List<Component> lore = meta.lore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        if (texts != null) {
            for (String s : texts) {
                lore.add(Component.text(Utils.color(s)));
            }
        }

        meta.lore(lore);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder hideAllAttributes() {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        meta.addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS);
        setItemMeta(meta);
        return this;
    }

    /**
     * Changes the durability of the {@link ItemStack}
     *
     * @param durability the new durability to set
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder durability(final int durability) {
        setDurability((short) durability);
        return this;
    }

    /**
     * Adds an {@link Enchantment} with the given level to the {@link ItemStack}
     *
     * @param enchantment the enchantment to add
     * @param level       the level of the enchantment
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder enchantment(final Enchantment enchantment, final int level) {
        addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Adds an {@link Enchantment} with the level 1 to the {@link ItemStack}
     *
     * @param enchantment the enchantment to add
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder enchantment(final Enchantment enchantment) {
        if (enchantment == null) {
            return this;
        }
        addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    /**
     * Changes the {@link Material} of the {@link ItemStack}
     *
     * @param material the new material to set
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder type(final Material material) {
        setType(material);
        return this;
    }

    /**
     * Clears the lore of the {@link ItemStack}
     *
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder clearLore() {
        final ItemMeta meta = getItemMeta();
        meta.setLore(new ArrayList<>());
        setItemMeta(meta);
        return this;
    }

    /**
     * Clears the list of {@link Enchantment}s of the {@link ItemStack}
     *
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder clearEnchantments() {
        getEnchantments().keySet().forEach(this::removeEnchantment);
        return this;
    }

    /**
     * Sets the {@link Color} of a part of leather armor
     *
     * @param color the {@link Color} to use
     * @return this builder for chaining
     * @since 1.1
     */
    public ItemBuilder color(Color color) {
        if (getType() == Material.LEATHER_BOOTS || getType() == Material.LEATHER_CHESTPLATE || getType() == Material.LEATHER_HELMET
                || getType() == Material.LEATHER_LEGGINGS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
            meta.setColor(color);
            setItemMeta(meta);
            return this;
        } else {
            throw new IllegalArgumentException("color() only applicable for leather armor!");
        }
    }

    public ItemBuilder addEncs(Map<String, Integer> enchantmentIntegerMap) {
        for (String enchantment : enchantmentIntegerMap.keySet()) {
            System.out.println(enchantment);
            System.out.println(Enchantment.getByName(enchantment).getMaxLevel());
            addUnsafeEnchantment(Enchantment.getByName(enchantment), enchantmentIntegerMap.get(enchantment));
        }

        return this;
    }

    public ItemBuilder addEnc(Enchantment enchantment, int level) {
        addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addGlow(final boolean glow) {
        ItemMeta meta = getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(NamespacedKey.minecraft("enchantment_glint_override"), PersistentDataType.BOOLEAN, glow);
        if (glow) {
            meta.addEnchant(Enchantment.INFINITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS);
            setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder unbreakable() {
        ItemMeta im = this.getItemMeta();
        im.setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        setItemMeta(im);
        return this;
    }

}