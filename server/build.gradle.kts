plugins {
    id("org.springframework.boot") version "2.1.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
    java
}

val optaplannerVersion = "7.17.0.Final"
val lombokVersion = "1.18.6"
val groovyVersion = "2.5.6"

dependencies {
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    implementation("org.optaplanner:optaplanner-core:$optaplannerVersion")
    implementation("org.optaplanner:optaplanner-persistence-jackson:$optaplannerVersion")
    implementation("org.optaplanner:optaplanner-persistence-xstream:$optaplannerVersion")
//    implementation("org.optaplanner:optaplanner-examples:$optaplannerVersion") {
//        exclude(group = "com.sun.xml.bind")
//        exclude(group = "org.freemarker")
//    }

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.codehaus.groovy:groovy:$groovyVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}
