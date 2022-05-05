# Aries Android SDK

This framework is a modern Kotlin implementation of the Aries protocols and definitions. It's purpose is to provide a universal library for building SSI applications on the Android platform, especially for mobile devices.

The overall structure is service based and designed following the [.NET implementation](https://github.com/hyperledger/aries-framework-dotnet). Each service implements one specific part of the Aries protocol and is provided with a ready-to-use default implementation. Wallets, records and messages make up the foundation for all other functionalities.

## Features

At this point, the implementation is still in development state and not fully tested.

The following features are supported:

- Managing wallets and records.
- Sending and receiving messages over HTTP including (un-)packing.
- Negotiating and receiving credentials.
- Connecting to a pool and reading data from a ledger.
- Different kinds of message decorators.

Missing features are:

- Payment services

## Libraries

In order to use the Aries Android SDK in your project, there are additional libraries you have to add to your project, which are not included in this repository:

### Android

The following sdk must be located in the app/libs folder of your project:

- Mediator Android SDK

The following libraries must be located in your project in the app/src/main/jniLibs folder:

Get it from https://developer.android.com/ndk/downloads:
- src/libs-android/arm64-v8a/libc++_shared.so
- src/libs-android/armeabi-v7a/libc++_shared.so
- src/libs-android/x86/libc++_shared.so
- src/libs-android/x86_64/libc++_shared.so

Get it from: https://repo.sovrin.org/android/libindy/stable:
- src/libs-android/arm64-v8a/libindy.so
- src/libs-android/armeabi-v7a/libindy.so
- src/libs-android/x86/libindy.so
- src/libs-android/x86_64/libindy.so

Get the jar from : https://github.com/java-native-access/jna/tree/master/lib/native and extract the:
- libjnidispatch.so
