package com.example.prueba1.ui.biometrics

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
class  BiometricPromptManager(
    private val activity : AppCompatActivity
) {
    // In order to return a result to the UI, we use a channel
    // We receive and send BiometricResult objects
    private val resultChannel = Channel<BiometricResult>()
    // We listen to the result and we observe it
    // An attribute that can be accessed from external classes
    val promptResults = resultChannel.receiveAsFlow()
    fun showBiometricPrompt( //Configure Biometric Prompt
        title: String,
        description: String
    ) {
        //Set an activity as the context of the biometric manager
        val manager = BiometricManager.from(activity)
        //Authenticators are ways how we can authenticate user with the biometric prompt
        // by using 'or', we can set multiple ways the user can authenticate itself
        //val authenticators = BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        val authenticators = if (Build.VERSION.SDK_INT >= 30) {
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        } else BIOMETRIC_STRONG
        // Building the prompt
        val promptInfo = PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setAllowedAuthenticators(authenticators)
        //True -> Gives the biometric manager a hint that users can confirm they want to authenticate
        // meaning the user needs to click something to begin the scan
        //False -> the biometric evaluation begins immediately
        //.setConfirmationRequired(false)
        if (Build.VERSION.SDK_INT < 30) {
            promptInfo.setNegativeButtonText("Cancel")
        }
        when (manager.canAuthenticate(authenticators)) {
            // We bind the received options to the ones we've created with the sealed class
            // Here we send the results
            //Hardware unavailable
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                resultChannel.trySend(BiometricResult.HardwareUnavailable) //We send the event
                return //To ask the user to try again, it does not continue here
            }
            // Feature unavailable, there's no hardware that can support the feature
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                resultChannel.trySend(BiometricResult.FeatureUnavailable)
                return
            }
            // Biometrics haven't been set
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                resultChannel.trySend(BiometricResult.AuthenticationNotSet)
                return
            }
            //When security update is available
            else -> Unit
        }
        //The actual prompt
        val prompt = BiometricPrompt(
            activity,
            object : BiometricPrompt.AuthenticationCallback() {
                // Something failed with the biometricAuth-mechanisim itself
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // The only option that sends a string
                    resultChannel.trySend(BiometricResult.AuthenticationError(errString.toString()))
                }
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    resultChannel.trySend(BiometricResult.AuthenticationSuccess)
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    resultChannel.trySend(BiometricResult.AuthenticationFailed)
                }
            }
        )
        // Perform the autentication
        prompt.authenticate(promptInfo.build())
    }
    sealed interface BiometricResult{ // Posible results
        //In case the evaluation can be perfomed, but hardware is busy
        data object HardwareUnavailable : BiometricResult
        //Biometric evaluation isn't avaialable in the device
        data object FeatureUnavailable : BiometricResult
        //Failed not beacause the users fault
        data class AuthenticationError(val error: String): BiometricResult
        //The wrong face or the wrong finger
        data object AuthenticationFailed : BiometricResult
        // Correctly recognized
        data object AuthenticationSuccess : BiometricResult
        //In case the user doesn't have set up authentication
        data object AuthenticationNotSet : BiometricResult
    }
}