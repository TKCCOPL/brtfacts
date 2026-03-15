# BrtFacts 模组 — 中文项目分析

## 项目概述

**BrtFacts** 是一个适用于 **Minecraft 1.20.1（Forge 47.4.16）** 的模组，由作者 **FReedom** 开发。该模组向游戏中添加了 4 件具有特殊能力的魔法工件（Artifacts），每件物品都赋予玩家独特的被动效果或属性加成。

---

## 物品功能详解

### 1. 迅捷围巾（Swift Scarf）

- **类型**：胸甲
- **材质基础**：皮革
- **特殊效果**：装备后持续获得**移动速度提升 I**，让玩家移动更快。
- **合成配方**（工作台 3×3）：

  ```
  S W S
  W F W
    W  
  ```

  - `S` = 线（String）
  - `W` = 任意羊毛（Wool）
  - `F` = 羽毛（Feather）

---

### 2. 矿工护目镜（Miner's Goggles）

- **类型**：头盔
- **材质基础**：金质
- **特殊效果**：装备后持续获得**夜视**，可在黑暗中清晰看路，非常适合挖矿。
- **合成配方**（工作台 3×3）：

  ```
  L G L
  G P G
  ```

  - `L` = 皮革（Leather）
  - `G` = 金锭（Gold Ingot）
  - `P` = 玻璃板（Glass Pane）

---

### 3. 铁拳（Iron Fist）

- **类型**：近战武器/工具（手持）
- **耐久度**：250
- **攻击加成**：+5.0 攻击伤害，-2.4 攻击速度（挥舞较慢但打击有力）
- **挖掘能力**：可以铁质工具的速度（6.0）挖掘镐类或铲类方块
- **合成配方**（工作台 3×3）：

  ```
    I I
  I L I
    L  
  ```

  - `I` = 铁锭（Iron Ingot）
  - `L` = 皮革（Leather）

---

### 4. 兔兔拖鞋（Bunny Slippers）

- **类型**：靴子
- **材质基础**：皮革
- **特殊效果**：装备后持续获得**跳跃提升 II**，可跳得更高；同时**完全免疫摔落伤害**。
- **合成配方**（工作台 3×3）：

  ```
  R S R
  W   W
  ```

  - `R` = 兔子皮（Rabbit Hide）
  - `S` = 线（String）
  - `W` = 白色羊毛（White Wool）

---

## 项目结构

```
brtfacts/
├── src/main/java/com/brtfacts/
│   ├── BrtFacts.java               # 模组主类，负责物品注册与创意标签页
│   ├── event/
│   │   └── ModEvents.java          # 事件监听（摔落伤害取消逻辑）
│   └── item/
│       ├── SwiftScarfItem.java     # 迅捷围巾
│       ├── MinersGogglesItem.java  # 矿工护目镜
│       ├── IronFistItem.java       # 铁拳
│       └── BunnySlippersItem.java  # 兔兔拖鞋
├── src/main/resources/
│   ├── META-INF/mods.toml          # 模组元数据配置
│   ├── assets/brtfacts/
│   │   ├── lang/
│   │   │   ├── en_us.json          # 英文语言文件
│   │   │   └── zh_cn.json          # 中文语言文件
│   │   ├── models/item/            # 物品 3D 模型定义
│   │   └── textures/item/          # 物品贴图（PNG）
│   └── data/brtfacts/recipes/      # 合成配方（JSON）
├── build.gradle                    # Gradle 构建脚本
├── gradle.properties               # 版本与属性配置
└── README.txt                      # Forge MDK 开发环境说明
```

---

## 技术栈

| 技术 | 版本 |
|------|------|
| Minecraft | 1.20.1 |
| Minecraft Forge | 47.4.16 |
| Java | 17 |
| Gradle | 构建工具 |

---

## 构建与运行

### 环境搭建（仅需执行一次）

```bash
# IntelliJ IDEA
./gradlew genIntellijRuns

# Eclipse
./gradlew genEclipseRuns
```

### 常用命令

```bash
# 编译并生成 JAR
./gradlew build

# 启动客户端（单人游戏）
./gradlew runClient

# 启动服务器
./gradlew runServer

# 清理构建输出
./gradlew clean

# 刷新依赖缓存
./gradlew --refresh-dependencies
```

### 构建产物

- **发布版 JAR**：`build/libs/brtfacts-1.0.jar`
- **开发版 JAR**：`build/libs/brtfacts-1.0-dev.jar`（未混淆，供开发使用）

---

## 代码架构分析

### 1. 注册机制（Deferred Registry）

所有物品通过 Forge 的 `DeferredRegister` 在 `BrtFacts.java` 中统一注册，确保物品在游戏生命周期的正确阶段加载：

```java
// 创建物品注册器
public static final DeferredRegister<Item> ITEMS =
    DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

// 注册每件物品
public static final RegistryObject<Item> SWIFT_SCARF =
    ITEMS.register("swift_scarf", SwiftScarfItem::new);
// ... 其余物品同理

// 在模组构造函数中将注册器绑定到事件总线
ITEMS.register(modEventBus);
```

### 2. 护甲 Tick 机制

迅捷围巾、矿工护目镜、兔兔拖鞋均继承 `ArmorItem`，通过重写 `onArmorTick()` 方法在**每个服务器 Tick**（每秒 20 次）为玩家施加持续药水效果：

```java
@Override
public void onArmorTick(ItemStack stack, Level level, Player player) {
    if (!level.isClientSide()) {
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, false, false, true));
    }
}
```

效果时长设为 220 tick（约 11 秒），由于每个服务器 Tick（每秒 20 次）都会刷新，玩家实际上会持续保持该效果。

### 3. 事件驱动（摔落免疫）

兔兔拖鞋的摔落免疫通过 `ModEvents.java` 监听 `LivingFallEvent` 实现，检测到玩家脚部装备为兔兔拖鞋时，将摔落距离置为 0 并取消事件：

```java
@SubscribeEvent
public static void onLivingFall(LivingFallEvent event) {
    LivingEntity entity = event.getEntity();
    if (entity instanceof Player player) {
        if (player.getItemBySlot(EquipmentSlot.FEET).getItem() == BrtFacts.BUNNY_SLIPPERS.get()) {
            event.setDistance(0.0F);
            event.setCanceled(true);
        }
    }
}
```

### 4. 属性修饰符（铁拳）

铁拳使用 Minecraft 的属性修饰符（Attribute Modifier）系统，通过固定 UUID 为玩家主手加成攻击力和攻击速度：

```java
builder.put(Attributes.ATTACK_DAMAGE,
    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 5.0D, ADDITION));
builder.put(Attributes.ATTACK_SPEED,
    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -2.4D, ADDITION));
```

### 5. 国际化（i18n）

模组同时支持英文（`en_us.json`）和中文（`zh_cn.json`）：

```json
{
  "item.brtfacts.swift_scarf": "迅捷围巾",
  "item.brtfacts.miners_goggles": "矿工护目镜",
  "item.brtfacts.iron_fist": "铁拳",
  "item.brtfacts.bunny_slippers": "兔兔拖鞋"
}
```

---

## 模组信息

| 属性 | 值 |
|------|----|
| Mod ID | `brtfacts` |
| 版本 | 1.0 |
| 作者 | FReedom |
| 目标 MC 版本 | 1.20.1 |
| 许可证 | LE_0.1 |
