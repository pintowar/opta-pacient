import com.moowork.gradle.node.yarn.YarnTask

plugins {
    id("com.moowork.node") version "1.3.1"
}

project.buildDir = file("dist")

node {
    version = "12.4.0"
    yarnVersion = "1.13.0"
    download = true
}

tasks.create<YarnTask>("run") {
    dependsOn("yarn")
    group = "application"
    description = "Run the client app"
    args = listOf("run", "serve")
}

tasks.create<YarnTask>("build") {
    dependsOn("yarn")
    group = "build"
    description = "Build the client bundle"
    args = listOf("run", "build")
}

tasks.create<YarnTask>("clean") {
    group = "build"
    description = "Clean the client bundle"
    delete("dist")
}

tasks.create<YarnTask>("test") {
    dependsOn("yarn")
    group = "verification"
    description = "Run the client tests"
    args = listOf("run", "test")
}
