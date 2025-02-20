plugins {
    id("java")
}

group = "org.berneick"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Postgres
    runtimeOnly("org.postgresql:postgresql:42.7.5")
}

tasks.test {
    useJUnitPlatform()
}