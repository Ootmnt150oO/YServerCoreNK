package Yuziouo.ServerCore.GradeSystem;

import Yuziouo.ServerCore.FormIDs;
import Yuziouo.ServerCore.ServerCore;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindowCustom;

import java.util.ArrayList;
import java.util.Arrays;

public class GradeForm implements Listener {
    public static void GradeMenu(Player player){
        FormWindowCustom custom = new FormWindowCustom("管理員玩家等級設定系統");
        ArrayList<String> list = new ArrayList<>();
        for (Player player1:ServerCore.getPlugin().getServer().getOnlinePlayers().values())
            list.add(player1.getName());
        custom.addElement(new ElementDropdown("請選擇玩家", list));
        custom.addElement(new ElementDropdown("請選擇使用類型", Arrays.asList("設定玩家等級","設定玩家經驗","增加玩家等級","增加玩家經驗")));
        custom.addElement(new ElementInput("請輸入數量"));
        player.showFormWindow(custom, FormIDs.GradeUI.getId());
    }
    @EventHandler
    public void onFormRec(PlayerFormRespondedEvent event){
        Player player = event.getPlayer();
        int id = event.getFormID();
        if (event.wasClosed()) return;
        if (id == FormIDs.GradeUI.getId()){
            FormResponseCustom response = (FormResponseCustom) event.getResponse();
            Player target = ServerCore.getPlugin().getServer().getPlayer(response.getDropdownResponse(0).getElementContent());
            int count = Integer.parseInt(response.getInputResponse(2));
            switch (response.getDropdownResponse(1).getElementContent()){
                case "設定玩家等級":
                    ServerCore.getPlugin().getGradeHashMap().get(target.getUniqueId().toString()).setGrade(count);
                    break;
                case "設定玩家經驗":
                    ServerCore.getPlugin().getGradeHashMap().get(target.getUniqueId().toString()).setExp(count);
                    break;
                case "增加玩家等級":
                    ServerCore.getPlugin().getServer().getPluginManager().callEvent(new PlayerAddGradeEvent(target,count));
                    ServerCore.getPlugin().getGradeHashMap().get(target.getUniqueId().toString()).addGrade(count);
                    break;
                case "增加玩家經驗":
                    ServerCore.getPlugin().getServer().getPluginManager().callEvent(new PlayerAddExpEvent(target,count));
                    ServerCore.getPlugin().getGradeHashMap().get(target.getUniqueId().toString()).addExp(count);
                    break;
            }
            player.sendMessage("操作完成");
        }
    }
}
