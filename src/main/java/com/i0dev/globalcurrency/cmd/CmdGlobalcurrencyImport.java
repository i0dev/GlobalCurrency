package com.i0dev.globalcurrency.cmd;

import com.i0dev.globalcurrency.GlobalCurrencyPlugin;
import com.i0dev.globalcurrency.engine.EngineLog;
import com.i0dev.globalcurrency.engine.EngineSQL;
import com.i0dev.globalcurrency.entity.MLang;
import com.i0dev.globalcurrency.util.Pair;
import com.i0dev.globalcurrency.util.Utils;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.UUID;

public class CmdGlobalcurrencyImport extends GlobalcurrencyCommand {

    public CmdGlobalcurrencyImport() {
        this.addParameter(TypeString.get(), "file");
        this.setVisibility(Visibility.SECRET);
    }

    @Override
    public void perform() throws MassiveException {
        String file = this.readArgAt(0);

        String filePath = GlobalCurrencyPlugin.get().getDataFolder().getPath();

        File f = new File(filePath + "/" + file);

        if (!f.exists()) {
            msg(Utils.prefixAndColor("&cFile not found."));
            return;
        }
        BufferedReader br;
        try {
            FileReader fr = new FileReader(f);
            br = new BufferedReader(fr);
        } catch (Exception e) {
            msg(Utils.prefixAndColor("&cError reading file. Make sure its in the valid format: username or UUID : amount"));
            return;
        }

        String staff = sender.getName();

        // Format is assumed to be:
        // username or UUID : amount
        Bukkit.getScheduler().runTaskAsynchronously(GlobalCurrencyPlugin.get(), () -> {
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    String name = line.split(":")[0].trim();
                    String amount = line.split(":")[1].trim();
                    // if provided UUID
                    if (Utils.isUUID(name)) {
                        EngineSQL.get().addAmount(UUID.fromString(name), Long.parseLong(amount));
                    } else { // if provided username
                        EngineSQL.get().addAmount(Bukkit.getOfflinePlayer(name).getUniqueId(), Long.parseLong(amount));
                    }
                    EngineLog.get().log(staff + " has added " + amount + " currency to " + Bukkit.getOfflinePlayer(name).getName() + " from an import file: " + file);
                }
            } catch (Exception e) {
                msg(Utils.prefixAndColor("&cError reading file. Make sure its in the valid format: username or UUID : amount"));
                e.printStackTrace();
            }
        });
    }
}
