package app.iandis.root_fear.security

internal object Native {
    val isMagiskPresentNative: Boolean
        external get

    init {
        System.loadLibrary("native-lib")
    }
}