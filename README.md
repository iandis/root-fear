# RootFear

A simple package that depends on [RootBeer](https://github.com/scottyab/rootbeer) and [DetectMagiskHide](https://github.com/darvincisec/DetectMagiskHide) to check root indication on Android Devices.

# Setup
Before using `RootFear`, make sure to add these lines to the `<application>` tag on your `AndroidManifest.xml`
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example"
    xmlns:tools="http://schemas.android.com/tools">  <!-- don't forget to add this -->
    ...
    <application
        ...
        android:zygotePreloadName="app.iandis.rootfear.security.AppZygotePreload"
        tools:targetApi="q">
            <activity>
                ...
            </activity>
            <service
                android:name="app.iandis.rootfear.security.IsolatedService"
                android:exported="false"
                android:isolatedProcess="true"
                android:useAppZygote="true" />
    </application>
...
</manifest>
```

# Usage
```dart
void main() {
    final bool isRooted = await RootFear().isRooted;
    print('Any root indication: $isRooted');
}
```

# Notes
- Supports Android SDK 21+
- Current `RootBeer` version this package uses: **0.1.0**