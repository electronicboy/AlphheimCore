import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.script.lang.kotlin.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

group = "pw.alphheim"
version = "1.0-SNAPSHOT"


buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.10"

    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlin_version))
        classpath("com.github.jengelman.gradle.plugins:shadow:2.0.2")
    }

}

apply {
    plugin("java")
    plugin("kotlin")
    plugin("com.github.johnrengelman.shadow")
}

plugins {
    java
}

val kotlin_version: String by extra

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = URI("http://repo.aikar.co/nexus/content/groups/aikar/")
    }
    maven {
        url = URI("https://hub.spigotmc.org/nexus/content/groups/public/")
    }
}

dependencies {
    compile(kotlinModule("stdlib-jdk8", kotlin_version))
    compile("com.zaxxer", "HikariCP", "2.7.4")
    compile("com.google.inject", "guice", "4.1.0")
    compile("co.aikar", "minecraft-timings", "1.0.4")
    compileOnly("pw.alphheim", "alphheimcraft", "1.8.8-R0.1-SNAPSHOT")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val shadowJar = tasks.getByName("shadowJar") as ShadowJar
shadowJar.apply {
    relocate("co.aikar.commands", "im.alphhe.alphheimplugin.libs")
}

val build: DefaultTask by tasks
build.dependsOn(shadowJar)

