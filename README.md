# AngularCordovaDemo

- This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 6.0.8.
- A cordova project using angular 6
# Run the following cmd
- $ cordova platform add ios 
- $ cordova platform add android
- $ cordova build ios (/Users/xxx/IdeaProjects/cordovaandangular/platforms/ios/build/emulator/cordovaangular.app)
- $ cordova build android (/Users/xxx/IdeaProjects/cordovaandangular/platforms/android/app/build/outputs/apk/debug/app-debug.apk)
- $ ng build (will output to www folder for cordova to render)
-- make changes to contact-plugin-test project<br>
-- then run the following commands<br>
-- take the .apk file and drop into the emulator
- $ cordova plugin remove contactplugin
- $ cordova plugin add ../contact-plugin
- $ ng build
- $ cordova build android

## Run and test on Android Studio
- import the contact-plugin-test project path (contact-plugin-test/platforms/android) to Android Studio
- then write code inside the contact-plugin-test project under contact-plugin-test/platforms/android/src/
- use Android Studio play button to run the emulator and debug the Java code
- *** After done writting/debugging Java code, copy the Java files to contact-plugin project ***
- then run the following commands and build the .apk to run on your emulator
- $ cordova plugin remove contactplugin
- $ cordova plugin add ../contact-plugin
- $ ng build
- $ cordova build android
## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
