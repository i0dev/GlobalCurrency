package com.i0dev.globalcurrency.cmd;

import com.i0dev.globalcurrency.cmd.type.TypeOfflinePlayer;
import com.i0dev.globalcurrency.engine.EngineLog;
import com.i0dev.globalcurrency.engine.EngineSQL;
import com.i0dev.globalcurrency.entity.MLang;
import com.i0dev.globalcurrency.util.Pair;
import com.i0dev.globalcurrency.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import org.bukkit.OfflinePlayer;

public class CmdGlobalcurrencyAdd extends GlobalcurrencyCommand {

    public CmdGlobalcurrencyAdd() {
        this.addParameter(TypeOfflinePlayer.get(), "player");
        this.addParameter(TypeInteger.get(), "amount");
        this.setVisibility(Visibility.SECRET);
    }

    @Override
    public void perform() throws MassiveException {
        OfflinePlayer player = this.readArgAt(0);
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
