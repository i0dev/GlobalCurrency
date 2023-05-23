package com.i0dev.cmd;

import com.i0dev.Perm;
import com.i0dev.engine.EngineSQL;
import com.i0dev.entity.MLang;
import com.i0dev.util.Pair;
import com.i0dev.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;

public class CmdGlobalCurrencyBalance extends GlobalCurrencyCommand {

    public CmdGlobalCurrencyBalance() {
        this.addParameter(TypePlayer.get(), "player").setDefaultValue(null);
        this.addAliases("bal");
        this.addRequirements(RequirementHasPerm.get(Perm.BALANCE));
    }

    @Override
    public void perform() throws MassiveException {
        Player player = this.readArg();

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
