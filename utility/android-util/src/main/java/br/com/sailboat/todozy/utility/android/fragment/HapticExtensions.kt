package br.com.sailboat.todozy.utility.android.fragment

import android.view.HapticFeedbackConstants
import androidx.fragment.app.Fragment

fun Fragment.playHaptic(feedback: Int = HapticFeedbackConstants.KEYBOARD_TAP) {
    view?.performHapticFeedback(feedback)
        ?: activity?.window?.decorView?.performHapticFeedback(feedback)
}

fun Fragment.hapticHandled(
    feedback: Int = HapticFeedbackConstants.KEYBOARD_TAP,
    action: () -> Unit,
): Boolean {
    playHaptic(feedback)
    action()
    return true
}
