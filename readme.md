# Procedural Platformer with AI Boss

A 2D scrolling platformer built in Java with LibGDX, developed as an AQA A-Level Computer Science NEA (Non-Exam Assessment). The project was designed and implemented independently over several months, following a full software development lifecycle: from client interview and prototyping through to a tested, documented technical solution, with a post-testing hand-written evaluation.

---

## Features

### Adaptive Boss AI — Markov Chain
The boss's AI is NOT hardcoded - it learns from your fights.

`BossAI` maintains a transition probability table across 8 states (`IDLE`, `WALK_AWAY`, `WALK_TOWARDS`, `DASH`, `JUMP`, `TELEPORT`, `SHOOT`, `SHOOT_EXPLOSIVE`). 
After each exchange, probabilities are rewarded or punished depending on whether the move succeeded – both for the specific transition *and* also globally across all states that lead to the unsuccessful move.

- Probabilities are (soft) clamped between `LOWEST_ALLOWED_PROBABILITY = 0.025` and `HIGHEST_ALLOWED_PROBABILITY = 0.8`, then renormalised after every update so that they always sum to 1.
- The full table of weights is stored in `assets/bossWeights.csv` after every update and reloaded on startup so that the boss literally remembers how to fight you across sessions.
- On a missing or corrupt weights file, the system falls back to a discrete uniform distribution.

### Procedural Level Generation
Levels are assembled at runtime from a pool of hand-crafted CSV prefabs, using a multi-layered generation pipeline:

1. **Recursive Binary Space Partitioning** — `LevelGenerator.generateSection()` recursively splits the level width up with a maximum of 4 recursive calls, stopping when sections fall below 800 units.
2. **Perlin noise for split positioning** — the split ratio within each section is nudged by `PerlinNoise.noise()`, keeping splits between 40–60% so the structure feels natural rather than mechanical.
3. **Difficulty-weighted prefab selection** — each leaf section is assigned a difficulty (1–5) based on its horizontal progress through the level, blended with a second noise pass for variation. `PrefabLibrary` selects a matching prefab by difficulty and available width from `prefabMetaData.csv`. 

The boss arena (`prefab10`) is always placed at the end of the level regardless of generation.


### Three-Box AABB Collision System
Each entity has three separate hitbox references:
- **CollisionBox** — used for physical world collision (platforms, walls)
- **Hitbox** — the zone that deals damage
- **Hurtbox** — the zone that receives damage

Interestingly, for some types of entity, these are all the same hit box, but for others, each reference points to a separate instance, depending on need.

