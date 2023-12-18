package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.ChatConnection
import com.example.chatapplication.Config
import com.example.chatapplication.MessageSentList
import com.example.chatapplication.MessagesList
import com.example.chatapplication.UserRegistrationAndLogin
import com.example.chatapplication.chatui.AuthScreen
import com.example.chatapplication.chatui.ChatScreen
import com.example.chatapplication.chatui.Message
import com.example.chatapplication.readUserDetails
import com.example.chatapplication.saveUserDetails
import com.example.ui.theme.ChatApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  UserRegistrationAndLogin.registerAndLogin(Config.config)

        setContent {

            ChatApplicationTheme {
//                var messageList by remember { mutableStateListOf(
//                    MessagesList.messagesList
//                ) }
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable(route = "login") {
                        AuthScreen(
                            navController = navController,
                            readUserDetails = {

                            },
                            onLoginClick = { jid, password ->
                                lifecycleScope.launch(Dispatchers.IO) {
                                    saveUserDetails(applicationContext, jid, password)

                                }

                            })

                    }

                    composable(route = "conversation") {
                        ChatScreen(
                            onClickSend = { message ->
                                lifecycleScope.launch(Dispatchers.IO) {
                                    readUserDetails(applicationContext).collect {
                                        UserRegistrationAndLogin.registerAndLogin(
                                            connectionConfig = Config.config,
                                            password = it.second,
                                            jid = it.first,
                                            message = message
                                        )
                                    }
                                }

                            },
                            messages = MessagesList.messagesList,
                            sentMessage = MessageSentList.messageSentList
                        )

                    }
                }


            }
        }
    }
}



