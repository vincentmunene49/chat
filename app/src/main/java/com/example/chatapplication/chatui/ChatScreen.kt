package com.example.chatapplication.chatui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapplication.R

@Composable
fun ChatScreen(
    onClickSend: (String) -> Unit,
    messages: SnapshotStateList<String>? = null,
    sentMessage:SnapshotStateList<String>? = null
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        messages.let { messages ->
            messages?.forEach {
                Text(text = it)
            }

        }
        ChatInput(
            sendMessage = { message ->
                onClickSend(message)
            }
        )

        sentMessage.let { sentMessage ->
            sentMessage?.forEach {
                Text(text = it)
            }
        }
    }
}


@Composable
private fun ChatInput(
    modifier: Modifier = Modifier,
    sendMessage: (String) -> Unit,
) {

    var messageText by remember { mutableStateOf("") }
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChatTextField(
                value = messageText,
                onValueChange = { message ->
                    messageText = message
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .padding(start = 16.dp)
                    .weight(1f)
            )

            val isSendEnabled = messageText.isNotBlank()

            IconButton(
                onClick = {
                    sendMessage(messageText)
                    messageText = ""
                },
                enabled = isSendEnabled
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = stringResource(R.string.send),
                    tint = MaterialTheme.colorScheme.primary.copy(
                        alpha = if (isSendEnabled) 1f else 0.4f
                    )
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    DialogueOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
        maxLines = 4,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = modifier
    )
}
