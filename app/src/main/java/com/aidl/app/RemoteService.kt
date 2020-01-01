package com.aidl.app

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.RemoteException
import android.widget.Toast
import com.aidl.app.entity.Message
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class RemoteService : Service() {

    private var isConnection: Boolean = false

    private val handler = Handler(Looper.getMainLooper())

    lateinit var scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor
    var messageReceiveListenerArrayList: ArrayList<MessageReceiveListener> = ArrayList()

    lateinit var scheduledFuture: ScheduledFuture<Boolean>
    override fun onCreate() {
        super.onCreate()
        scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(5)
    }

    private val connectionService = object : IConnectionService.Stub() {
        @Throws(RemoteException::class)
        override fun connection() {
            try {
                Thread.sleep(5000)
                this@RemoteService.isConnection = true

                handler.post { Toast.makeText(this@RemoteService, "connection", Toast.LENGTH_SHORT).show() }


                scheduledFuture = scheduledThreadPoolExecutor.scheduleAtFixedRate({

                    for (messageReceiveListener in messageReceiveListenerArrayList) {
                        var message = Message("this message from remote")
                        messageReceiveListener.onReceiveMessage(message)

                    }


                }, 5000, 5000, TimeUnit.MILLISECONDS) as ScheduledFuture<Boolean>


            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }

        @Throws(RemoteException::class)
        override fun disconnect() {
            this@RemoteService.isConnection = false
            handler.post { Toast.makeText(this@RemoteService, "disconnect", Toast.LENGTH_LONG).show() }

            scheduledFuture.cancel(true)

        }

        @Throws(RemoteException::class)
        override fun isConnection(): Boolean {
            return isConnection
        }
    }


    var messageService = object : IMessageService.Stub() {
        override fun sendMessage(message: Message?) {


            handler.post {
                Toast.makeText(this@RemoteService, message!!.content, Toast.LENGTH_LONG).show()
            }

            message!!.isSendMessage = isConnection

        }

        override fun registerMessageReceiveListener(messageReceiveListener: MessageReceiveListener?) {
            if (messageReceiveListener != null) {

                messageReceiveListenerArrayList.add(messageReceiveListener!!)
            }
        }

        override fun unRegisterMessageReceiveListener(messageReceiveListener: MessageReceiveListener?) {
            if (messageReceiveListener != null) {

                messageReceiveListenerArrayList.remove(messageReceiveListener!!)
            }

        }

    }

    var serviceManager = object : IServiceManager.Stub() {
        override fun getService(serviceName: String?): IBinder? {

            if (IConnectionService::javaClass.name == serviceName) {

                return connectionService.asBinder()
            } else if (IMessageService::javaClass.name == serviceName) {

                return messageService.asBinder()
            } else {
                return null
            }
        }

    }

    override fun onBind(intent: Intent): IBinder? {
        return serviceManager.asBinder()
    }
}
