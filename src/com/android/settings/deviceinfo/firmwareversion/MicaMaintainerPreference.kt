package com.android.settings.deviceinfo.firmwareversion

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.SystemProperties
import androidx.preference.Preference
import com.android.settings.R
import com.android.settingslib.metadata.PreferenceAvailabilityProvider
import com.android.settingslib.metadata.PreferenceMetadata
import com.android.settingslib.metadata.PreferenceSummaryProvider
import com.android.settingslib.preference.PreferenceBinding

/**
 * Preference for displaying the maintainer of the ROM (if set) and linking to their Telegram group.
 */
class MicaMaintainerPreference :
    PreferenceMetadata,
    PreferenceAvailabilityProvider,
    PreferenceSummaryProvider,
    PreferenceBinding {

    companion object {
        // System property key where the maintainer's name is stored
        private const val ROM_PROPERTY = "ro.mica.maintainer"
        
        // URL to open when the preference is clicked
        private const val MAINTAINER_URL = "https://t.me/Generate_initramfs"
    }

    override val key: String
        get() = "mica_maintainer"

    override val title: Int
        get() = R.string.mica_maintainer

    // Checks if the property is set, making the preference available only when a maintainer is defined.
    override fun isAvailable(context: Context) =
        SystemProperties.get(ROM_PROPERTY, "").isNotEmpty()

    // Returns the name of the maintainer to be displayed as the summary.
    override fun getSummary(context: Context): String {
        val maintainer = SystemProperties.get(ROM_PROPERTY, "")
        return maintainer.ifEmpty {
            context.getString(R.string.device_info_default)
        }
    }

    // FIX: Added 'override' to correctly implement the intent from PreferenceMetadata.
    // This creates the Intent to open the external URL (Telegram link).
    override fun intent(context: Context): Intent =
        Intent(Intent.ACTION_VIEW, Uri.parse(MAINTAINER_URL))

    // Binds the metadata to the actual Preference object.
    override fun bind(preference: Preference, metadata: PreferenceMetadata) {
        // Call super to ensure all other metadata is bound
        super.bind(preference, metadata)

        // Enable copying the summary (maintainer name)
        preference.isCopyingEnabled = true

        // Ensure the intent is set explicitly for reliability (click opens the link)
        preference.intent = intent(preference.context)
    }
}
