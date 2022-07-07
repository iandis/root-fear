package app.iandis.rootfear

import android.util.Log
import androidx.annotation.NonNull
import app.iandis.rootfear.security.MagiskDetectorService
import com.scottyab.rootbeer.RootBeer

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RootFearPlugin : FlutterPlugin, MethodCallHandler, CoroutineScope {

    companion object {
        private const val CHANNEL_NAME: String = "root-fear-plugin"
    }

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext get() = Dispatchers.IO + job

    private lateinit var channel: MethodChannel

    private var rootBeer: RootBeer? = null

    private var magiskDetectorService: MagiskDetectorService? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        job = Job()
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, CHANNEL_NAME)
        channel.setMethodCallHandler(this)
        rootBeer = RootBeer(flutterPluginBinding.applicationContext)
        magiskDetectorService = MagiskDetectorService().also {
            it.bind(flutterPluginBinding.applicationContext)
        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        try {
            when (call.method) {
                "isRooted" -> launch {
                    val isRooted: Boolean = checkIsRooted()
                    result.success(isRooted)
                }
                else -> result.notImplemented()
            }
        } catch (e: Throwable) {
            Log.e(CHANNEL_NAME, e.message ?: "Unknown error.", e.cause)
            result.error("ROOT_FEAR_UNKNOWN_ERROR", e.message, e.cause)
        }

    }

    private suspend fun checkIsRooted(): Boolean = withContext(Dispatchers.IO) {
        magiskDetectorService?.isMagiskPresent == true || rootBeer?.isRooted == true
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        job.cancel()
        magiskDetectorService?.unbind(binding.applicationContext)
        magiskDetectorService = null
        rootBeer = null
    }
}
