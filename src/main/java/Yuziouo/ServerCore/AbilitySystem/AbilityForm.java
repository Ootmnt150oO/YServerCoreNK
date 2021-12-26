package Yuziouo.ServerCore.AbilitySystem;

import Yuziouo.ServerCore.FormIDs;
import Yuziouo.ServerCore.MySQL;
import Yuziouo.ServerCore.ServerCore;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementStepSlider;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbilityForm implements Listener {
    public static void OpMenu(Player player){
        FormWindowCustom custom = new FormWindowCustom("管理員天賦設定菜單");
        ArrayList<String> list = new ArrayList<>();
        for (Player player1: ServerCore.getPlugin().getServer().getOnlinePlayers().values())
            list.add(player1.getName());
        custom.addElement(new ElementDropdown("請選擇玩家", list));
        custom.addElement(new ElementStepSlider("請選擇操作方式",Arrays.asList("設定","增加")));
        custom.addElement(new ElementDropdown("請選擇使用類型", Arrays.asList("點數","力量","防禦力","身高","速度","挖礦速度","血量")));
        custom.addElement(new ElementInput("請輸入數量"));
        player.showFormWindow(custom, FormIDs.AbilityOPUI.getId());
    }
    public static void PlayerMenu(Player player){
        FormWindowSimple simple = new FormWindowSimple("天賦系統","當前剩餘天賦點數"+ServerCore.getPlugin().getAbilityHashMap().get(player.getUniqueId().toString()).getPoint());
        List<String> list = Arrays.asList("力量","防禦力","身高","速度","挖礦速度","血量");
        simple.addButton(new ElementButton(list.get(0)+"+"+ServerCore.getPlugin().getAbilityHashMap().get(player.getUniqueId().toString()).getStr()));
        simple.addButton(new ElementButton(list.get(1)+"+"+ServerCore.getPlugin().getAbilityHashMap().get(player.getUniqueId().toString()).getDef()));
        simple.addButton(new ElementButton(list.get(2)+"+"+ServerCore.getPlugin().getAbilityHashMap().get(player.getUniqueId().toString()).getHigh()));
        simple.addButton(new ElementButton(list.get(3)+"+"+ServerCore.getPlugin().getAbilityHashMap().get(player.getUniqueId().toString()).getSpeed()));
        simple.addButton(new ElementButton(list.get(4)+"+"+ServerCore.getPlugin().getAbilityHashMap().get(player.getUniqueId().toString()).getMine()));
        simple.addButton(new ElementButton(list.get(5)+"+"+ServerCore.getPlugin().getAbilityHashMap().get(player.getUniqueId().toString()).getHealth()));
        player.showFormWindow(simple,FormIDs.AbilityUI.getId());
    }
    @EventHandler
    public void onOPMenu(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        int id = event.getFormID();
        if (event.wasClosed()) return;
        if (id == FormIDs.AbilityOPUI.getId()) {
            FormResponseCustom response = (FormResponseCustom) event.getResponse();
            Player target = ServerCore.getPlugin().getServer().getPlayer(response.getDropdownResponse(0).getElementContent());
            String type = response.getStepSliderResponse(1).getElementContent();
            String at = response.getDropdownResponse(2).getElementContent();
            int count = Integer.parseInt(response.getInputResponse(3));
            Ability ability = ServerCore.getPlugin().getAbilityHashMap().get(target.getUniqueId().toString());
            if (type.equals("設定")){
                switch (at){
                    case "點數":
                        ability.setPoint(count);
                        break;
                    case "力量":
                        ability.setStr(count);
                        break;
                    case "防禦力":
                        ability.setDef(count);
                        break;
                    case "身高":
                        ability.setHigh(count);
                        break;
                    case "速度":
                        ability.setSpeed(count);
                        break;
                    case "挖礦速度":
                        ability.setMine(count);
                        break;
                    case "血量":
                        ability.setHealth(count);
                        break;
                }
            }else {
                switch (at){
                    case "點數":
                        ServerCore.getPlugin().getServer().getPluginManager().callEvent(new PlayerAddPointEvent(target,count));
                        ability.addPoint(count);
                        break;
                    case "力量":
                        ability.setStr(ability.getStr()+count);
                        break;
                    case "防禦力":
                        ability.setDef(ability.getDef()+count);
                        break;
                    case "身高":
                        ability.setHigh(ability.getHigh()+count);
                        break;
                    case "速度":
                        ability.setSpeed(ability.getSpeed()+count);
                        break;
                    case "挖礦速度":
                        ability.setMine(ability.getMine()+count);
                        break;
                    case "血量":
                        ability.setHealth(ability.getHealth()+count);
                        break;
                }
            }
            player.sendMessage("操作成功!");
        }
    }
    @EventHandler
    public void PlayerForm(PlayerFormRespondedEvent event){
        Player player = event.getPlayer();
        int id = event.getFormID();
        if (event.wasClosed()) return;
        if (id == FormIDs.AbilityUI.getId()) {
            FormResponseSimple simple = (FormResponseSimple) event.getResponse();
            Ability ability = ServerCore.getPlugin().getAbilityHashMap().get(player.getUniqueId().toString());
            int count = 1;
            if (ability.getPoint()>0){
                switch (simple.getClickedButtonId()){
                    case 0:
                        ability.setStr(ability.getStr()+count);
                        break;
                    case 1:
                        ability.setDef(ability.getDef()+count);
                        break;
                    case 2:
                        if (ability.getHigh()>=ServerCore.getPlugin().getConfig().getInt("身高最大值")){
                            player.sendMessage("你已經達到最高值了 無法再繼續加點下去了");
                            return;
                        }
                        ability.setHigh(ability.getHigh()+count);
                        break;
                    case 3:
                        if (ability.getSpeed()>=ServerCore.getPlugin().getConfig().getInt("移動速度最大值")){
                            player.sendMessage("你已經達到最高值了 無法再繼續加點下去了");
                            return;
                        }
                        ability.setSpeed(ability.getSpeed()+count);
                        break;
                    case 4:
                        if (ability.getMine()>=ServerCore.getPlugin().getConfig().getInt("挖掘速度最大值")){
                            player.sendMessage("你已經達到最高值了 無法再繼續加點下去了");
                            return;
                        }
                        ability.setMine(ability.getMine()+count);
                        break;
                    case 5:
                        ability.setHealth(ability.getHealth()+count);
                        break;
                }
                ability.delPoint(count);
                ServerCore.reloadPlayer(player);
                player.sendMessage("天賦添加成功");
            }else {
                player.sendMessage("你的點數不足喔 趕快去升級把!");
            }
        }
    }
}
