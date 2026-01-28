plugins {
	java
	id("org.springframework.boot") version "3.4.1" // 2026年1月現在、安定版の3.4.x推奨（3.5.xはマイルストーン版の可能性あり）
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.m_takahisa"
version = "0.0.1-SNAPSHOT"
description = "Task management API built with Java 21 and Spring Boot 3"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// DB関連
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("org.postgresql:postgresql")

	// Web関連
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation ("org.springframework.boot:spring-boot-starter-validation")

	// 便利ツール
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// テスト関連
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}