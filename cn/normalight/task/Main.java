package cn.normalight.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
extends JavaPlugin
implements Listener{

    public void onEnable()
    {
        //注册监听器
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    //生物死亡事件
    @EventHandler
    public void onEneityDied(EntityDeathEvent e)
    {
        LivingEntity entity = e.getEntity();
        if (entity instanceof Player) return;
        if (entity.getKiller() == null) return;
        LivingEntity killer = entity.getKiller();
        if (!(killer instanceof Player)) return;
        Player p = (Player) killer;
        //检测掉落物
        if (e.getDrops() != null)
        {
            //给予物品
            for (ItemStack item : e.getDrops())
            {
                p.getInventory().addItem(item);
            }
            //清除掉落
            e.getDrops().clear();
        }
        //检测掉落经验
        if (e.getDroppedExp() != 0)
        {
            //给予经验
            p.giveExp(e.getDroppedExp());
            //清除掉落经验
            e.setDroppedExp(0);
        }
    }
}
