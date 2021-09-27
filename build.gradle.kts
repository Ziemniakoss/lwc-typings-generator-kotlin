import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.5.21"
	application
}

group = "pl.ziemniakoss"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

buildscript {
	repositories {
		mavenCentral()
	}
}

dependencies {
	testImplementation(kotlin("test"))
	implementation(group="com.fasterxml.jackson.module", name="jackson-module-kotlin", version = "2.9.8")
}

tasks.test {
	useJUnit()
}

tasks.withType<KotlinCompile>() {
	kotlinOptions.jvmTarget = "1.8"
}

application {
	mainClassName = "MainKt"
}