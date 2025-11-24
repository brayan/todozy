import com.android.build.gradle.BaseExtension
import org.gradle.api.GradleException
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(BuildPlugin.android)
        classpath(BuildPlugin.kotlin)
        classpath(BuildPlugin.googleServices)
        classpath(BuildPlugin.crashlytics)
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

subprojects {
    plugins.withId("com.android.application") {
        extensions.configure<BaseExtension>("android") {
            compileOptions {
                isCoreLibraryDesugaringEnabled = true
            }
        }
        dependencies.add("coreLibraryDesugaring", Desugar.jdkLibs)
    }
    plugins.withId("com.android.library") {
        extensions.configure<BaseExtension>("android") {
            compileOptions {
                isCoreLibraryDesugaringEnabled = true
            }
        }
        dependencies.add("coreLibraryDesugaring", Desugar.jdkLibs)
    }
}

var testsTotal = 0L
var testsSuccess = 0L
var testsFailed = 0L
var testsSkipped = 0L

allprojects {
    repositories {
        google()
        mavenCentral()
    }
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

// Test Logging
    tasks.withType(Test::class) {
        testLogging {
            events(
                TestLogEvent.FAILED,
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.STANDARD_OUT,
            )
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = true

            addTestListener(
                object : TestListener {
                    override fun beforeSuite(suite: TestDescriptor) {
                    }

                    override fun beforeTest(testDescriptor: TestDescriptor) {
                    }

                    override fun afterTest(
                        testDescriptor: TestDescriptor,
                        result: TestResult,
                    ) {
                    }

                    override fun afterSuite(
                        suite: TestDescriptor,
                        result: TestResult,
                    ) {
                        if (suite.parent == null) {
                            println("\nTest result: ${result.resultType}")
                            println(
                                "Test summary: ${result.testCount} tests, " +
                                    "${result.successfulTestCount} succeeded, " +
                                    "${result.failedTestCount} failed, " +
                                    "${result.skippedTestCount} skipped",
                            )

                            testsTotal += result.testCount
                            testsSuccess += result.successfulTestCount
                            testsFailed += result.failedTestCount
                            testsSkipped += result.skippedTestCount

                            println(
                                "Total: $testsTotal tests, " +
                                    "$testsSuccess succeeded, " +
                                    "$testsFailed failed, " +
                                    "$testsSkipped skipped",
                            )
                        }
                    }
                },
            )
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.register("forbidSimpleDateFormat") {
    group = "verification"
    description = "Fails if legacy java.text.SimpleDateFormat is used."
    doLast {
        val forbidden =
            fileTree(rootDir) {
                include("**/*.kt", "**/*.java")
                exclude("**/build/**")
            }.files.filter { it.readText().contains("SimpleDateFormat") }

        if (forbidden.isNotEmpty()) {
            val files = forbidden.joinToString(separator = "\n") { " - ${it.relativeTo(rootDir)}" }
            throw GradleException("Legacy SimpleDateFormat usage found:\n$files")
        }
    }
}

tasks.register("checkCrossModuleRImports") {
    group = "verification"
    description = "Fails if app R is imported outside the app module."
    doLast {
        val offenders =
            fileTree(rootDir) {
                include("**/*.kt", "**/*.java")
                exclude("**/build/**", "app/**")
            }.files.filter { it.readText().contains("import br.com.sailboat.todozy.R") }

        if (offenders.isNotEmpty()) {
            val files = offenders.joinToString(separator = "\n") { " - ${it.relativeTo(rootDir)}" }
            throw GradleException("Cross-module app R imports found:\n$files")
        }
    }
}

// Convenience aggregate task for local checks
tasks.register("staticAnalysis") {
    group = "verification"
    description = "Runs ktlint and Android lint across the project"
    dependsOn("ktlintCheck", "lint", "forbidSimpleDateFormat")
}
