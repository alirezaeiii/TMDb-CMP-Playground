This is a Compose Multiplatform project targeting Android, iOS.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

## Screenshots
<p align="center">
  <img src="https://github.com/alirezaeiii/Disney-Compose-Multiplatform/blob/main/screenshots/screenshot1.gif" width="250" />
</p>

## Libraries
* [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform) - Compose Multiplatform, a modern UI framework for Kotlin that makes building performant and beautiful user interfaces.
* [Jetbrain Navigation Compose](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation-routing.html) - The Navigation component provides support for Jetpack Compose applications. You can navigate between composables while taking advantage of the Navigation component’s infrastructure and features.
* [Ktor Client](https://ktor.io/docs/welcome.html) - Ktor includes a multiplatform asynchronous HTTP client, which allows you to make requests and handle responses.
* [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - Kotlin multiplatform / multi-format reflectionless serialization
* [ViewModel](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-viewmodel.html) - ViewModel is designed to store and manage UI-related data in a lifecycle conscious way. This allows data to survive configuration changes such as screen rotations.
* [Room] (https://developer.android.com/kotlin/multiplatform/room) - is a library for data storage persistence which provides an abstraction layer over SQLite.
* [Koin](https://github.com/InsertKoinIO/koin) is a lightweight dependency injection framework for Kotlin & Kotlin Multiplatform.
* [Kotlin coroutines](https://developer.android.com/kotlin/coroutines) - Executing code asynchronously.
* [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) - is a state-holder observable flow that emits the current and new state updates to its collectors.
* [Coil-compose](https://coil-kt.github.io/coil/compose/) - An image loading library for Android backed by Kotlin Coroutines.
* [Shared Element Transition](https://developer.android.com/develop/ui/compose/animation/shared-elements) - Shared element transitions are a seamless way to transition between composables that have content that is consistent between them.
* [Material3 CenterAlignedTopAppBar](https://developer.android.com/develop/ui/compose/components/app-bars) - Provides access to key tasks and information. Generally hosts a title, core action items, and certain navigation items.
* [Material3 PullToRefresh](https://developer.android.com/reference/kotlin/androidx/compose/material3/pulltorefresh/package-summary.html) - Pull-to-refresh indicator in Matrial3

## Licence
    MIT License

    Copyright (c) 2024 Mohammadali Rezaei

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
