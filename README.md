# AppChat
WebChat is a android-based chat application built using Android Studio.

<!-- TOC -->
- Markdown Navigation
    - [Description](#Description)
    - [Features](#Features)
    - [Installing And Executing](#Installing-And-Executing)
    - [Authors](#Authors)
<!-- /TOC -->

## Description
This project is an android chat application which allows multiple users to communicate with each other in real-time via a simple and user-friendly interface.

Submissions:

## Features
* **Login page:**
On the login page, the user is asked to enter the username he registered with and the appropriate password.
If a user is not yet registered to the application, he will be given the option to go to the registration page where he can enter the relevant data and register.
Otherwise, if the username and code are correct, he will be redirect to the chat list page.

* **Sign up page:**
On the registration page, the user will be asked to enter relevant information for registering to the application, such as a password and a username with which he would connect to the application, in addition, the user can add a profile picture that will be displayed to the contacts who will contact  with him.
We implemented a validation logic on the input fields, and presented a clear visual indication to the user which values are allowed to be entered. We also sent him a corresponding message when he entered bad values.

![image](https://github.com/DanSaada/AppChat-Android/assets/112869076/859a6290-261b-4a1a-963b-7de446b6a5de)


* **Chat page:** On the chat page the user can add contacts to his contacts list (by clicking on the icon in the right bottom) and talk with them.
In addition to sending messages.
The user can send a message by pressing the send button (in the shape of an airplane) which is placed just on the right of the input box.
In addition, the user can go back to the chat list and switch between chats and see for each contact the history of messages between him and the choosen contact. The name and photo of the person that the user choosed to contact with, will appear in the title of the chat.
The user has a button to logout from the application which, when clicked, allows him the opportunity to regret and stay in the application, or to continue with the action and disconnect, which will take him to the login page. If he connects again, the history of contacts and conversations with them will be saved and restored frpm the database.
The chat list show all the contacts that the user choose to add to the app, and also shows the last message that was sent and when it was sent. If the user added a contact but didn't start a conversation with him the contact box in the chat list of that user will be empty, but when entering that chat the user will see a message saying: "No messages yet" which will indicate that there is no chat history between him and that contact.

![image](https://github.com/DanSaada/AppChat-Android/assets/112869076/43491eb5-4638-4595-bad1-8aa16cf6d9d3)


* **Setting page:** On this screen you can edit relevant settings of the application. It is possible to edit the address of the server that the user is working against. Likewise, there is a button on the settings screen that changes the theme of the application. That is, when changing the setting, the colors of the application will change. There is also a button for to logout from the application. The Setting screen can be accessed from the chat list, login and register's screens.


* **Database:** The application stores a local copy of chats, messages, and relevant information, if available. For instance, upon opening the application, data is extracted from the local SQLite DB using Room. In the background, synchronization occurs with the server, and the response received from the server updates the local database, consequently updating the visual interface.
Similarly, when a contact is clicked, the conversation with that contact is loaded using the local DB (Room), while new messages, if any, are simultaneously fetched from the server and saved in the local database, refreshing the display.
It's important to note that the server itself utilizes MongoDB to store its information.


* **Communication:** The application's components interact with the server-side API for various functionalities such as registration, connection, managing contacts, initiating chats, and sending/receiving messages. The API delivers the received information to the relevant sections within the application and also sends the necessary information back to the server.
Using the Android client, users can send and receive messages from other clients, whether it's through the Android app itself or the web interface, which is also implemented and available.

![image](https://github.com/DanSaada/AppChat-Android/assets/112869076/1ecd5827-7b23-40d3-b293-cb75a636145e)



* **Notifications:** The server leverages Notifications Push to deliver messages to clients. This functionality is implemented using Firebase, specifically for handling push notifications. However.
When a user add a contact or sends a message through the server, it will be pushed to the recipient's device, triggering a corresponding notification. If the recipient's device is currently inactive, the messages will be received and displayed upon activating the application, as it synchronizes with the server.

![image](https://github.com/DanSaada/AppChat-Android/assets/112869076/11155b3d-caa2-4d78-af8b-10427b1a2170)


* **Architecture:** We implemented a structured architecture consisting of Activity, ViewModel, Repository, API, and server components. The Activity handles the user interface, while the ViewModel manages data preparation and user input. The Repository serves as a central data management hub, interfacing with both local (Room) and remote (web service) data sources. The API enables communication between the application and the server, facilitating data exchange. This architecture promotes modularity, testability, and efficient data flow.

![image](https://github.com/DanSaada/AppChat-Android/assets/112869076/7d810deb-5936-4eae-9a39-8cb729ce69f0)



## Installing And Executing
    
To clone and run this application, you'll need [Git](https://git-scm.com) installed on your computer.
  
From your command line:
  
```bash
# Clone this repository.
$ git clone https://github.com/DanSaada/AppChat-Android.git


# For running the Server - go into the repository.
$ cd AppChat-Android\app\src\main\java\com\appchat\Server

# start the server (copy the Server folder into a different project and execute it from there).
$ npm start

# For running the Client - go into the repository.
$ cd AppChat-Android

# start the client.
# You will have to use an emulator for an android application.

```
  
## Authors
- [Dan Saada](https://github.com/DanSaada)
- [Ofir Helerman](https://github.com/OfirHelerman)
- [Idan Inbar](https://github.com/idaninbar)

