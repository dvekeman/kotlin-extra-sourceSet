# Extra SourceSet Example

```
Gradle 5.2.1
Intellij 2019.1
Kotlin 1.3.21
```

Example which shows how to setup and use an additional source set.
This can be useful to organize your code as your codebase grows 
such that it can be refactored easily afterwards into separate libraries.

## The tricky part

**sourceSets** vs **kotlin.sourceSets**

This would be the ideal configuration using KotlinSourceSets...

**... but does not work in Intellij!**

```
kotlin.sourceSets {
    val commonMain by creating {
        dependencies {
            implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
        }
    }
}

kotlin.sourceSets["main"].dependsOn(kotlin.sourceSets["commonMain"])
```

Even though it works in Gradle it will give errors in Intellij (and also not recognize the extra module)

Here is the alternative 

```
sourceSets {
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
```

## Groovy Equivalent

For completeness, the groovy equivalent

**build.gradle**

```
plugins {
    id 'kotlin2js' version '1.3.21'
}

repositories {
    jcenter()
    mavenCentral()
}

sourceSets {
    "commonMain" {
        dependencies {
            implementation "org.jetbrains.kotlin:kotlin-stdlib-js"
        }
    }

    main {
        compileClasspath += sourceSets.commonMain.runtimeClasspath
    }
}

//kotlin.sourceSets {
//    "commonMain" {
//        dependencies {
//            implementation "org.jetbrains.kotlin:kotlin-stdlib-js"
//        }
//    }
//
//}
//kotlin.sourceSets["main"].dependsOn(kotlin.sourceSets["commonMain"])


dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib-js"
}

compileKotlin2Js {
    kotlinOptions {
        languageVersion = "1.3"
        moduleKind = "umd"
        sourceMap = true
        metaInfo = true
        main = "noCall"
    }
}
```

**settings.gradle**

```
pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin2js") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
        }
    }
}
rootProject.name = "extraSourceSetExample"
```