package com.example.chatapplication.chatui

import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapplication.R

@Composable
fun AuthScreen(
    onLoginClick: (String, String) -> Unit,
    readUserDetails: (context:Context) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val (jid, setJid) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    var jidHasError by remember { mutableStateOf(false) }
    var passwordHasError by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current.applicationContext

    Column(
        modifier = modifier
            .fillMaxSize()
            // Workaround for safeContentPadding not affecting horizontal paddings
            // for api lower that 32
            .padding(if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S_V2) 32.dp else 0.dp)
            // For layout to scroll when ime is displayed
            .safeContentPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(text = stringResource(R.string.login_title))

        InputField(
            value = jid,
            onValueChange = {
                setJid(it)
                jidHasError = false
            },
            labelRes = R.string.jabber_id,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            jidHasError = jidHasError,
            errorRes = R.string.error_jabber_id_not_valid,
            modifier = modifier.fillMaxWidth()
        )

        InputField(
            value = password,
            onValueChange = {
                setPassword(it)
                passwordHasError = false
            },
            labelRes = R.string.password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                VisibilityIcon(
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChange = { passwordVisible = it }
                )
            },
            jidHasError = passwordHasError,
            errorRes = R.string.error_password_not_valid,
            modifier = modifier.fillMaxWidth()
        )

        LoginButton(
            onClick = {
                passwordHasError = password.isEmpty()
                jidHasError = jid.isEmpty()
                if (!jidHasError && !passwordHasError) {
                    onLoginClick(jid, password)
                    readUserDetails(context)
                    navController.navigate("conversation")
                    focusManager.clearFocus()
                }
            },
            enabled = true,
        )
    }
}

@Composable
private fun GeneralError(errorMessage: String) {
    Text(text = errorMessage, color = Color.Red)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes labelRes: Int,
    jidHasError: Boolean,
    @StringRes errorRes: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = stringResource(labelRes)) },
            singleLine = true,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            isError = jidHasError,
            modifier = modifier.fillMaxWidth()
        )
        if (jidHasError) {
            Text(
                text = stringResource(errorRes),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun VisibilityIcon(
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit
) {
    val image = if (passwordVisible)
        Icons.Filled.KeyboardArrowUp
    else Icons.Filled.ArrowDropDown

    val descriptionResId = if (passwordVisible) R.string.hide_password else R.string.show_password

    IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
        Icon(
            imageVector = image,
            contentDescription = stringResource(descriptionResId)
        )
    }
}

@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.login).uppercase(),
                modifier = Modifier.align(Alignment.Center)
            )

        }
    }
}

@Preview
@Composable
fun PreviewAuthScreen() {
//    AuthScreen(
//        onLoginClick = { _, _ -> },
//        navigateToConversations = {},
//        readUserDetails = {},
//
//    )

}