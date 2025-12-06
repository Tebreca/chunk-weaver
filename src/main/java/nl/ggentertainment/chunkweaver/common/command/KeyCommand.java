package nl.ggentertainment.chunkweaver.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import nl.ggentertainment.chunkweaver.ChunkWeaver;
import nl.ggentertainment.chunkweaver.common.core.classes.PlayerClass;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.EngineerHelper;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.key.DynamicVaultHolder;

public class KeyCommand {
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("key")//
                .then(Commands.literal("generate")
                        .executes(KeyCommand::generate)
                        .then(Commands.argument("name", StringArgumentType.greedyString()).requires(CommandSourceStack::isPlayer).executes(KeyCommand::generate)))//
                .then(Commands.literal("info").requires(CommandSourceStack::isPlayer).executes(KeyCommand::info))
        );
    }

    private static int info(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        return 0;
    }

    private static int generate(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        ServerPlayer player = commandSourceStackCommandContext.getSource().getPlayer();
        assert player != null;
        if (player.getData(ChunkWeaver.CLASS_ATTACHMENT) == PlayerClass.ENGINEER && player instanceof DynamicVaultHolder provider && provider.hasVault()) {
            provider.getVault().ifPresent(vault -> {
                String name = commandSourceStackCommandContext.getArgument("name", String.class);
                EngineerHelper.insertKey(vault, name == null || name.isEmpty() ? Component.translatable("item.chunkweaver.key") : Component.literal(name));
            });
        }
        return Command.SINGLE_SUCCESS;
    }

}