`CollisionManager` is a static utility that resolves collisions using previous-frame positions to determine the direction of approach. It handles:
- Standard top/bottom/left/right platform resolution
- Moving platform drag (entities inherit the platform's velocity delta each frame)
- `PacingEnemy` bounce-back on vertical and horizontal axes
- Enemy-specific wall-turn behaviour
- A failsafe for cases where none of the directional checks fire

### Weapon & Combat System
Three swappable staff weapons, all extending an abstract `Staff` base class:

- **MeleeStaff** — dynamic hurtbox positioning relative to player facing, multi-hit prevention, downward attack 'pogo' mechanics, and projectile parrying
- **FireStaff** — spawns `Projectile` entities with `projectileType.FIRE`
- **IceStaff** — spawns `Projectile` entities with `projectileType.ICE`

Many enemies (`FireMage`, `Fireball`, `Boss`) also use the projectile system with an `Origin` enum to distinguish player vs. enemy projectiles in collision resolution.

### Level Loading via CSV
Hand-authored levels and all 16 prefabs are stored as CSV tile maps in `assets/Level/`. The loader parses each file, constructs tile objects, and places them into the world. `prefabMetaData.csv` stores difficulty ratings and dimensions so the generator can select prefabs without loading them.

### Input
`InputHandler` abstracts keyboard and controller input (via `gdx-controllers 2.2.4`), with correct just-pressed vs. held detection for both devices.

### UI
- `MainMenuScreen`, `LevelSelectScreen`, `HowToPlayScreen`, `GameScreen`
- Parallax cave backgrounds (`Background` class with a `BackgroundLayer` enum)
- HUD with `HealthBar` and `ManaBar`, both extending `UIBar`

---

## Tech Stack

|                        |                                                             |
|------------------------|-------------------------------------------------------------|
| **Language**           | Java                                                        |
| **Framework**          | LibGDX 1.13.5 (LWJGL3 backend)                              |
| **Controller support** | gdx-controllers 2.2.4                                       |
| **Font rendering**     | gdx-freetype 1.13.5                                         |
| **Build**              | Gradle (Kotlin DSL)                                         |
| **Tests**              | JUnit Jupiter 5.10.0                                        |
| **AI**                 | Markov chain — custom implementation, persisted to CSV      |
| **Generation**         | Recursive BSP + Perlin noise + prefab pool                  |
| **Collision**          | Custom discrete AABB (three-box system, no physics library) |
| **Level format**       | CSV                                                         |

---

## Project Structure

```
src/main/java/ruairi/nea/
├── applicationClasses/
│   ├── DesktopLauncher.java       # Entry point
│   ├── Main.java                  # LibGDX Application
│   ├── MainMenuScreen.java
│   ├── LevelSelectScreen.java
│   ├── HowToPlayScreen.java
│   └── Button.java
└── gameClasses/
    ├── GameScreen.java
    ├── CollisionManager.java      # Static AABB collision utility
    ├── Hitbox.java                # Shared box type (collision, hit, hurt)
    ├── InputHandler.java
    ├── Combat/
    │   ├── Staff.java             # Abstract base
    │   ├── MeleeStaff.java
    │   ├── FireStaff.java
    │   └── IceStaff.java
    ├── Entities/
    │   ├── Entity.java            # Abstract base
    │   ├── Hero.java
    │   ├── Platform.java
    │   ├── MovingPlatform.java
    │   ├── Wall.java
    │   ├── Projectile.java
    │   ├── Checkpoint.java
    │   ├── Coin.java
    │   ├── Goal.java
    │   └── Enemies/
    │       ├── Enemy.java
    │       ├── Boss.java
    │       ├── BossAI.java        # Markov chain state machine
    │       ├── PacingEnemy.java
    │       ├── FireMage.java
    │       ├── WillOWisp.java
    │       ├── Fireball.java
    │       └── Explosion.java
    ├── Level/
    │   ├── Level.java
    │   ├── LevelGenerator.java    # Recursive BSP + Perlin noise
    │   ├── PerlinNoise.java
    │   ├── PrefabLibrary.java
    │   ├── PrefabMetadata.java
    │   └── Background.java
    └── UI/
        ├── UIBar.java
        ├── HealthBar.java
        └── ManaBar.java

assets/
├── Level/
│   ├── level1.csv … level4.csv   # Hand-authored levels
│   ├── prefab10.csv … prefab25.csv
│   └── prefabMetaData.csv        # Difficulty + dimensions index
└── bossWeights.csv               # Persisted Markov chain weights
```

---

## Getting Started

**Prerequisites:** Java 17+, no other setup needed (all dependencies via Gradle)

```bash
git clone https://github.com/Ruairia/NEA.git
cd NEA
./gradlew run
```

On Windows:
```cmd
gradlew.bat run
```

> The run task passes `--enable-native-access=ALL-UNNAMED` and the required `--add-opens` flags automatically via `build.gradle.kts`.

---

## About the NEA

An AQA A-Level Computer Science NEA is a major independent coursework project simulating the software development lifecycle, marked across five sections:

1. **Analysis** — client interview, research into similar systems, scoped objectives
2. **Design** — architecture diagrams, algorithm design, UI wireframes, error handling strategy
3. **Technical Solution** — the implemented codebase
4. **Testing** — evidence-based testing against objectives
5. **Evaluation** — reflection against client requirements and personal critique

Completed in 2026 under AQA specification 7517.