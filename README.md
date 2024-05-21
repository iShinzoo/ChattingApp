# RunTrack

ChatterBox is a modern chatting application built on Android, leveraging cutting-edge
technologies such as Jetpack Compose,MVVM architecture, FireStore , Firebase Authentication
and Firebase Storage. The app enables users to engage in real-time conversations with
their contactsin a sleek and intuitive interface.

## Features
1. Real-time Messaging: Enjoy seamless communication with friends and family in real-time,
  powered by FireStore Database.

2. Intuitive User Interface: Experience a sleek and user-friendly interface designed with
  Jetpack Compose, ensuring smooth navigation and delightful interactions.

3. Foreground Service: Even when the app is closed or running in the background, ChatBox
 continues to receive and deliver messages efficiently, thanks to Foreground Service integration.

4. Advanced Navigation: Seamlessly navigate through the app with nested navigation and deep
  linking, facilitated by Jetpack Navigation Component.

5. Security and Privacy: Prioritize user privacy and security with end-to-end encryption
 for messages, ensuring confidential conversations stay private.


## Screenshot


## Package Structure

* `data` : contains data class.
* `di` : Hilt Modules.
* `ui`
    * `nav`: Contains app navigation and destinations.
    * `screen`: Contains UI.
    * `theme`: Material3 theme.
    * `utils`: UI utility classes and common components.


## Build With

[Kotlin](https://kotlinlang.org/):
As the programming language.

[Jetpack Compose](https://developer.android.com/jetpack/compose) :
To build UI.

[Jetpack Navigation](https://developer.android.com/jetpack/compose/navigation) :
For navigation between screens and deep linking.


[Firebase](https://firebase.google.com/docs/build) :
To track user's running activity such as speed, distance and path on the map.

[Hilt](https://developer.android.com/training/dependency-injection/hilt-android) :
For injecting dependencies.

[Coil](https://coil-kt.github.io/coil/compose/) :
To load image asynchronously.

## Architecture

This app follows MVVM architecture, Uni Directional Flow (UDF) pattern and Single architecture
pattern.


## Installation

Simple clone this app and open in Android Studio.


## Project Status

These features are left to be implemented:

1. Audio and video Calling.
2. Sharing Media.
