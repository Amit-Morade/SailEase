package com.example.sailease

import com.example.sailease.model.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class SignInViewModel @Inject constructor(
    private val accountService: AccountService
) : SailEaseAppViewModel()  {
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun onSignInClick(email: String, password: String) {
        launchCatching {
            accountService.signIn(email, password)

        }
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {

    }

    fun onSignOut() {
        launchCatching {
            accountService.signOut()

        }
    }
}