package dev.anvilcraft.rg.survival.util;

import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class WanderingTraderPurchasedUtil {
    @SuppressWarnings("deprecation")
    private static final List<Offer> trades = List.of(
        new Offer(
            new ItemCost(
                Items.POTION.builtInRegistryHolder(),
                1,
                DataComponentPredicate.builder()
                    .expect(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER))
                    .build()
            ),
            Optional.empty(),
            new ItemStack(Items.EMERALD, 1),
            2,
            5,
            1
        ),
        new Offer(new ItemCost(Items.WATER_BUCKET, 1), Optional.empty(), new ItemStack(Items.EMERALD, 2), 2, 5, 1),
        new Offer(new ItemCost(Items.MILK_BUCKET, 1), Optional.empty(), new ItemStack(Items.EMERALD, 2), 2, 5, 1),
        new Offer(new ItemCost(Items.FERMENTED_SPIDER_EYE, 1), Optional.empty(), new ItemStack(Items.EMERALD, 1), 2, 5, 1),
        new Offer(new ItemCost(Items.BAKED_POTATO, 4), Optional.empty(), new ItemStack(Items.EMERALD, 1), 2, 5, 1),
        new Offer(new ItemCost(Items.HAY_BLOCK, 1), Optional.empty(), new ItemStack(Items.EMERALD, 1), 2, 5, 1)
    );

    public static MerchantOffer @NotNull [] getRandomTrades(@NotNull RandomSource random) {
        int trade1 = random.nextInt(trades.size());
        int trade2 = random.nextInt(trades.size());
        while (trade1 == trade2) {
            trade2 = random.nextInt(trades.size());
        }
        return new MerchantOffer[]{
            trades.get(trade1).toOffer(),
            trades.get(trade2).toOffer()
        };
    }

    record Offer(ItemCost baseCostA, Optional<ItemCost> costB, ItemStack result, int maxUses, int xp, float priceMultiplier) {
        public @NotNull MerchantOffer toOffer() {
            return new MerchantOffer(baseCostA, costB, result, maxUses, xp, priceMultiplier);
        }
    }
}
