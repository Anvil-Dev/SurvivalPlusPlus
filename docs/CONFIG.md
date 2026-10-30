本分支完整支持 Minecraft 1.21.10，NeoForge 依赖范围为 `[21.10,)`，使用 Java 21。

保留全部生存规则、快乐恶魂控制、大容量木桶、容器能力、活塞交互、玩家数据和聊天计算器。

构建命令为 `./gradlew.bat compileJava build --console=plain`，输出 `build/libs/survival_plus_plus-0.0.1-mc1.21.10.jar`。构建时优先使用相邻目录中的同版本 RollingGate 源码，可用 `-Prolling_gate_source=路径` 指定源码位置。
