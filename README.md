![](https://github.com/sava-software/sava/blob/003cf88b3cd2a05279027557f23f7698662d2999/assets/images/solana_java_cup.svg)

# Solana Web2 [![Build](https://github.com/sava-software/solana-web2/actions/workflows/gradle.yml/badge.svg)](https://github.com/sava-software/solana-web2/actions/workflows/gradle.yml) [![Release](https://github.com/sava-software/solana-web2/actions/workflows/release.yml/badge.svg)](https://github.com/sava-software/solana-web2/actions/workflows/release.yml)

### Disclaimer

In addition to the MIT License, this project is under active development and breaking changes are to be expected.

### Requirements

- The latest generally available JDK. This project will continue to move to the latest and will not maintain
  versions released against previous JDK's.

### Dependencies

- [sava-core](https://github.com/sava-software/sava)
- [sava-rpc](https://github.com/sava-software/sava)

### Add Dependency

Create
a [GitHub user access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic)
with read access to GitHub Packages.

Then add the following to your Gradle build script.

```groovy
repositories {
  maven {
    url = "https://maven.pkg.github.com/sava-software/sava"
    credentials {
      username = GITHUB_USERNAME
      password = GITHUB_PERSONAL_ACCESS_TOKEN
    }
  }
  maven {
    url = "https://maven.pkg.github.com/sava-software/solana-web2"
  }
}

dependencies {
  implementation "software.sava:sava-core:$VERSION"
  implementation "software.sava:sava-rpc:$VERSION"
  implementation "software.sava:solana-web2:$VERSION"
}
```

### Contribution

Unit tests are needed and welcomed. Otherwise, please open an issue or send an email before working on a pull request.
