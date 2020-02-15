
plugins {
    id("com.android.library")
    id("com.novoda.bintray-release")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(Modules.compileSdk)
    buildToolsVersion = Modules.buildToolsVersion


    defaultConfig {
        minSdkVersion(Modules.minSdk)
        targetSdkVersion(Modules.targetSdk)
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

}

publish {
    project.name
    userOrg = Bintray.bt_userOrg
    groupId = Bintray.bt_groupId
    repoName = Bintray.bt_repoName
    website = Bintray.bt_website
    artifactId = name
    publishVersion = android.defaultConfig.versionName
    desc = ""
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    implementation("androidx.lifecycle:lifecycle-livedata:${Versions.lifecycle}")
}
