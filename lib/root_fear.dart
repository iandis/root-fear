
import 'dart:async';

import 'package:flutter/services.dart';

class RootFear {
  static const MethodChannel _channel = MethodChannel('root_fear');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
