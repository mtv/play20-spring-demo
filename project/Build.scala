import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play-spring"
    val appVersion      = "1.0.0-SNAPSHOT" // Semantic Versioning - http://semver.org/

	val appDependencies = Seq(
		javaCore,

		// Spring dependency
		"org.springframework" % "spring-context" % "3.2.1.RELEASE"
	)

	val main = play.Project(appName, appVersion, appDependencies).settings(
		// Add your own project settings here      
	)

}
