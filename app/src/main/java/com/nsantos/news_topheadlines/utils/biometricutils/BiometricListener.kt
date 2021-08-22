package com.nsantos.news_topheadlines.utils.biometricutils

import androidx.biometric.BiometricPrompt

interface BiometricListener {
    fun onBiometricAuthenticationSuccess(authenticationResult: BiometricPrompt.AuthenticationResult)
    fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String)
}