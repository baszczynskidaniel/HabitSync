package org.example.project.authentication.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.authentication.domain.User
import org.example.project.authentication.domain.UserRepository
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.design_system.HSCenterSurface
import org.example.project.core.presentation.design_system.MediumSpacer
import org.example.project.core.presentation.design_system.SmallSpacer
import org.example.project.core.presentation.ui.LocalDimensions

@Composable
fun SignUpScreen(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
) {
    val localFocusManager = LocalFocusManager.current
    HSCenterSurface(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Text(

            text = "Sign in",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )
        MediumSpacer()
        OutlinedTextField(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
            value = state.username,
            label = {
                Text("username")
            },
            onValueChange = {
                onAction(SignUpAction.OnUsernameChange(it))
            },
            placeholder = {
                Text("username")
            },
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onNext = { localFocusManager.moveFocus(FocusDirection.Down) }
            )
        )
        MediumSpacer()
        OutlinedTextField(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
            value = state.password,
            label = {
                Text("password")
            },
            onValueChange = {
                onAction(SignUpAction.OnPasswordChange(it))
            },

            maxLines = 1,
            keyboardActions = KeyboardActions(
                onNext = { localFocusManager.moveFocus(FocusDirection.Down) }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )
        MediumSpacer()
        OutlinedTextField(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
            value = state.repeatedPassword,
            label = {
                Text("repeat password")
            },
            onValueChange = {
                onAction(SignUpAction.OnRepeatedPasswordChange(it))
            },

            maxLines = 1,
            keyboardActions = KeyboardActions(
                onNext = { localFocusManager.moveFocus(FocusDirection.Down) }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )
        MediumSpacer()
        Button(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
            onClick = {
                onAction(SignUpAction.OnSignUp)
            }
        ) {
            Text("Sign up")
        }
        SmallSpacer()
        Row(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Already a user? ")
            TextButton(onLogin) {
                Text("Log in")
            }
        }
    }
}

data class SignUpState(
    val username: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
)

sealed class SignUpAction {
    data class OnUsernameChange(val usernameChange: String): SignUpAction()
    data class OnPasswordChange(val passwordChange: String): SignUpAction()
    data class OnRepeatedPasswordChange(val repeatedPasswordChange: String): SignUpAction()
    data object OnSignUp: SignUpAction()
}

class SignUpViewModel(
    private val userRepository: UserRepository,
): ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state
        .onStart {  }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: SignUpAction) {
        when(action) {
            is SignUpAction.OnPasswordChange -> _state.update { it.copy(
                password = action.passwordChange
            )
            }
            is SignUpAction.OnRepeatedPasswordChange -> _state.update { it.copy(
                repeatedPassword = action.repeatedPasswordChange
            )
            }
            SignUpAction.OnSignUp -> {
                if(state.value.password == state.value.repeatedPassword) {
                    viewModelScope.launch {
                        userRepository.signUp(
                            User(
                                username = state.value.username,
                                password = state.value.password
                            )
                        ).onSuccess {
                            _state.value = SignUpState()
                        }
                    }
                }
            }
            is SignUpAction.OnUsernameChange -> {
                _state.update { it.copy(
                     username = action.usernameChange
                )
                }
            }
        }
    }
}