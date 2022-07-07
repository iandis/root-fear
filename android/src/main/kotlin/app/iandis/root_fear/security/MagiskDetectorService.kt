package app.iandis.root_fear.security

import android.content.ComponentName
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import io.flutter.Log

class MagiskDetectorService : ServiceConnection {

    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private var isolatedService: IIsolatedService? = null

    private var isServiceBound: Boolean = false

    val isMagiskPresent: Boolean
        get() {
            if (!isServiceBound) return false
            return try {
                isolatedService.isMagiskPresent()
            } catch (t: Throwable) {
                false
            }
        }

    fun bind(context: Context) {
        val intent: Intent = Intent(
            context,
            IsolatedService::class.java
        )
        context.bindService(
            intent,
            this,
            BIND_AUTO_CREATE
        )
    }

    fun unbind(context: Context) {
        context.unbindService(this)
    }

    override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
        isolatedService = IIsolatedService.Stub.asInterface(iBinder)
        isServiceBound = true
        Log.d(TAG, "Service bound")
    }

    override fun onServiceDisconnected(componentName: ComponentName) {
        isServiceBound = false
        Log.d(TAG, "Service unbound")
    }

}