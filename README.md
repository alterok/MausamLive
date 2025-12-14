# MausamLive - Indian Weather App

MausamLive is an Indian weather app built on Zomato's WeatherUnion service to provide real-time weather data. The app offers a user-friendly interface with light and dark mode options, featuring a map view of cities and detailed weather information for selected localities.

## Supported Weather Sensors
- Temperature (C)
- Wind Speed (m/s)
- Wind Direction (degrees)
- Humidity (%)
- Rain Intensity (mm/min)
- Rain Accumulation (mm since 12AM)
  
## Features
- Real-time weather updates
- Light and Dark mode
- Map view of cities with weather sensors
- Detailed weather data for selected localities

## Architecture
- MVVM design pattern
- Reactive data flow using Flow and Coroutines
- Offline data persistance using ROOM
- Map integration using MapBox with location search

## Screenshots
<p float="left" >
    <img src="https://github.com/alterok/MausamLive/blob/9f69b834fa697e683b23f8ff4c0320bc634fdaa6/screenshots/screenshot_light_dashboard.png" width="25%"/>
    <img src="https://github.com/alterok/MausamLive/blob/9f69b834fa697e683b23f8ff4c0320bc634fdaa6/screenshots/screenshot_dark_mausam_2.png" width="25%"/>
</p>

## Installation

To install and run MausamLive, follow these steps:

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/MausamLive.git
    ```

2. Open the project in Android Studio.

3. Change/add the string resource in strings.xml file:
   ```xml
   <string name="api_key_release">GET YOUR API FROM weatherunion.com AND PUT HERE</string>
   <string name="mapbox_key_public">GET YOUR PUBLIC API FROM mapbox.com AND PUT HERE</string>```
4. Add a gradle.properties file to the users/.gradle/ directory with contents:
   ```groovy
   MAPBOX_DOWNLOADS_TOKEN=[GET SECRET TOKEN FROM MAPBOX DASHBOARD AND PUT IT HERE WITHOUT [] ]
   ```
6. Build and run the project on an Android device or emulator.
