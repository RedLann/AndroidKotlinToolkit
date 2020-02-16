
object Modules {
    const val compileSdk = 29
    const val minSdk = 23
    const val targetSdk = 29
    const val buildToolsVersion = "29.0.3"
    const val dryRun = "true"
}

object Versions {
    const val gradle = "3.5.3"
    const val kotlin = "1.3.50"
    const val appcompat = "1.0.2"
    const val lifecycle = "2.2.0"

    /* test */
    const val junit = "4.12"
}

object Bintray {
    const val bt_userOrg = "vidi"
    const val bt_groupId = "com.github.redlann"
    const val bt_repoName = "AndroidKotlinToolkit"
    const val bt_website = "https://github.com/RedLann/AndroidKotlinToolkit"
}

object Libs {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
}

object TestLibs {
    const val junit = "junit:junit:${Versions.junit}"
}
