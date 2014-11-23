android-Math-Get-To-24
======================
[![Build Status](https://travis-ci.org/tonytamsf/android-Math-Get-To-24.svg?branch=master)](https://travis-ci.org/tonytamsf/android-Math-Get-To-24)

Android version of Math Get To 24

### TODO
- tap to end the FYI screen
- add sound

### Build
- broken July 6, 2014
   - https://github.com/travis-ci/travis-ci/issues/2470

### Troubleshooting

```
Error:(9) A problem occurred evaluating root project 'android-HowToTip'.
> Gradle version 1.10 is required. Current version is 1.12. If using the gradle wrapper, try editing the distributionUrl in /Users/tonytam/git/android-HowToTip/gradle/wrapper/gradle-wrapper.properties to gradle-1.10-all.zip
````

chnage build.gradle
```
  dependencies {
        classpath 'com.android.tools.build:gradle:0.11+'
    }
```
