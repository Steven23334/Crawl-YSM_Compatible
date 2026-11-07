package com.github.steven23334.crawl_ysm_mod_compatible;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.minecraft.commands.CommandSourceStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 通过 /ysm 命令驱动 YSM 的强制动画。
 *
 * <p>YSM 的命令树（反编译自 yes_steve_model 2.5.3 / 2.6.5）：
 * <pre>
 *   /ysm play &lt;targets&gt; &lt;animation&gt;   // animation 传 stop 即清除
 * </pre>
 * 该命令 requires 权限等级 ≥ 2，所以这里用 withPermission(2) 构造命令源。
 * 参数用玩家档案名，避免中文/特殊字符导致的解析问题。
 */
public final class YsmCommandHelper {

    private static final Logger LOG = LogManager.getLogger(CrawlYsmCompatible.MODID);

    /** 只提示一次，避免爬行时刷屏 */
    private static boolean warnedOnce = false;

    private YsmCommandHelper() {}

    /** 强制播放指定动画，例如 climb / climbing；返回是否真正执行成功。 */
    public static boolean play(ServerPlayer player, String animation) {
        return run(player, "play " + player.getGameProfile().getName() + " " + animation);
    }

    /** 清除强制动画，让 YSM 回到它自己的动画控制器。 */
    public static boolean stop(ServerPlayer player) {
        return run(player, "play " + player.getGameProfile().getName() + " stop");
    }

    private static boolean run(ServerPlayer player, String args) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return false;
        }

        CommandSourceStack source = server.createCommandSourceStack()
                .withSuppressedOutput()
                .withPermission(2)
                .withEntity(player);

        try {
            // 1.21 的 performPrefixedCommand 返回 void，这里直接用 Brigadier 执行以拿到返回值
            int result = server.getCommands().getDispatcher().execute("ysm " + args, source);
            if (result <= 0 && !warnedOnce) {
                warnedOnce = true;
                LOG.warn("[Crawl-YSM] 执行 '/ysm {}' 没有生效：请确认 YSM 可用（游戏内 Alt+Y 能打开模型界面），"
                        + "以及当前模型里有 climb / climbing 动画。", args);
            }
            return result > 0;
        } catch (Throwable t) {
            if (!warnedOnce) {
                warnedOnce = true;
                LOG.warn("[Crawl-YSM] 执行 '/ysm {}' 抛出异常，已跳过。", args, t);
            }
            return false;
        }
    }
}
