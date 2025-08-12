package com.ducishere.hyperworldgen.server;

import com.ducishere.hyperworldgen.common.reward.RewardNetwork;
import com.ducishere.hyperworldgen.common.util.PlayerData;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ChallengeCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("challenge")
                .then(CommandManager.literal("test")
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        ServerPlayerEntity player = source.getPlayer();

                        if (player != null) {
                            // Lấy data của player
                            PlayerData data = PlayerData.load(player);

                            // Thêm coin & lưu
                            data.addCryoCoins(10);
                            PlayerData.save(player, data);

                            // Gửi quà
                            ItemStack rewardItem = new ItemStack(Items.DIAMOND, 3);
                            player.getInventory().insertStack(rewardItem);

                            // Gửi UI popup
                            RewardNetwork.sendRewardNotification(player, rewardItem, "Nhận 3 Kim Cương!");

                            source.sendFeedback(Text.literal("§aĐã test reward popup!"), false);
                        }

                        return 1;
                    })
                )
            );
        });
    }
}
