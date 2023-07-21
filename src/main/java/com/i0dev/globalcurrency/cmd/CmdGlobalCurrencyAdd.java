package com.i0dev.globalcurrency.cmd;

import com.i0dev.globalcurrency.Perm;
import com.i0dev.globalcurrency.engine.EngineLog;
import com.i0dev.globalcurrency.engine.EngineSQL;
import com.i0dev.globalcurrency.entity.MLang;
import com.i0dev.globalcurrency.util.Pair;
import com.i0dev.globalcurrency.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import org.bukkit.entity.Player;

public class CmdGlobalCurrencyAdd extends GlobalCurrencyCommand {

    public CmdGlobalCurrencyAdd() {
        this.addAliases("give");
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(TypeInteger.get(), "amount");
        this.addRequirements(RequirementHasPerm.get(Perm.ADD));
        this.setVisibility(Visibility.SECRET);
    }

    @Override
    public void perform() throws MassiveException {
        Player player = this.readArgAt(0);
        Integer amount = this.readArgAt(1);

        if (amount <= 0) {
            msg(Utils.prefixAndColor(MLang.get().canOnlyUsePositiveNumbers));
            return;
        }

        EngineSQL.get().addAmount(player.getUniqueId(), amount);

        msg(Utils.prefixAndColor(MLang.get().addedToPlayer,
                        new Pair<>("%player%", player.getName()),
                        new Pair<>("%amount%", String.valueOf(amount))
                )
        );

        EngineLog.get().log(sender.getName() + " has added " + amount + " currency to " + player.getName());

    }


}
