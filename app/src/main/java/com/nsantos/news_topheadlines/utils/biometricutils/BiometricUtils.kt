package com.nsantos.news_topheadlines.utils.biometricutils

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun canAuthenticateWithBiometrics(context: Context): Int {
    val biometricManager = BiometricManager.from(context)
    val authenticatorTypes = BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK
    return biometricManager.canAuthenticate(authenticatorTypes)
}

fun setBiometricPromptInfo(
    title: String,
    subtitle: String,
): BiometricPrompt.PromptInfo {
    val builder = BiometricPrompt.PromptInfo.Builder()
        .setTitle(title)
        .setSubtitle(subtitle)
        .setNegativeButtonText("Cancel")

    return builder.build()
}

fun promptBiometricAuthentication(fragment: Fragment, listener: BiometricListener): BiometricPrompt {

    val executor = ContextCompat.getMainExecutor(fragment.requireContext())
    val applicationContext = fragment.requireActivity().applicationContext

    return BiometricPrompt(fragment, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                listener.onBiometricAuthenticationError(errorCode, errString.toString())
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                listener.onBiometricAuthenticationSuccess(result)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(
                    applicationContext, "Authentication failed",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
}