package dev.anvilcraft.rg.survival.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import dev.anvilcraft.rg.survival.util.WanderingTraderPurchasedUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(WanderingTrader.class)
abstract class WanderingTraderMixin extends AbstractVillager {
    public WanderingTraderMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "updateTrades",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/WanderingTrader;addOffersFromItemListings(Lnet/minecraft/world/item/trading/MerchantOffers;[Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;I)V"
        )
    )
    private void updateTrades(CallbackInfo ci, @Local @NotNull MerchantOffers merchantoffers) {
        if (!SurvivalPlusPlusServerRules.wanderingTraderPurchased) return;
        MerchantOffer[] trades = WanderingTraderPurchasedUtil.getRandomTrades(this.random);
        merchantoffers.addAll(Arrays.asList(trades));
    }
}
