plugins {
    kotlin("jvm") version "1.4.30"
    id("org.openjfx.javafxplugin") version "0.0.10"
}

group = "me.gaetan"
version = "1.0"

javafx {
    version = "12"
    modules("javafx.controls", "javafx.fxml")
}


repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/data2viz/p/maven/public") }
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks

compileKotlin.kotlinOptions.jvmTarget = "1.8"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("io.data2viz.d2v:color:0.8.12")
    implementation("io.data2viz.d2v:random:0.8.12")
    implementation("io.data2viz.d2v:shape:0.8.12")
    implementation("io.data2viz.d2v:scale:0.8.12")
    implementation("io.data2viz.d2v:viz:0.8.12")

}