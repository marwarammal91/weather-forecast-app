# weather-forecast-app
Sample android application to fetch the weather forecast of multiple cities using Open Weather API - [OpenWeatherAPI](https://openweathermap.org/)

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone https://github.com/marwarammal91/weather-forecast-app.git
```

## Build variants
Use the Android Studio *Build Variants* button to choose between **production** and **staging** flavors combined with debug and release build types


## Generating signed APK
From Android Studio:
1. ***Build*** menu
2. ***Generate Signed APK...***
3. Fill in the keystore information *(you only need to do this once manually and then let Android Studio remember it)*


## Generate jacoco report
From Android Studio:
1. Go to ***terminal*** 
2. Run the command: `./gradlew createDebugCoverageReport`
3. Go to path to build directory `forecast-weather-app/app/build/reports/coverage`
4. Click on ***index.html*** to see the report


## Maintainers
This project is mantained by:
* [Marwa Rammal](http://github.com/marwarammal91)


## Contributing

1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -m 'Add some feature')
4. Run the linter (gradlew ktlintFormat').
5. Push your branch (git push origin my-new-feature)    
6. Create a new Pull Request