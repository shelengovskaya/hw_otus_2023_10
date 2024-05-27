rootProject.name = "otusJava"
include(":hw01-gradle")
include(":hw02-generics")
include(":hw03-annotations")
include(":hw04-gc:homework")
include(":hw05-aop")
include(":hw06-solid")
include(":hw08-json:homework")
include(":hw16-queues")

pluginManagement {
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("org.springframework.boot") version springframeworkBoot
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}