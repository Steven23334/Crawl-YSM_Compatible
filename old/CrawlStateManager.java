package com.github.steven23334.crawl_ysm_mod_compatible;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

/**
 * 「是否在爬行」的统一判定。
 *
 * <p>Crawl 模组的 {@code Player#updatePlayerPose} mixin 会把爬行姿势改成自定义的
 * {@code CRAWLING}，而 {@code PlayerUpdatePoseMixin} 又在方法返回后把它统一改回
 * {@code Pose.SWIMMING}。所以运行时我们能看到的爬行姿势只有两种：
 * <ul>
 *   <li>原版 1 格高爬行 / Crawl 爬行（已被我们改写）→ {@code Pose.SWIMMING} 且不在水里；</li>
 *   <li>万一改写没生效（例如别的模组又改回去）→ {@code CRAWLING}。</li>
 * </ul>
 * 两种都认，才算「原版爬行和 Crawl 爬行都接进来了」。
 */
public final class CrawlStateManager {

    /** Crawl 模组自定义 Pose 的名字 */
    private static final String CRAWL_POSE_NAME = "CRAWLING";

    private CrawlStateManager() {}

    public static boolean isCrawling(Player player) {
        return isCrawlPose(player) || isVanillaCrawlPose(player);
    }

    /** 原版爬行姿势：SWIMMING 且不在水里（在水里那是真游泳，不该拦）。 */
    public static boolean isVanillaCrawlPose(Player player) {
        return player.getPose() == Pose.SWIMMING && !player.isInWater();
    }

    /** Crawl 模组自定义的 CRAWLING 姿势（正常情况下已被 Mixin 改写掉）。 */
    public static boolean isCrawlPose(Player player) {
        return CRAWL_POSE_NAME.equals(player.getPose().name());
    }
}
