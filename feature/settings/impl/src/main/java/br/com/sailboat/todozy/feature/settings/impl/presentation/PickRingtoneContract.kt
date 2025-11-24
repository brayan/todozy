package br.com.sailboat.todozy.feature.settings.impl.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import br.com.sailboat.uicomponent.impl.R as UiR

internal class PickRingtoneContract : ActivityResultContract<Uri, Uri?>() {
    override fun createIntent(
        context: Context,
        alarmSoundUri: Uri,
    ) = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
        putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
        putExtra(
            RingtoneManager.EXTRA_RINGTONE_TITLE,
            context.getString(UiR.string.notification_tone),
        )
        putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, alarmSoundUri)
    }

    override fun parseResult(
        resultCode: Int,
        result: Intent?,
    ): Uri? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return result?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
    }
}
