package app.iandis.root_fear

import android.util.Log
import androidx.annotation.NonNull
import com.scottyab.rootbeer.RootBeer

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RootFearPlugin : FlutterPlugin, MethodCallHandler {

    companion object {
        private const val _channelName: String = "root-fear-plugin"
    }

    private lateinit var channel: MethodChannel

    private var _backgroundExecutor: ExecutorService? = null

    private var _rootBeer: RootBeer? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, _channelName)
        channel.setMethodCallHandler(this)
        _rootBeer = RootBeer(flutterPluginBinding.applicationContext)
        _backgroundExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        try {
            when (call.method) {
                "isRooted" -> _checkIsRooted { result.success(it) }
                else -> result.notImplemented()
            }
        } catch (e: Throwable) {
            Log.e(_channelName, e.message ?: "Unknown error.", e.cause)
            result.error("ROOT_FEAR_UNKNOWN_ERROR", e.message, e.cause)
        }

    }

    private fun _checkIsRooted(onResult: (Boolean) -> Unit) = _backgroundExecutor?.execute {
        val isRooted: Boolean = _rootBeer?.isRooted ?: false
        onResult(isRooted)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        _backgroundExecutor?.shutdown()
        _backgroundExecutor = null
        _rootBeer = null
    }
}
