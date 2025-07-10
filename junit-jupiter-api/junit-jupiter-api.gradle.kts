plugins {
	id("junitbuild.kotlin-library-conventions")
	id("junitbuild.java-nullability-conventions")
	id("junitbuild.code-generator")
	`java-test-fixtures`
}

description = "JUnit Jupiter API"

dependencies {
	api(platform(projects.junitBom))
	api(libs.opentest4j)
	api(projects.junitPlatformCommons)

	compileOnlyApi(libs.apiguardian)
	compileOnlyApi(libs.jspecify)

	compileOnly(kotlin("stdlib"))

	testFixturesImplementation(libs.assertj)
	testFixturesImplementation(testFixtures(projects.junitPlatformCommons))

	osgiVerification(projects.junitJupiterEngine)
	osgiVerification(projects.junitPlatformLauncher)
}

tasks {
	jar {
		bundle {
			val version = project.version
			bnd("""
				Require-Capability:\
					org.junit.platform.engine;\
						filter:='(&(org.junit.platform.engine=junit-jupiter)(version>=${'$'}{version_cleanup;${version}})(!(version>=${'$'}{versionmask;+;${'$'}{version_cleanup;${version}}})))';\
						effective:=active
			""")
		}
	}
}
