package br.com.sailboat.todozy.feature.about.impl.presentation

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import br.com.sailboat.uicomponent.model.ImageTitleDividerUiModel
import br.com.sailboat.uicomponent.model.LabelValueUiModel
import br.com.sailboat.uicomponent.model.UiModel
import br.com.sailboat.uicomponent.impl.R as UiR

internal class GetAboutView(private val context: Context) : GetAboutViewUseCase {
    override suspend fun invoke(): Result<List<UiModel>> = runCatching {
        val items = ArrayList<UiModel>()
        items.add(getImageWithTitle())
        items.add(getAppDescription())
        items.add(getVersion())
        items.add(getDevelopedBy())

        return@runCatching items
    }

    private fun getImageWithTitle(): ImageTitleDividerUiModel {
        return ImageTitleDividerUiModel(
            imageRes = UiR.drawable.ic_todozy_128px,
            title = context.getString(UiR.string.app_full_name),
        )
    }

    private fun getAppDescription(): UiModel {
        return LabelValueUiModel(
            label = context.getString(UiR.string.label_description),
            value = context.getString(UiR.string.app_description),
        )
    }

    private fun getVersion(): LabelValueUiModel {
        return LabelValueUiModel(
            label = context.getString(UiR.string.version),
            value = context.appVersionName(),
        )
    }

    private fun getDevelopedBy(): LabelValueUiModel {
        return LabelValueUiModel(
            label = context.getString(UiR.string.developed_by),
            value = "Brayan Bedritchuk",
        )
    }
}

private fun Context.appVersionName(): String {
    val packageManager = packageManager
    val packageName = packageName
    val packageInfo =
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(packageName, 0)
            }
        }.getOrNull()
    val longVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo?.longVersionCode
    } else {
        @Suppress("DEPRECATION")
        packageInfo?.versionCode?.toLong()
    }
    return packageInfo?.versionName ?: longVersionCode?.toString().orEmpty()
}
