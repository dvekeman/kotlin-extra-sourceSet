import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

plugins {
    id("kotlin2js") version "1.3.21"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-js"))
}

tasks {

     "compileKotlin2Js"(Kotlin2JsCompile::class)  {
         kotlinOptions {
             languageVersion = "1.3"
             moduleKind = "umd"
             sourceMap = true
             metaInfo = true
             main = "call"
         }
     }

}

// This works for Intellij *and* Gradle
// The output are 2 js files:
// |- extraSourceSetExample.js
// |- extraSourceSetExample_commonMain.js
// >>
sourceSets {
    // Add a source set to contain all sources which are common between the
    // frontend and the backend (e.g. model entities, constants, ...)
    val commonMain by creating {
        dependencies {
            implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
        }
    }

    main {
        compileClasspath += sourceSets["commonMain"].runtimeClasspath
    }
}

configurations {
    val commonMainImplementation by getting {
        extendsFrom(configurations["implementation"])
    }
}
// <<

// This only works for Gradle, but not for Intellij
// The output is 1 js: extraSourceSetExample.js
// >>
//kotlin.sourceSets {
//    val commonMain by creating {
//        dependencies {
//            implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
//        }
//    }
//}
//
//kotlin.sourceSets["main"].dependsOn(kotlin.sourceSets["commonMain"])
// <<