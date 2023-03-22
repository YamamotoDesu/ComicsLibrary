# ComicsLibrary

## Project Setup
### API CONFIG
<img width="396" alt="スクリーンショット_2023_03_22_8_55" src="https://user-images.githubusercontent.com/47273077/226767782-9478410d-c439-43dc-9d4a-ad662b6cecf6.png">
build.grale(app)

```gradle
def apikeyPropertiesFile = rootProject.file("apikey.properties")
def apikeyProperties = new Properties()
apikeyProperties.load(new FileInputStream(apikeyPropertiesFile))

    defaultConfig {
        applicationId "com.codewithkyo.comicslibrary"
        minSdk 24
        targetSdk 32
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary true
        }

        buildConfigField("String", "MARVEL_KEY", apikeyProperties['MARVEL_KEY'])
        buildConfigField("String", "MARVEL_SECRET", apikeyProperties['MARVEL_SECRET'])
    }
```


