buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradle}")
        classpath("com.novoda:bintray-release:0.9.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.google.gms:google-services:4.3.3")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }

    ext {
        set("bintrayUser", System.getenv("bintrayUser"))
        set("bintrayKey", System.getenv("bintrayKey"))
        set("dryRun", Modules.dryRun)
    }

}

subprojects {
    val subprojectName = name
    tasks.register("bintrayPublish", DefaultTask::class){
        dependsOn(":$subprojectName:build", ":$subprojectName:bintrayUpload")
    }
}

tasks.register("bintrayPublish", DefaultTask::class) {
    doFirst {
        val publishingSubprojects = subprojects.filter { s -> s.plugins.hasPlugin("com.novoda.bintray-release") }.map { it.name + ":bintrayPublish" }.toTypedArray()
        println("./gradlew "+publishingSubprojects.joinToString(" "))
    }
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}