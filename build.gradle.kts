
plugins {
    id("java")
    id("application")
}

group = "ruairi.nea"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val gdxVersion = "1.13.5"
val gdxControllersVersion = "2.2.4"

dependencies {

    implementation("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")
    runtimeOnly("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")

    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    runtimeOnly("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")


    implementation("com.badlogicgames.gdx-controllers:gdx-controllers-desktop:${gdxControllersVersion}")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("ruairi.nea.applicationClasses.DesktopLauncher")
}

tasks.named<JavaExec>("run") {
    jvmArgs = listOf(
        "--enable-native-access=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang=ALL-UNNAMED",
        "--add-opens", "java.base/sun.misc=ALL-UNNAMED"
    )
}

tasks.test {
    useJUnitPlatform()
}