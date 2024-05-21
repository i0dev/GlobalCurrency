package com.i0dev.globalcurrency.cmd;

import com.i0dev.globalcurrency.Perm;
import com.i0dev.globalcurrency.cmd.type.TypeOfflinePlayer;
import com.i0dev.globalcurrency.engine.EngineSQL;
import com.i0dev.globalcurrency.entity.MLang;
import com.i0dev.globalcurrency.util.Pair;
import com.i0dev.globalcurrency.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CmdGlobalCurrencyBalance extends GlobalCurrencyCommand {

    public CmdGlobalCurrencyBalance() {
        this.addParameter(TypeOfflinePlayer.get(), "player").setDefaultValue(null);
        this.addAliases("bal");
        this.addRequirements(RequirementHasPerm.get(Perm.BALANCE));
    }

    @Override
    public void perform() throws MassiveException {
        OfflinePlayer player = this.readArg();

        if (player == null && sender instanceof ConsoleCommandSender) {
            msg("&cYou must specify a player.");
            return;
        }

        if (player == null || player == me) {
            long balance = EngineSQL.get().getAmount(((Player) sender).getUniqueId());
            msg(Utils.prefixAndColor(MLang.get().yourBalance,
                            new Pair<>("%amount%", String.valueOf(balance))
                    )
            );
            return;
        }

        if (Perm.BALANCEOTHERS.has(sender, true)) {
            long balance = EngineSQL.get().getAmount(player.getUniqueId());
            msg(Utils.prefixAndColor(MLang.get().playerBalance,
                            new Pair<>("%player%", player.getName()),
                            new Pair<>("%amount%", String.valueOf(balance))
                    )
            );
        }

    }
}
