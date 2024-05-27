import io.flutter.app.FlutterApplication

class Application : FlutterApplication(),
    io.flutter.plugin.common.PluginRegistry.PluginRegistrantCallback {
    // ...
    override fun onCreate() {
        super.onCreate()
        //FlutterFirebaseMessagingBackgroundService.setPluginRegistrant(this)
    }

    override fun registerWith(registry: io.flutter.plugin.common.PluginRegistry) {
        //GeneratedPluginRegistrant.registerWith(registry)
    }
}