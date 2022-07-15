# DataComm
An app for purchasing Airtime and Data subscription at a cheaper price 

## Table of Contents
* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Screenshots](#screenshots)
* [Setup](#setup)
* [Usage](#usage)
* [Project Status](#project-status)
* [Room for Improvement](#room-for-improvement)
* [Acknowledgements](#acknowledgements)
* [Contact](#contact)
<!-- * [License](#license) -->

## General Information
- Buy airtime and mobile internet data at a more cheapter price with this android application
## Technologies Used
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [Android Jetpack Navigation](https://developer.android.com/guide/navigation)
- [Retrofit for networking](https://square.github.io/retrofit/)
- and more...


## Features
- Check Balance
- Buy Airtime
- Buy Data
- Swipe to refresh
- Network error handling
- 
## Screenshots
![Untitled design](https://user-images.githubusercontent.com/54691862/179222246-868c65ea-09e7-4487-b55e-ef2c42ced082.png)

## Setup
To setup the project, go to [paytev.com](https://client.paytev.com/customer/login?next=/customer/) and create an account
- Complete your profile
- Open on the menu pane on the left and expand "Developer's API" option
- Click on "Generate Token" to generate your API Token
- Open Android Studio and expand the Project pane on the left(Ensure you are on the Project View)
- On the Project view, open local.properties file
- Inside the file, create a variable PAYTEV_API_KEY and set the value to your Api key from [paytev.com](https://client.paytev.com/customer/login?next=/customer/) like this:
PAYTEV_API_KEY = YOUR_API_KEY_HERE
- Rebuild the application and install. 
- You can fund your wallet from the website and it will be reflected on the app

## Project Status
Project is: _in progress_ 

To do:
- Implement dependency injection for injecting dependencies
- Implemnet Kotlin Flows for Asynchronous streams

## Acknowledgements
- This project was inspired when i visited [paytev.com](https://paytev.com) and discovered that they have an API. 
I decided to build this application with it
