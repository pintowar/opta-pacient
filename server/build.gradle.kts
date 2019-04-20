import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("com.github.johnrengelman.shadow") version "4.0.2"
    id("net.ltgt.apt-eclipse") version "0.21"
    id("net.ltgt.apt-idea") version "0.21"
    java
    application
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom("io.micronaut:micronaut-bom:1.1.0")
    }
}

val developmentOnly by configurations.creating
val lombokVersion = "1.18.6"

dependencies {
    implementation(project(":core"))
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    implementation("com.github.akarnokd:rxjava2-jdk8-interop:0.3.5")

    annotationProcessor("io.micronaut:micronaut-inject-java")
    annotationProcessor("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-inject")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-http-server-netty")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntime("org.junit.jupiter:junit-jupiter-engine")
}


application {
    mainClassName = "com.github.pintowar.app.Application"
}
//// use JUnit 5 platform
val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}
test.classpath += developmentOnly

val shadowJar by tasks.getting(ShadowJar::class) {
    mergeServiceFiles()
}


val run by tasks.getting(JavaExec::class)
run.jvmArgs("-noverify", "-XX:TieredStopAtLevel=1")
run.classpath += developmentOnly
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
