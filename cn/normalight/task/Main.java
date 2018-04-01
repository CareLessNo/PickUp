package cn.normalight.task;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
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

    @EventHandler
    public void onBlockBroken(BlockBreakEvent e)
    {
        Block block = e.getBlock();
        if (block.breakNaturally()) return;
        if (e.getPlayer() != null) return;
        Player p = e.getPlayer();
        //检测掉落物
        if (block.getDrops() != null)
        {
            //给予物品
            for (ItemStack item : block.getDrops())
            {
                if (ifFull(p))
                {
                    p.getInventory().addItem(item);
                    //清除掉落
                    block.getDrops().remove(item);
                }
            }

        }
        //检测掉落经验
        if (e.getExpToDrop() != 0)
        {
            //给予经验
            p.giveExp(e.getExpToDrop());
            //清除掉落经验
            e.setExpToDrop(0);
        }
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
                if (ifFull(p))
                {
                    p.getInventory().addItem(item);
                    e.getDrops().remove(item);
                }
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

    private static boolean ifFull(Player p)
    {
        Inventory inv = p.getInventory();
        if (p.getInventory().firstEmpty() != -1) return true;
        return false;
    }
}
