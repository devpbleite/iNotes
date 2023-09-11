package app.kotlin.devpbleite.inotes.model

import com.google.firebase.Timestamp

class Note {
    @JvmField
    var title: String? = null
    @JvmField
    var content: String? = null
    @JvmField
    var timestamp: Timestamp? = null
}