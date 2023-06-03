import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version ("8.1.1")
}

group = "com.i0dev.plugin.globalcurrency"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://nexus.mcrivals.com/repository/maven-private")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://jitpack.io")
}

dependencies {
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("commons-lang:commons-lang:2.6")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.26")
    compileOnly("me.clip:placeholderapi:2.11.3")
    compileOnly("de.tr7zw:item-nbt-api-plugin:2.11.1")

    annotationProcessor("org.projectlombok:lombok:1.18.26")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
    }
}