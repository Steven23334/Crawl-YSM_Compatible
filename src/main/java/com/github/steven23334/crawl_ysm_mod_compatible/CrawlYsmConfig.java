package com.github.steven23334.crawl_ysm_mod_compatible;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 模组配置，生成于 {@code config/crawl_ysm_mod_compatible-common.toml}。
 *
 * <p>只有一个开关 {@code enabled}，控制是否允许 {@code PlayerUpdatePoseMixin}
 * 把 Crawl 模组的自定义 {@code CRAWLING} 姿势改写回
 * {@link net.minecraft.world.entity.Pose#SWIMMING}：
 * <ul>
 *   <li>true（默认）：允许改写，YSM 可以识别原版 / Crawl 爬行姿势；</li>
 *   <li>false：拦截 Mixin 改写，Crawl 模组的原始姿势保持不变。</li>
 * </ul>
 *
 * <p>{@code Player#updatePlayerPose} 在客户端与服务端都会执行，因此配置注册为
 * {@link net.neoforged.fml.config.ModConfig.Type#COMMON}，保证双端都能读取开关值。
 *
 * <p>配置每 tick 读取一次（{@link #isEnabled()} 在 Mixin 里被调用），
 * 修改后无需重启游戏即可生效。
 */
public final class CrawlYsmConfig {

    public static final ModConfigSpec SPEC;
    private static final ModConfigSpec.BooleanValue ENABLED;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        ENABLED = builder
                .comment("是否允许把 Crawl 模组的 CRAWLING 姿势改写回 SWIMMING（供 YSM 识别）。",
                        "关闭后 Mixin 不做任何修改，Crawl 模组的原始姿势保持不变。",
                        "Whether to rewrite the Crawl mod's CRAWLING pose back to SWIMMING so YSM can recognize it.",
                        "When disabled, the mixin leaves the pose untouched.")
                .define("enabled", true);
        SPEC = builder.build();
    }

    private CrawlYsmConfig() {}

    /**
     * 是否允许 Mixin 改写爬行姿势。
     *
     * <p>配置尚未加载完成时按默认值（true）处理。
     */
    public static boolean isEnabled() {
        try {
            return ENABLED.get();
        } catch (Throwable t) {
            return true;
        }
    }
}