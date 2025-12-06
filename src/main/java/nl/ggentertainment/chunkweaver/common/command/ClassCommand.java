package nl.ggentertainment.chunkweaver.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.SkillsAPI;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;
import nl.ggentertainment.chunkweaver.common.network.PickClassPacket;

import java.util.Optional;

import static net.minecraft.util.CommonColors.RED;
import static nl.ggentertainment.chunkweaver.ChunkWeaver.CLASS_ATTACHMENT;
import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class ClassCommand {

    public static void register(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("class");
        root.then(
                Commands.literal("switch").then(
                        Commands.argument("confirm", BoolArgumentType.bool()).executes(ClassCommand::switchClass)
                ).executes(ClassCommand::confirm)
        );
        root.then(
                Commands.literal("info").executes(ClassCommand::info)
        );
        event.getDispatcher().register(root);
    }

    private static int info(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        commandSourceStackCommandContext.getSource().getPlayer().displayClientMessage(Component.translatable("message.chunkweaver.class.info"), false);
        return 0;
    }

    private static int switchClass(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        ServerPlayer player = commandSourceStackCommandContext.getSource().getPlayer();
        if (player == null)
            return 0;
        if (commandSourceStackCommandContext.getArgument("confirm", Boolean.class)) {
            Optional<Category> category = SkillsAPI.getCategory(ResourceLocation.fromNamespaceAndPath(MOD_ID, player.getData(CLASS_ATTACHMENT).name().toLowerCase()));
            if (category.isPresent()) {
                Category category1 = category.get();
                category1.erase(player);
                PacketDistributor.sendToPlayer(player, new PickClassPacket.data(PlayerClass.FARMER));
            }
            return Command.SINGLE_SUCCESS;
        } else {
            player.sendSystemMessage(Component.translatable("chunkweaver.command.confirm_switch").withColor(RED));
        }
        return 0;
    }

    private static int confirm(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {

        ServerPlayer player = commandSourceStackCommandContext.getSource().getPlayer();
        if (player == null)
            return 0;
        player.sendSystemMessage(Component.translatable("chunkweaver.command.confirm_switch").withColor(RED));
        return Command.SINGLE_SUCCESS;
    }
}
