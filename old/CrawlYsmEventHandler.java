package com.github.steven23334.crawl_ysm_mod_compatible;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 爬行时驱动 YSM 播放 climb / climbing 动画。
 *
 * <p>爬行判定见 {@link CrawlStateManager}：原版是 {@link Pose#SWIMMING}，
 * Crawl 模组是它自己的 {@code CRAWLING}（已由 {@code PlayerUpdatePoseMixin}
 * 强制改写成 SWIMMING），所以这里统一走 CrawlStateManager。
 */
public final class CrawlYsmEventHandler {

    private static final Logger LOG = LogManager.getLogger(CrawlYsmCompatible.MODID);

    /** 水平位移平方阈值：约 0.01 格/tick（0.2 m/s），低于此视为静止 */
    private static final double MOVE_THRESHOLD_SQR = 1.0E-4;

    /** 每隔多少 tick 重新下发一次，防止模型切换/重生后状态丢失 */
    private static final int REASSERT_INTERVAL = 20;

    private static final Map<UUID, State> STATES = new HashMap<>();

    /** /ysm 命令是否存在；YSM 的 native 初始化失败时它根本不会注册命令 */
    private static boolean ysmAvailable = false;

    private CrawlYsmEventHandler() {}

    private static final class State {
        boolean crawling;
        boolean moving;
        int reassertTimer;
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ysmAvailable = event.getServer().getCommands().getDispatcher().getRoot().getChild("ysm") != null;
        boolean enabled = CrawlYsmConfig.isEnabled();
        if (!ysmAvailable) {
            LOG.warn("[Crawl-YSM] 未检测到 /ysm 命令：YSM 没有正常初始化"
                    + "（常见原因见日志里的 'Failed to load native lib' / 'err: 54' / 'Incompatible system'）。"
                    + "本模组将保持空闲。");
        } else if (!enabled) {
            LOG.info("[Crawl-YSM] 总开关 enabled=false，本模组不介入（不改姿势、不发动画命令）。");
        } else {
            LOG.info("[Crawl-YSM] 检测到 /ysm 命令，联动已启用（enabled=true）。");
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        STATES.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!ysmAvailable) {
            return;
        }
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        State state = STATES.computeIfAbsent(player.getUUID(), uuid -> new State());

        // 总开关关闭：把还挂着的强制动画清掉，然后完全退出
        if (!CrawlYsmConfig.isEnabled()) {
            if (state.crawling) {
                YsmCommandHelper.stop(player);
                state.crawling = false;
                state.moving = false;
                state.reassertTimer = 0;
            }
            return;
        }

        boolean crawling = CrawlStateManager.isCrawling(player);

        if (!crawling) {
            if (state.crawling) {
                YsmCommandHelper.stop(player);
                state.crawling = false;
                state.moving = false;
                state.reassertTimer = 0;
            }
            return;
        }

        boolean moving = isMovingHorizontally(player);
        int timer = state.reassertTimer + 1;
        boolean needUpdate = !state.crawling || moving != state.moving || timer >= REASSERT_INTERVAL;

        if (needUpdate) {
            YsmCommandHelper.play(player, moving ? "climb" : "climbing");
            state.crawling = true;
            state.moving = moving;
            timer = 0;
        }
        state.reassertTimer = timer;
    }

    private static boolean isMovingHorizontally(ServerPlayer player) {
        double dx = player.getX() - player.xOld;
        double dz = player.getZ() - player.zOld;
        return dx * dx + dz * dz > MOVE_THRESHOLD_SQR;
    }
}
