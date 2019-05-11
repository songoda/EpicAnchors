package com.songoda.epicanchors.command.commands;

import com.songoda.epicanchors.EpicAnchorsPlugin;
import com.songoda.epicanchors.command.AbstractCommand;
import com.songoda.epicanchors.utils.Methods;
import org.bukkit.command.CommandSender;

public class CommandEpicAnchors extends AbstractCommand {

    public CommandEpicAnchors() {
        super("EpicAnchors", null, false);
    }

    @Override
    protected ReturnType runCommand(EpicAnchorsPlugin instance, CommandSender sender, String... args) {
        sender.sendMessage("");
        sender.sendMessage(Methods.formatText(instance.getReferences().getPrefix() + "&7Version " + instance.getDescription().getVersion() + " Created with <3 by &5&l&oBrianna"));

        for (AbstractCommand command : instance.getCommandManager().getCommands()) {
            if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
                sender.sendMessage(Methods.formatText("&8 - &a" + command.getSyntax() + "&7 - " + command.getDescription()));
            }
        }
        sender.sendMessage("");

        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/EpicAnchors";
    }

    @Override
    public String getDescription() {
        return "Displays this page.";
    }
}
