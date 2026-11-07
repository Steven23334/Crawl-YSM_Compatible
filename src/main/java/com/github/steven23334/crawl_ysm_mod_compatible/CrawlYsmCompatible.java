package com.github.steven23334.crawl_ysm_mod_compatible;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(CrawlYsmCompatible.MODID)
public class CrawlYsmCompatible {

    public static final String MODID = "crawl_ysm_mod_compatible";

    public CrawlYsmCompatible(ModContainer modContainer) {
        // 通用配置：config/crawl_ysm_mod_compatible-common.toml
        // updatePlayerPose 双端执行，必须用 COMMON 才能让客户端也读到开关
        modContainer.registerConfig(ModConfig.Type.COMMON, CrawlYsmConfig.SPEC);
    }
}