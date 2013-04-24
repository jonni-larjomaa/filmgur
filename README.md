filmgur
=======

This is a projectwork for Metropolia UAS Android application development course.
It uses google drive to store and retrieve images taken with android device. Thus cannot be used without google account.

*Features made*

1. Login to your google drive.
2. Browse all folders/albums in your google drive
3. Create albums / folders.
3. Show all image files from selected album (only "image/jpeg"-mimetyped object are fetched)
4. Display selected image in a dialog. (supports rotating of image by 90 degrees at a time)
5. Pictures are automatically stored in selected album.
6. Removing of multiple albums / pictures by contextual menus.

*Future Features*

1. Caching of frequently accessed files (some like 10-20 images at time and older ones gets always removed)
2. Saving the rotated image back to google drive.
3. Adding geolocation information to picture metadata (supported by google drive natively).
4. Adding thumbnails of images to listviews.
5. and whatever comes in to my mind.

The application uses ActionBarSherlock and Google play services libs.
before building google play services must be applied to the project as library project.

*Application building!*

1. git clone on this repo
2. import to eclipse
3. add google-play-services-lib as library project and attach to this one (http://developer.android.com/google/play-services/setup.html)
4. Attach real device to computer
5. run the app on real device (cant use emulator because of google play service dependency and those are not available for the emulators)

*Libraries Used!*

1. ActionBarSherlock (included as submodule)
2. google-play-services-lib (needs to be added to project from sdk)
