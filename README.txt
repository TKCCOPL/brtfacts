# BrtFacts 饰品模组 (BrtFacts Mod)

一个简单的 Minecraft 1.20.1 Forge 饰品模组，基于 Curios API 开发，为主装带来了多种实用且附有特效的神奇饰品。

## ✨ 包含的物品
- **迅捷围巾 (Swift Scarf):** 穿戴时提供持久的移动速度提升。
- **矿工护目镜 (Miner's Goggles):** 穿戴时在黑暗中提供清晰的夜视。
- **铁拳 (Iron Fist):** 沉重的铁拳，穿戴时大幅增加近战伤害。
- **兔兔拖鞋 (Bunny Slippers):** 穿戴时赋予跳跃提升效果，不再畏惧高地。
- **末影单片镜 (Ender Monocle):** 提供夜视能力，同时使周围生物发光无所遁形。
- **岩浆之心腰带 (Magma Heart Belt):** 保护佩戴者免受火焰与岩浆伤害，并免疫粉雪冰冻。

## 📥 安装说明

本模组依赖以下前置：
1. [Minecraft Forge (1.20.1)](https://files.minecraftforge.net/net/minecraftforge/forge/)
2. [Curios API](https://www.curseforge.com/minecraft/mc-mods/curios)

下载最新版的 `.jar` 模组文件后，放入游戏的 `mods` 文件夹即可。

## 🛠️ 开发者指南 (For Modders)

如果您想对本模组进行二次开发或阅读源码，该项目采用了推荐的 Forge 开发构建方法并使用了 Parchment 映射。

**环境搭建流程：**
1. 克隆或下载本仓库代码到本地。
2. 打开命令行进入项目根目录。
3. 执行工作区初始化：
   - 对于 IntelliJ IDEA 开发者：运行 `./gradlew genIntellijRuns` 然后刷新 Gradle 项目。
   - 对于 VS Code 开发者：运行 `./gradlew genVSCodeRuns` 并确保有 Java 插件支持。
   - 对于 Eclipse 开发者：运行 `./gradlew genEclipseRuns`

如果在加载或同步过程中遇到丢失依赖的问题，您可以尝试运行 `./gradlew --refresh-dependencies`。

---

*This mod is built based on the Minecraft Forge MDK. Ensure you follow their licenses properly when distributing modifications.*
