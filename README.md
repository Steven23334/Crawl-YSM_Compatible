# Crawl YSM Compatible
[![Available on GitHub](https://wsrv.nl/?url=https%3A%2F%2Fcdn.jsdelivr.net%2Fnpm%2F%40intergrav%2Fdevins-badges%403%2Fassets%2Fcozy%2Favailable%2Fgithub_vector.svg&n=-1)](https://github.com/Steven23334/Crawl-YSM_Mod_Compatible)
[![Available on CurseForge](https://wsrv.nl/?url=https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/curseforge_vector.svg)](https://modrinth.com/project/crawl-ysm-compatible)

This is a compatibility addon that makes the **Crawl** mod (and the vanilla 1-block-high crawl) work with **Yes Steve Model (YSM)**. It rewrites the Crawl mod's custom `CRAWLING` pose back to the vanilla `Pose.SWIMMING`, so YSM can recognize both crawl states.

## 📦 What It Does

- **Pose rewrite**
  The Crawl mod replaces the vanilla crawl/swim pose with its own custom pose `CRAWLING` in `Player#updatePlayerPose`, which YSM does not recognize (so no crawl animation is triggered). A Mixin rewrites `CRAWLING` back to `Pose.SWIMMING` after `updatePlayerPose` returns, so both the Crawl mod crawl and the vanilla 1-block crawl are recognized by YSM.

- **Master switch**
  A single `enabled` option. When disabled, the Mixin is intercepted and does not modify the pose at all — the Crawl mod's original `CRAWLING` pose is left untouched.

## 🎯 Purpose

- Makes the Crawl mod's custom `CRAWLING` pose visible to YSM.
- Covers both the Crawl mod crawl and the vanilla 1-block-high crawl.
- Provides a lightweight, config-toggleable patch without touching YSM itself.

## 🔧 Installation

1. Install **NeoForge**.
2. Install the **Crawl** mod and **Yes Steve Model (YSM)**.
3. Place the `.jar` file of this mod into the `.minecraft/mods` folder.
4. Launch the game.

## ⚙️ Configuration

- File: `config/crawl_ysm_mod_compatible-common.toml`
- `enabled` (default `true`) — whether to rewrite the Crawl mod's `CRAWLING` pose back to `SWIMMING` for YSM recognition. When disabled, the mixin leaves the pose untouched.
- The switch takes effect immediately (read every tick), no game restart required.
- Registered as a **COMMON** config so both the client and the server can read it (`updatePlayerPose` runs on both sides).

## ⚠️ Requirements

- **Minecraft Versions**: 1.21.x
- **Required Mods**: [Crawl](https://modrinth.com/project/crawl) and [Yes Steve Model (YSM)](https://modrinth.com/project/yes-steve-model)

## 📝 Notes

- Without the Crawl mod, the `CRAWLING` pose never appears, so the rewrite does nothing — the mod is safe but pointless.
- No `/ysm` commands are sent; this addon only touches the pose, it does not drive YSM animations.

## 📜 License

- Code: [MIT License](https://mit-license.org/)

## 🙏 Authors

- Programmer: Steven23334
---------
# Crawl YSM 兼容

[![可在 GitHub 上获取](https://wsrv.nl/?url=https%3A%2F%2Fcdn.jsdelivr.net%2Fnpm%2F%40intergrav%2Fdevins-badges%403%2Fassets%2Fcozy%2Favailable%2Fgithub_vector.svg&n=-1)](https://github.com/Steven23334/Crawl-YSM_Mod_Compatible)
[![可在 CurseForge 上获取](https://wsrv.nl/?url=https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/curseforge_vector.svg)](https://modrinth.com/project/crawl-ysm-compatible)   
这是一个兼容补丁，让 **Crawl** 模组（以及原版 1 格高爬行）能被 **Yes Steve Model (YSM)** 正确识别。它把 Crawl 模组的自定义 `CRAWLING` 姿势改写回原版 `Pose.SWIMMING`，于是两种爬行状态 YSM 都能识别。

## 📦 新增内容

- **姿势改写**
  Crawl 模组在 `Player#updatePlayerPose` 里把原版爬行/游泳姿势换成了它自定义的 `CRAWLING`，YSM 不认识这个姿势，因此不会播放爬行动画。本模组用 Mixin 在 `updatePlayerPose` 返回后把 `CRAWLING` 改回 `Pose.SWIMMING`，于是 Crawl 模组的爬行和原版 1 格高爬行都能被 YSM 识别。

- **总开关**
  只有一个 `enabled` 选项。关闭后 Mixin 被拦截、不做任何修改，Crawl 模组的原始 `CRAWLING` 姿势保持不变。

## 🎯 模组用途

- 让 Crawl 模组的自定义 `CRAWLING` 姿势能被 YSM 识别。
- 同时覆盖 Crawl 模组爬行和原版 1 格高爬行。
- 提供一个轻量、可配置开关的补丁，不修改 YSM 本身。

## 🔧 安装方法

1. 安装 **NeoForge**。
2. 安装 **Crawl** 模组和 **Yes Steve Model (YSM)**。
3. 将本模组的 `.jar` 文件放入 `.minecraft/mods` 文件夹。
4. 启动游戏即可。

## ⚙️ 配置项

- 文件：`config/crawl_ysm_mod_compatible-common.toml`
- `enabled`（默认 `true`）—— 是否把 Crawl 模组的 `CRAWLING` 姿势改写回 `SWIMMING` 供 YSM 识别。关闭后 Mixin 不做任何修改，姿势保持不变。
- 开关即时生效（每 tick 读取），无需重启游戏。
- 注册为 **COMMON** 配置，客户端与服务端都能读取（`updatePlayerPose` 双端都会执行）。

## ⚠️ 前置要求

- **Minecraft 版本**：1.21.x
- **必需模组**：[Crawl](https://modrinth.com/project/crawl) 和 [Yes Steve Model (YSM)](https://modrinth.com/project/yes-steve-model)

## 📝 注意事项

- 没有安装 Crawl 模组时 `CRAWLING` 姿势永远不会出现，改写逻辑不会生效 —— 模组安全但无意义。
- 本模组不发送任何 `/ysm` 命令，只改姿势，不驱动 YSM 动画。

## 📜 许可证

- 代码：[MIT License](https://mit-license.org/)

## 🙏 作者

- 程序：Steven23334