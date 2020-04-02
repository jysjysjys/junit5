import org.gradle.api.JavaVersion

object Versions {

    val jvmTarget = JavaVersion.VERSION_1_8

    // Dependencies
    val apiGuardian = "1.1.0"
    val junit4 = "4.13"
    val junit4Min = "4.12"
    val ota4j = "1.2.0"
    val picocli = "4.2.0"
    val univocity = "2.8.4"

    // Test Dependencies
    val archunit = "0.13.1"
    val assertJ = "3.14.0"
    val bartholdy = "0.2.3"
    val classgraph = "4.8.65"
    val commonsIo = "2.6"
    val coroutines = "1.3.3"
    val groovy = "3.0.2"
    val log4j = "2.13.1"
    val mockito = "3.3.3"
    val slf4j = "1.7.30"

    // Tools
    val checkstyle = "8.25"
    val jacoco = "0.8.5"
    val jmh = "1.23"
    val ktlint = "0.35.0"
    val surefire = "2.22.2"
    var bnd = "5.0.0"

}
