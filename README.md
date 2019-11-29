# GeoNotes

## Description
A simple mobile application that allows users to save location based notes on a map. These notes can be displayed on the map where they were saved and viewed by the user that created the note.

## Features
List the main features of your app. For example:
1. Google+ login
2. Display list of notes
3. Location Based
4. Google Maps Clustering
5. Swipe to delete (both directions) on Notes List
6. Long press on note item to add it on home screen App widget

## Technology Stack Used
1. Android (Java)
2. Firebase (Google Login)
3. Firestore (for data persistence)
4. Google Cluster API (to cluster all pointers in that area)
5. Lottie (for animation)
6. MaterialDialog (for displaying saved noted Info)
7. Butterknife (View Injection)

## Commit History
Commits are made at regular intervals and all those can be seen [here](https://github.com/nasreekar/GeoNotes/commits/master)

## Future Updates/Modifications
Many functionalities can either be added or modified to the current application. 
Few of the things that can be either modified or added are:
1. Edit Note functionality
2. Color coding of notes
3. Better Architecture pattern (MVP/MVVM, SOLID etc)
4. Update App widget to show a list of all notes added (only title) and clicking on any item to open details of the note
5. Allow the user to set a note private so that its not displayed in other's map 

## Bugs
There might be some bugs that would have crept in and went unnoticed while developing, but my highest priority is to implement the functionality as per requirements and these bugs would be fixed in coming updated versions/releases.

## Testing
For testing purposes, you can run use the following credentials.

email: abc@gmail.com

password: password

## Screens
Map screen (FootprintFragment) - Displays all the pointers of all users (color coded. Red for own notes, Blue for other users markers)

Notes List (NotesListFragment) - Displays only Notes of the logged in user (To read the notes of other users, navigate to map screen and click on any `blue` marker which will show a window and on clicking on that a bottom dialog appears with more details.) Clicking on any of the card views in this screen will open a bottom dialog sheet with more details. Clicking on the map in card view will open google maps.

| ![Home Screen - GeoNotes](https://i.postimg.cc/MHSMSd7G/Screenshot-2019-11-26-at-5-59-23-PM.png) | ![HomeScreen](https://i.postimg.cc/PxZp9B8c/Screenshot-2019-11-26-at-5-57-19-PM.png)| ![Marker Window](https://i.postimg.cc/gJvjN8Cb/Screenshot-2019-11-26-at-5-57-31-PM.png) | ![Details Screen Extended](https://i.postimg.cc/fbSJS65L/Screenshot-2019-11-26-at-5-57-42-PM.png) |
|:---:|:---:|:---:|:---:|
| ![NotesList](https://i.postimg.cc/zGcyBJC6/Screenshot-2019-11-26-at-5-57-58-PM.png)| ![Share](https://i.postimg.cc/YqtGDD2n/Screenshot-2019-11-26-at-5-58-17-PM.png) | ![Profile](https://i.postimg.cc/3RMWXnbZ/Screenshot-2019-11-26-at-5-59-00-PM.png) | ![Favorites](https://i.postimg.cc/wMQtTL2Q/Screenshot-2019-11-26-at-5-58-31-PM.png)

## App Functionality

![video](https://media.giphy.com/media/QtvDhnGCIuMs2reCYx/giphy.gif)

## Hiding API Keys
The problem I faced while setting up the repository is I entered the API key in a tracked file and pushed it directly to Github. Checking this into source control can expose your key to the public and And I started receiving emails from GitGuardian that I'm exposing the API keys. 

[![Screenshot-2019-08-14-at-12-11-05-PM.png](https://i.postimg.cc/SxS8kFfY/Screenshot-2019-08-14-at-12-11-05-PM.png)](https://postimg.cc/9rkzYkSc)

So I had to figure out how to fix this issue and found these articles helpful. 

* https://richardroseblog.wordpress.com/2016/05/29/hiding-secret-api-keys-from-git/


## Contribution

1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -am 'Added some feature')
4. Push to the branch (git push origin my-new-feature)
5. Create new Pull Request

## License
[MIT](https://github.com/nasreekar/license/blob/master/LICENSE)

