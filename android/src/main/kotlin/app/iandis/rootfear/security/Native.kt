package app.iandis.rootfear.security

internal object Native {
    val isMagiskPresentNative: Boolean
        external get

    init {
        System.loadLibrary("native-lib")
    }
}