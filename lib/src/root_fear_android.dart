import 'dart:developer';

import 'package:flutter/services.dart';
import 'package:root_fear/src/root_fear.dart';

const RootFear rootFearInstance = _RootFearAndroid();

class _RootFearAndroid implements RootFear {
  const _RootFearAndroid();

  static const String _channelName = 'root-fear-plugin';

  static const MethodChannel _channel = MethodChannel(_channelName);

  @override
  Future<bool> get isRooted async {
    try {
      final bool? isRooted = await _channel.invokeMethod<bool>('isRooted');
      return isRooted ?? false;
    } catch (error, stackTrace) {
      log(
        'An error caught while checking root state',
        name: '_RootFearAndroid.isRooted',
        error: error,
        stackTrace: stackTrace,
      );
    }
    return false;
  }
}
