本分支完整支持 Minecraft 1.21.10，NeoForge 依赖范围为 `[21.10,)`，使用 Java 21。

保留全部生存规则、快乐恶魂控制、大容量木桶、容器能力、活塞交互、玩家数据和聊天计算器。

构建命令为 `./gradlew.bat compileJava build --console=plain`，输出 `build/libs/survival_plus_plus-0.0.1-mc1.21.10.jar`。构建依赖同版本的 RollingGate；本次交付通过 Gradle 复合构建使用同目录的 RollingGate 源码。
