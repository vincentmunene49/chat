package com.example.chatapplication.chatui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapplication.R


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChatScreen2(
    sendMessage: (String) -> Unit,
    onUserTyping: (String) -> Unit,
    message: Message,
) {
    val focusManager = LocalFocusManager.current
    val keyboardState by keyboardAsState()


    LaunchedEffect(keyboardState) {
        if (keyboardState == KeyboardState.Closed) {
            focusManager.clearFocus()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        MessageItem(message = message)
    }

    ChatInput(
        onUserTyping = onUserTyping,
        sendMessage = sendMessage
    )

}


@Composable
private fun MessageItem(
    message: Message,
    modifier: Modifier = Modifier
) {
    val style = getMessageStyle(message.isMe)

    Box(
        contentAlignment = style.alignment,
        modifier = modifier.fillMaxWidth()
    ) {
        Surface(
            color = style.containerColor,
            shape = style.shape
        ) {
            Column(modifier = modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                MessageBody(message)
            }
        }
    }
}

@Composable
private fun MessageBody(message: Message) {
    Text(text = message.message)
}


@Composable
private fun ChatInput(
    onUserTyping: (String) -> Unit,
    sendMessage: (String) -> Unit
) {

    val messageText by remember {
        mutableStateOf("")
    }



    Surface(color = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChatTextField(
                value = messageText,
                onValueChange = {
                //    setMessageText(it)
                    onUserTyping(it)
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
                    //setMessageText("")
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

@Composable
private fun getMessageStyle(isMine: Boolean): MessageStyle {
    return if (isMine) {
        MessageStyle(
            alignment = Alignment.CenterEnd,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
        )
    } else {
        MessageStyle(
            alignment = Alignment.CenterStart,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            shape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
        )
    }
}


@ExperimentalMaterial3Api
@Composable
fun DialogueOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.outlinedShape,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    contentPadding: PaddingValues = TextFieldDefaults.outlinedTextFieldPadding()
) {
    // If color is not provided via the text style, use content color as a default
//    val textColor = textStyle.color.takeOrElse {
//
//      //  colors.textColor(enabled).value
//    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = MaterialTheme.colorScheme.onPrimary))

    // CompositionLocalProvider(LocalTextSelectionColors provides colors.selectionColors) {
    @OptIn(ExperimentalMaterial3Api::class)
    (BasicTextField(
        value = value,
        modifier = if (label != null) {
            modifier.padding(top = OutlinedTextFieldTopPadding)
        } else {
            modifier
        }
            .background(MaterialTheme.colorScheme.primary, shape)
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth
            ),
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(Color.Red),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                contentPadding = contentPadding
            )
        }
    ))
}


internal val OutlinedTextFieldTopPadding = 8.dp

private data class MessageStyle(
    val alignment: Alignment,
    val containerColor: Color,
    val shape: Shape
)

@Preview
@Composable
fun PreviewChatUi() {
    //ChatScreen(sendMessage = {}, onUserTyping = {}, message = Message("Hello", true))
}