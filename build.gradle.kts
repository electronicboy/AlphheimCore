import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "pw.alphheim"
version = "1.0-SNAPSHOT"


buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.10"

    repositories {
        mavenCentral()
    }
    
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlin_version))
    }
    
}

apply {
    plugin("java")
    plugin("kotlin")
}

val kotlin_version: String by extra

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compile(kotlinModule("stdlib-jdk8", kotlin_version))
    compile("com.zaxxer", "HikariCP", "2.7.4")
    compileOnly("pw.alphheim", "alphheimcraft", "1.8.8-R0.1-SNAPSHOT")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

