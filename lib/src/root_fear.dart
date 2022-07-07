import 'dart:io';

import 'package:root_fear/src/root_fear_android.dart';

abstract class RootFear {
  /// Returns a singleton instance of [RootFear]
  factory RootFear() {
    if (Platform.isAndroid) {
      return rootFearInstance;
    }

    throw UnsupportedError('Unsupported platform: ${Platform.operatingSystem}');
  }

  Future<bool> get isRooted;
}
