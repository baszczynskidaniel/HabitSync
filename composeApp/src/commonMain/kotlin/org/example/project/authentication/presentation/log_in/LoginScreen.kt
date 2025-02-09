package org.example.project.authentication.presentation.log_in

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
import org.example.project.core.data.datastore.MIUPreferencesDataSource
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.design_system.HSCenterSurface
import org.example.project.core.presentation.design_system.MediumSpacer
import org.example.project.core.presentation.design_system.SmallSpacer
import org.example.project.core.presentation.ui.LocalDimensions

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
    onSignUp: () -> Unit,
) {
    val localFocusManager = LocalFocusManager.current
    HSCenterSurface(
        modifier = modifier,
       verticalArrangement = Arrangement.Center
    ) {
        Text(

            text = "Log in",
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
                onAction(LoginAction.OnUsernameChange(it))
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
                Text("Password")
            },
            onValueChange = {
                onAction(LoginAction.OnPasswordChange(it))
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
                onAction(LoginAction.OnLogIn)
            }
        ) {
            Text("Log in")
        }
        SmallSpacer()
        Row(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("New user? ")
            TextButton(onSignUp) {
                Text("Sign up")
            }
        }
    }
}

data class LoginState(
    val username: String = "",
    val password: String = "",
)

fun LoginState.toUser() = User(username, password)

sealed class LoginAction {
    data class OnUsernameChange(val usernameChange: String): LoginAction()
    data class OnPasswordChange(val passwordChange: String): LoginAction()
    data object OnLogIn: LoginAction()
}

class LoginViewModel(
    private val userRepository: UserRepository,
    private val miuPreferencesDataSource: MIUPreferencesDataSource
): ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart {  }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: LoginAction) {
        when(action) {
            LoginAction.OnLogIn -> {
                viewModelScope.launch {
                    userRepository.login(state.value.toUser())
                        .onSuccess {
                            if(!it.isNullOrBlank()) {
                                miuPreferencesDataSource.setUserId(it)
                            }
                        }
                }
            }
            is LoginAction.OnPasswordChange -> _state.update { it.copy(
                password = action.passwordChange
            )
            }
            is LoginAction.OnUsernameChange ->  _state.update { it.copy(
                username = action.usernameChange
            )
            }
        }
    }
}