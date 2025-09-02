package dev.anvilcraft.rg.survival.event.listener;

import dev.anvilcraft.rg.api.event.ServerPlayerChatEvent;
import dev.anvilcraft.rg.api.server.TranslationUtil;
import dev.anvilcraft.rg.survival.SurvivalPlusPlus;
import dev.anvilcraft.rg.survival.SurvivalPlusPlusServerRules;
import dev.anvilcraft.rg.survival.event.PlayerCanPlaceBlockItemEvent;
import dev.anvilcraft.rg.survival.event.PlayerCanPlaceStandingAndWallBlockItemEvent;
import dev.anvilcraft.rg.survival.event.PlayerDeathEvent;
import dev.anvilcraft.rg.survival.mixin.BlockItemAccessor;
import dev.anvilcraft.rg.survival.util.IPlayerData;
import dev.anvilcraft.rg.survival.util.SimpleInGameCalculator;
import dev.anvilcraft.rg.tools.TriConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = SurvivalPlusPlus.MOD_ID)
public class PlayerEventListener {
    @SubscribeEvent
    private static void onPlayerDeath(PlayerDeathEvent event) {
        if (!SurvivalPlusPlusServerRules.broadcastDeathPosition) return;
        Player entity = event.getEntity();
        if (!(entity instanceof ServerPlayer player)) return;
        MinecraftServer server = player.getServer();
        if (server == null) return;
        PlayerList playerList = server.getPlayerList();
        Component pos = TranslationUtil.trans(
                "broadcast_death_position.message.position",
                player.getOnPos().getX(),
                player.getOnPos().getY(),
                player.getOnPos().getZ()
            )
            .withStyle(Style.EMPTY.applyFormats(ChatFormatting.DARK_GREEN)
                .withHoverEvent(new HoverEvent.ShowText(TranslationUtil.trans("broadcast_death_position.message.position.hover")))
                .withClickEvent(new ClickEvent.CopyToClipboard("/tp @s %s %s %s".formatted(
                    player.getOnPos().getX(),
                    player.getOnPos().getY(),
                    player.getOnPos().getZ()
                ))));
        Component component = TranslationUtil.trans("broadcast_death_position.message", player.getDisplayName(), pos);
        playerList.broadcastSystemMessage(component, false);
    }

    @SubscribeEvent
    public static void onPlayerChat(@NotNull ServerPlayerChatEvent event) {
        ServerPlayer player = event.getEntity();
        Component component = event.getComponent();
        PlayerEventListener.handleChat(
            SurvivalPlusPlusServerRules.simpleInGameCalculator,
            "==",
            player,
            component,
            SimpleInGameCalculator::handleChat
        );
    }

    @SubscribeEvent
    public static void onPlayerCanPlace(@NotNull PlayerCanPlaceBlockItemEvent event) {
        if (event.canPlace()) return;
        BlockItem item = event.getItem();
        BlockPlaceContext context = event.getContext();
        BlockState state = event.getState();
        Player player = context.getPlayer();
        CollisionContext collisioncontext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
        boolean flag = (
                           !((BlockItemAccessor) item).invokeMustSurvive() || state.canSurvive(context.getLevel(), context.getClickedPos())
                       ) && PlayerEventListener.canSpectatingPlace(
            context.getLevel(),
            state,
            context.getClickedPos(),
            collisioncontext,
            context
        );
        event.setCanPlace(flag);
    }

    @SubscribeEvent
    public static void onPlayerCanPlace(@NotNull PlayerCanPlaceStandingAndWallBlockItemEvent event) {
        if (event.canPlace()) return;
        BlockPlaceContext context = event.getContext();
        BlockState state = event.getState();
        Player player = context.getPlayer();
        LevelReader reader = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (SurvivalPlusPlusServerRules.creativeNoClip && player != null && player.isCreative() && player.getAbilities().flying) {
            VoxelShape voxelShape = state.getCollisionShape(reader, pos, CollisionContext.empty());
            if (voxelShape.isEmpty() || reader.isUnobstructed(player, voxelShape.move(pos.getX(), pos.getY(), pos.getZ()))) {
                event.setCanPlace(true);
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("resource")
    public static void onTick(PlayerTickEvent.@NotNull Pre event) {
        if (SurvivalPlusPlusServerRules.easyBoneMeal < 0) return;
        Player entity = event.getEntity();
        Level level = entity.level();
        if (level.isClientSide()) return;
        if (!(entity instanceof IPlayerData data)) return;
        boolean last = data.<Boolean>rg$getPlayerData("easy_bone_meal", false);
        if (last) {
            if (!entity.isShiftKeyDown()) data.rg$savePlayerData("easy_bone_meal", false);
            return;
        }
        if (!entity.isShiftKeyDown()) return;
        data.rg$savePlayerData("easy_bone_meal", true);
        double chance = SurvivalPlusPlusServerRules.easyBoneMeal == 0 ? 1.0 : 1.0 / SurvivalPlusPlusServerRules.easyBoneMeal;
        if (Math.random() > chance) return;
        BlockPos onPos = entity.getOnPos();
        BlockState state = level.getBlockState(onPos);
        if (!state.is(Blocks.COMPOSTER)) return;
        int value = state.getValue(ComposterBlock.LEVEL);
        int nextValue = Math.min(value + 1, ComposterBlock.MAX_LEVEL);
        if (nextValue <= value) return;
        level.setBlock(onPos, state.setValue(ComposterBlock.LEVEL, nextValue), 3);
    }

    private static boolean canSpectatingPlace(
        Level world,
        BlockState state,
        BlockPos pos,
        CollisionContext context,
        @NotNull BlockPlaceContext contextOuter
    ) {
        Player player = contextOuter.getPlayer();
        if (SurvivalPlusPlusServerRules.creativeNoClip && player != null && player.isCreative() && player.getAbilities().flying) {
            // copy from canPlace
            VoxelShape voxelShape = state.getCollisionShape(world, pos, context);
            if (voxelShape.isEmpty() || world.isUnobstructed(player, voxelShape.move(pos.getX(), pos.getY(), pos.getZ()))) {
                return true;
            }
        }
        return world.isUnobstructed(state, pos, context);
    }

    private static void handleChat(
        boolean rule,
        @SuppressWarnings("SameParameterValue") String prefix,
        ServerPlayer player,
        Component component,
        TriConsumer<MinecraftServer, ServerPlayer, String> handle
    ) {
        if (!rule) return;
        String string = component.getString();
        if (!string.startsWith(prefix)) return;
        string = string.substring(prefix.length());
        MinecraftServer server = player.getServer();
        handle.accept(server, player, string);
    }
}
