plugins {
    `java-library`
}

val optaplannerVersion = "7.20.0.Final"
val lombokVersion = "1.18.6"

dependencies {
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    api("org.optaplanner:optaplanner-core:$optaplannerVersion")
    api("org.optaplanner:optaplanner-persistence-jackson:$optaplannerVersion")
    api("org.optaplanner:optaplanner-persistence-xstream:$optaplannerVersion")
//    implementation("org.optaplanner:optaplanner-examples:$optaplannerVersion") {
//        exclude(group = "com.sun.xml.bind")
//        exclude(group = "org.freemarker")
//    }
}
