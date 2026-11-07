package com.github.steven23334.crawl_ysm_mod_compatible;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = CrawlYsmCompatible.MODID, dist = Dist.CLIENT)
public class CrawlYsmCompatibleClient {
    public CrawlYsmCompatibleClient(ModContainer modContainer) {
        // ConfigurationScreen 会自动展示所有已注册配置（包括 COMMON），无需额外适配
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}