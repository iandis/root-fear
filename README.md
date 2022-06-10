# RootFear

A simple package that depends on [RootBeer](https://github.com/scottyab/rootbeer) to check root indication on Android Devices.

# Usage
```dart
void main() {
    final bool isRooted = await RootFear().isRooted;
    print('Any root indication: $isRooted');
}
```

# Notes
Current `RootBeer` version this package uses: **0.1.0**