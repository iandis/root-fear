package app.iandis.root_fear.security

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.system.Os
import io.flutter.Log
import java.io.*

internal class IsolatedService : Service() {
    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    private val mBinder: IIsolatedService.Stub = object : IIsolatedService.Stub() {
        /*Incase the java calls are hooked, there is 1 more level
                    of check in the native to detect if the same blacklisted paths are
                    found in the proc maps along with checks for su files when accessed
                    from native.Native functions can also be hooked.But requires some effort
                    if it is properly obfuscated and syscalls are used in place of libc calls
                     */
        //Log.d(TAG, "MountPath:"+ str);
        val isMagiskPresent: Boolean
            get() {
                Log.d(TAG, "Isolated UID:" + Os.getuid())
                var isMagiskPresent = false
                val file = File("/proc/self/mounts")
                try {
                    val fis = FileInputStream(file)
                    val reader = BufferedReader(InputStreamReader(fis))
                    var str: String
                    var count = 0
                    while (reader.readLine().also { str = it } != null && count == 0) {
                        //Log.d(TAG, "MountPath:"+ str);
                        for (path in blackListedMountPaths) {
                            if (str.contains(path)) {
                                Log.d(
                                    TAG,
                                    "Blacklisted Path found $path"
                                )
                                count++
                                break
                            }
                        }
                    }
                    reader.close()
                    fis.close()
                    Log.d(TAG, "Count of detected paths $count")
                    if (count > 0) {
                        Log.d(TAG, "Found magisk in atleast 1 mount path ")
                        isMagiskPresent = true
                    } else {
                        /*Incase the java calls are hooked, there is 1 more level
                    of check in the native to detect if the same blacklisted paths are
                    found in the proc maps along with checks for su files when accessed
                    from native.Native functions can also be hooked.But requires some effort
                    if it is properly obfuscated and syscalls are used in place of libc calls
                     */
                        isMagiskPresent = Native.isMagiskPresentNative
                        Log.d(
                            TAG,
                            "Found Magisk in Native $isMagiskPresent"
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return isMagiskPresent
            }
    }

    companion object {
        private val blackListedMountPaths = arrayOf("magisk", "core/mirror", "core/img")
        private const val TAG = "DetectMagisk-Isolated"
    }
}
