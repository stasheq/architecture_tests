# Android App schema proposal
This app is created to show the idea of the architecture scheme that is:
- modular
- easy to test automatically
- easy to expand
- cool

Utils used:
- RxJava3 - for communication between modules
- Kotlin Coroutines - for doing time consuming jobs in logic
- JUnit  5 & kotest - for unit tests of logic module
- Retrofit & okhttp - for network communication
- Hilt & Dagger - for dependency injection
- Leak Canary - for memory leaks detection
- ViewModel from androidx - to provide Logic injection into views and to avoid Logic rebuilding when view is being rebuilt
- Android view binding, because Kotlin synthetics got deprecated [Info](https://developer.android.com/topic/libraries/view-binding/migration)

Good class to get an overall overview is [ListFragment.kt](app/src/main/java/me/szymanski/arch/ListFragment.kt)

The app shows list of Github repositories for a given user.\
After choosing a repository, app shows it's details.\
\
<img src="readmeImages/app.gif" alt="App" width="300"/>\
It supports bigger screens\
<img src="readmeImages/app_tablet.png" alt="On tablet" width="500"/>

## Modular
<img src="readmeImages/modules_studio.png" alt="Modules list" width="200"/>\
<img src="readmeImages/modules_diagram.png" alt="Architecture idea diagram" width="600"/>

## Logic
Communication from the app to the logic module:
- via method calls and setting its variables. `logic.loadNextPage()`

Communication from the logic module to the app:
- via exposed Observables `val list: Observable<List<Repository>>`

Example: [ListLogic.kt](logic/src/main/kotlin/me/szymanski/arch/logic/cases/ListLogic.kt)

## Unit tests of logic
Example: [ListLogic tests](logic/src/test/kotlin/me/szymanski/arch/logic/test/ListTest.kt)\
It uses [kotest](https://github.com/kotest/kotest) framework that allows to write unit tests efficiently.\
Setting `isolationMode = IsolationMode.InstancePerLeaf` allows to write tests `On download 2nd page` and
`On error downloading 2nd page` without taking care of preparing previous steps like `On started and download 1st page`.\
Whole path is executed for every leaf.\
Suggested method of running tests (because of nice UI output) is to run from Android Studio Gradle menu `logic/Tasks/verification/test`.

## Views

Specific parts of Views are created using xml layouts, but to make them more reusable they are composed
in the code.\
[ListScreen.kt](views/src/main/java/me/szymanski/arch/screens/ListScreen.kt) gives a good overview.
It combines multiple "Widgets" that are used also in other screens.
