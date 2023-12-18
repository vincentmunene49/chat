package com.example.chatapplication

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import timber.log.Timber

object ChatConnection {
    //send message
    fun sendAndReceiveMessages(connection: XMPPTCPConnection, message: String) {
        val chatManager: ChatManager = ChatManager.getInstanceFor(connection)//this is the chat manager that takes the conncetion formed in the connection class to enable sending and receiving of messages
        val entityBareJid = JidCreate.entityBareFrom("usertest6@localhost")//this is the user that we are sending the message to
        val chat = chatManager.chatWith(entityBareJid)
        MessageSentList.messageSentList.add(message)
        chat.send(message)

        chatManager.addIncomingListener(ChatListener)
    }


}

object ChatListener : IncomingChatMessageListener {//this is a listener for incoming messages
    override fun newIncomingMessage(
        from: EntityBareJid?,
        message: Message?,
        chat: org.jivesoftware.smack.chat2.Chat?
    ) {
        Timber.tag("ChatListener").d("New message from $from: $message")

        message?.body?.let { MessagesList.messagesList.add(it) }
        Timber.tag("Messasges").d(MessagesList.messagesList.toString())

    }

}

object MessagesList {

    val messagesList: SnapshotStateList<String> = mutableStateListOf()
}

object MessageSentList {
    val messageSentList: SnapshotStateList<String> = mutableStateListOf()
}
