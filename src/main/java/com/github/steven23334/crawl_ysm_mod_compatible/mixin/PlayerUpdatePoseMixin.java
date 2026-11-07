package com.github.steven23334.crawl_ysm_mod_compatible.mixin;

import com.github.steven23334.crawl_ysm_mod_compatible.CrawlYsmConfig;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 把 Crawl 模组的自定义 {@code CRAWLING} 姿势改回 {@link Pose#SWIMMING}，
 * 让 YSM 能识别原版 1 格高爬行和 Crawl 模组爬行。
 *
 * <p>Crawl 模组（0.14.x）在 {@code Player#updatePlayerPose} 里用 {@code @ModifyArg}
 * 把原本的 {@code Pose.SWIMMING} 换成了它扩展出来的 {@code Pose.valueOf("CRAWLING")}：
 * <ul>
 *   <li>Crawl 键爬行 → 姿势是 {@code CRAWLING}</li>
 *   <li><b>原版 1 格高爬行也会被改成 {@code CRAWLING}</b>（因为原版本来就是 SWIMMING）</li>
 * </ul>
 * YSM 不认识 {@code CRAWLING}，两种爬行都不会走它的爬行/游泳渲染分支。
 *
 * <p>这里在方法返回后统一改写：只要当前姿势是 {@code CRAWLING}，就换成 {@code SWIMMING}。
 * 不用 {@code @At("HEAD")} + cancel，是因为那需要自己判断「是否正在爬行」，
 * 一旦用当前姿势反推就会自我维持、永远退不出爬行姿势。返回后改写只依赖方法算出的结果，安全。
 *
 * <p>受 {@link CrawlYsmConfig#isEnabled()} 控制：关闭时直接 return，不做任何修改。
 */
@Mixin(Player.class)
public abstract class PlayerUpdatePoseMixin {

    /**
     * Crawl 模组自定义 Pose 的名字。
     *
     * <p>用 {@code @Unique} + 模组前缀命名，避免与其他 Mixin 添加的同名成员冲突。
     */
    @Unique
    private static final String crawlYsm$CRAWL_MOD_POSE = "CRAWLING";

    @Inject(method = "updatePlayerPose", at = @At("RETURN"))
    private void crawlYsm$rewriteCrawlPose(CallbackInfo ci) {
        // 功能关闭：拦截改写，保持 Crawl 模组的原始姿势
        if (!CrawlYsmConfig.isEnabled()) {
            return;
        }
        Player self = (Player) (Object) this;
        if (crawlYsm$CRAWL_MOD_POSE.equals(self.getPose().name())) {
            self.setPose(Pose.SWIMMING);
            self.refreshDimensions();
        }
    }
}