import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.googleServices)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiTooling)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.materialIconsExtended)
            implementation(libs.compose.material3) // pinned to 1.12.0-alpha03 in libs.versions.toml
            // NOT compose.material3 -- that's the Compose plugin's own accessor, which
            // resolves to Compose Multiplatform 1.12.0's bundled stable material3 1.9.0,
            // where every Expressive API (MaterialExpressiveTheme, ButtonGroup, ToggleButton,
            // FloatingToolbar) was removed.
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.kotlinx.datetime)
            implementation(libs.navigation.compose)
            implementation(libs.firebase.auth)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

compose.resources {
    packageOfResClass = "com.example.cherrypick.resources"
}

android {
    // ponytail: com.example.* is rejected by Google Play at submission time --
    // rename the package (here and in google-services.json's registered app)
    // and re-register the Firebase app under project cherrypick-6 before shipping.
    namespace = "com.example.cherrypick"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.cherrypick"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    // GitLive's Android artifacts declare com.google.firebase:* deps with no
    // version, relying on the BoM to pin them -- so androidMain needs it too.
    implementation(platform(libs.firebase.bom))
}
