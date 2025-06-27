plugins {
  id("software.sava.build.feature.publish-maven-central")
}

dependencies {
  nmcpAggregation(project(":solana-web2"))
}

tasks.register("publishToGitHubPackages") {
  group = "publishing"
  dependsOn(
    ":solana-web2:publishMavenJavaPublicationToSavaGithubPackagesRepository"
  )
}
