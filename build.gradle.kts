plugins {
    id("java")
}

group = "ru.unlegit"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("com.squareup.okhttp:okhttp:2.7.5")

    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
    testImplementation("com.google.code.gson:gson:2.10.1")
    testImplementation("com.squareup.okhttp:okhttp:2.7.5")
}