import 'package:flutter_test/flutter_test.dart';
import 'package:live_activity_plugin/live_activity_plugin.dart';
import 'package:live_activity_plugin/live_activity_plugin_platform_interface.dart';
import 'package:live_activity_plugin/live_activity_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockLiveActivityPluginPlatform
    with MockPlatformInterfaceMixin
    implements LiveActivityPluginPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final LiveActivityPluginPlatform initialPlatform = LiveActivityPluginPlatform.instance;

  test('$MethodChannelLiveActivityPlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelLiveActivityPlugin>());
  });

  test('getPlatformVersion', () async {
    LiveActivityPlugin liveActivityPlugin = LiveActivityPlugin();
    MockLiveActivityPluginPlatform fakePlatform = MockLiveActivityPluginPlatform();
    LiveActivityPluginPlatform.instance = fakePlatform;

    expect(await liveActivityPlugin.getPlatformVersion(), '42');
  });
}
