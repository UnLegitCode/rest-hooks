plugins {
    id("java")
    id("maven-publish")
}

group = "ru.unlegit"
version = "1.0.1"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("com.squareup.okhttp3:okhttp:4.12.0")

    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
    testImplementation("com.google.code.gson:gson:2.10.1")
    testImplementation("com.squareup.okhttp:okhttp:4.12.0")
}

publishing {
    repositories {
        mavenLocal()
    }
    publications {
        create<MavenPublication>("rest-hooks") {
            from(components["java"])
        }
    }
}

tasks {
    jar {
        archiveFileName.set("rest-hooks.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations.runtimeClasspath.get().files.forEach { from(zipTree(it)) }
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
}