package com.i0dev.cmd;

import com.i0dev.Perm;
import com.i0dev.engine.EngineLog;
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

public class CmdGlobalCurrencySet extends GlobalCurrencyCommand {

    public CmdGlobalCurrencySet() {
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(TypeInteger.get(), "amount");
        this.addRequirements(RequirementHasPerm.get(Perm.SET));
        this.setVisibility(Visibility.SECRET);
    }

    @Override
    public void perform() throws MassiveException {
        Player player = this.readArgAt(0);
        Integer amount = this.readArgAt(1);

        EngineSQL.get().setAmount(player.getUniqueId(), amount);

        msg(Utils.prefixAndColor(MLang.get().setPlayerBalance,
                        new Pair<>("%player%", player.getName()),
                        new Pair<>("%amount%", String.valueOf(amount))
                )
        );

        EngineLog.get().log(sender.getName() + " has set " + amount + " currency to " + player.getName());
    }


}
