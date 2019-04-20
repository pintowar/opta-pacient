plugins {
    id("com.palantir.docker") version "0.22.0"
    id("com.palantir.docker-run") version "0.22.0"
}

version = "0.1.0"
group = "com.github.pintowar"

subprojects {
    version = project.version
    group = project.group

    apply(plugin = "idea")

    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()
    }
}

tasks.create("copyClientResources") {
    dependsOn(":client:build")
    group = "build"
    doLast {
        copy {
            from(project(":client").buildDir.absolutePath)
            into("${project(":server").buildDir}/resources/main/public")
        }
    }
}

tasks.create("assembleServerAndClient") {
    dependsOn("copyClientResources", ":server:assemble")
    group = "build"
    description = "Build combined server & client JAR"

    doLast {
        copy {
            from(fileTree("${project(":server").buildDir}/libs/"))
            into("$rootDir/build/")
        }

        logger.quiet("JAR generated at $rootDir/build/. It combines the server and client projects.")
    }
}
tasks.getByPath(":server:assemble").mustRunAfter(":copyClientResources")

tasks.create<Delete>("clean") {
    group = "build"
    description = "Clean the client bundle"
    delete("build")
}

docker {
//    dependsOn(tasks.getByName("assembleServerAndClient"))
    name = "pintowar/opta-pacient:$version"
    tag("latest", "pintowar/opta-pacient:latest")
    files("$rootDir/build/server.jar")
}

dockerRun {
    name = "opta-pacient"
    image = docker.name
    clean = true
    daemonize = false
}