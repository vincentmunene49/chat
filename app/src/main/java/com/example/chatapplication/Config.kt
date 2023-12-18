package com.example.chatapplication

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.XMPPException.XMPPErrorException
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jxmpp.jid.parts.Localpart
import timber.log.Timber
import java.security.KeyStore
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class Config {
    //this is the configuration for the connection
    companion object {
        val config = XMPPTCPConnectionConfiguration.builder()
            .setPort(5222)
            .setHost("192.168.0.104")
            .setHostnameVerifier({ hostname, session -> true })
            .setXmppDomain("localhost")
            .setCustomX509TrustManager(CustomX509TrustManager())
            .build()
    }
}


object UserRegistrationAndLogin {
    fun registerAndLogin(
        connectionConfig: XMPPTCPConnectionConfiguration,
        password: String,
        jid: String,
        message: String
    ) {
        val scope = CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Timber.tag("UserRegistrationAndLogin").e(throwable)
        })

        val connection = XMPPTCPConnection(connectionConfig)
        val userName = Localpart.from(jid)

        scope.launch {
            try {
                connection.connect()//this enables connection to the server
                val accountManager = AccountManager.getInstance(connection)
                if (userName != null) {
                    accountManager.sensitiveOperationOverInsecureConnection(true)
                    accountManager.createAccount(userName, password)
                    connection.login(userName, password)
                    Timber.tag("UserRegistrationAndLogin").d("User created")
                } else {
                    Timber.tag("UserRegistrationAndLogin").d("UserName is null")

                }
            } catch (e: XMPPException) {
                connection.login(userName, password)
                e.printStackTrace()
            } catch (e: XMPPErrorException) {
                connection.login()
                e.printStackTrace()
            }
            ChatConnection.sendAndReceiveMessages(connection,message)//You need the connection to send messages

        }

    }
}
//leave this, it is just used to validate the serve certificate
class CustomX509TrustManager : X509TrustManager {
    private val defaultTrustManager: X509TrustManager

    init {
        // Initialize the default TrustManager
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        defaultTrustManager = trustManagerFactory.trustManagers[0] as X509TrustManager
    }

    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
        // Return an empty array or the default behavior
        return defaultTrustManager.acceptedIssuers
    }
}

