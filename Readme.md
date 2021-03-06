
<p align="center">
	<img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/app/src/main/res/drawable/icon.png" width="220">
</p>

# AmplyWeather

AmplyWeather is an Android application that allows users to check the weather using Amply Weather API for their current location and for other locations using the ZIP Code.
This was developed as a part of the case study given by Amply Media.



## Installation

Clone this repository using,

```
git clone https://github.com/Abhishekds94/Amply-Weather.git
```

## Features

1. Dynamic UI elements throughout the application that renders based on the system time
2. Quick and reliable weather source
3. Ability to check 3-day forecast
4. Ability to check other location weather forecast (throughout the USA)
5. Push notifications to the users at 7:30 hours every day that contains the day's forecast and the average temperature
6. Ready to use Google Admob (Banner) Ads


## Installation

You can build the app with Android Studio or with `./gradlew assembleDebug` command.


## Technologies Used
1. [Android (Java)](https://developer.android.com/guide) - To develop the application using different Android components in Java programming language
2. [Lottie Animation](https://airbnb.design/lottie/) - To display animations 
3. [OkHttp](https://square.github.io/okhttp/) - To retrieve the data from API
4. [Volley](https://developer.android.com/training/volley) - To retrieve the data from API (for location update)
5. [Material Design](https://material.io/develop/android/) - For components such as CardView, Toolbar, and so on
6. [Alarm Manager](https://developer.android.com/reference/android/app/AlarmManager) - To set Push Notifications at 07:30 hours every day
7. [Picasso](https://github.com/square/picasso) - To render the weather icons from the API response
8. [Admob - Banner Ads](https://developers.google.com/admob/android/banner) - To display Banner Ads in the app
9. [Esporesso Testing](https://developer.android.com/training/testing/espresso) - To test the UI components of the app
10. [Flaticon](https://www.flaticon.com/) - For all the icons used in the app


## Applicaiton target platform
* `minSdkVersion` - 22
* `targetSdkVersion` - 29
* `compileSdkVersion` - 29
* `buildToolsVersion` - 29.0.3
* `gradle` - 3.5.1


## Screenshots

###### Morning UI -
<div>
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/morning_splash.jpg" width="220">

  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/morning_dashboard.jpg" width="220">

  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/morning_locUpdate.jpg" width="220">
  
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/morning_locUpdateResult.jpg" width="220">
  
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/morning_locUpdateError1.jpg" width="220">
  
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/morning_locUpdateError2.jpg" width="220">
  
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/morning_locUpdateErrorResult.jpg" width="220">
</div>


###### Noon UI -
<div>
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/noon_splash.jpg" width="220">

  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/noon_dashboard.jpg" width="220">

  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/noon_locUpdate.jpg" width="220">
  
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/noon_locUpdateResult.jpg" width="220">
  
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/noon_locUpdateError1.jpg" width="220">
  
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/noon_locUpdateError2.jpg" width="220">
  
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/noon_locUpdateErrorResult.jpg" width="220">
</div>


###### Night UI -
<div>
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/night_splash.jpg" width="220">

  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/night_dashboard.jpg" width="220">

  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/night_locUpdate.jpg" width="220">
  
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/night_locUpdateResult.jpg" width="220">
  
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/night_locUpdateError1.jpg" width="220">
  
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/night_locUpdateError2.jpg" width="220">
  
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/night_locUpdateErrorResult.jpg" width="220">
</div>

###### Notification at 7:30 hours everyday
  <img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/notification.jpg" width="220">

## App Working Video
<a href="https://youtu.be/6ast5TCaF9g" target="_blank"><img src="https://github.com/Abhishekds94/Amply-Weather/blob/master/screenshots/video.jpg" 
alt="Video Working" width="220" /></a>

## Future Scope
Below are a few of the ideas that I could think of to enhance the application,
* Adding a dynamic notification service based on the change in weather
* Adding a home screen widget
* Adding [Interstitial ads](https://developers.google.com/ad-manager/mobile-ads-sdk/android/interstitial) to monetize the app
* Change UI based on current weather

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
