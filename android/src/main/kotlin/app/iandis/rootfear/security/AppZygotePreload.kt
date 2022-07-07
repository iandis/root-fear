package app.iandis.rootfear.security

import android.app.ZygotePreload
import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.Q)
class AppZygotePreload : ZygotePreload {
    override fun doPreload(p0: ApplicationInfo) {
        System.loadLibrary("native-lib")
    }
}