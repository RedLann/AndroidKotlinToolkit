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

    dataBinding {
        isEnabled = true
    }
}

publish {
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
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.1.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.1.0")
}
