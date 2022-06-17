import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

buildscript {

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.fabric.io/public") }
    }
    dependencies {
        classpath(BuildPlugin.android)
        classpath(BuildPlugin.kotlin)
        classpath(BuildPlugin.googleServices)
        classpath(BuildPlugin.crashlytics)
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
}

private var testsTotal = 0L
private var testsSuccess = 0L
private var testsFailed = 0L
private var testsSkipped = 0L

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.fabric.io/public") }
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

            addTestListener(object : TestListener {
                override fun beforeSuite(suite: TestDescriptor) {}
                override fun beforeTest(testDescriptor: TestDescriptor) {}
                override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
                override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                    if (suite.parent == null) {
                        println("\nTest result: ${result.resultType}")
                        println(
                            "Test summary: ${result.testCount} tests, " +
                                "${result.successfulTestCount} succeeded, " +
                                "${result.failedTestCount} failed, " +
                                "${result.skippedTestCount} skipped"
                        )

                        testsTotal += result.testCount
                        testsSuccess += result.successfulTestCount
                        testsFailed += result.failedTestCount
                        testsSkipped += result.skippedTestCount

                        println(
                            "Total: $testsTotal tests, " +
                                "$testsSuccess succeeded, " +
                                "$testsFailed failed, " +
                                "$testsSkipped skipped"
                        )
                    }
                }
            })
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
