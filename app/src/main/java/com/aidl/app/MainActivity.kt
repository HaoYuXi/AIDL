package com.aidl.app

import androidx.appcompat.app.AppCompatActivity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.aidl.app.entity.Message
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var messageReceiveListener = object :MessageReceiveListener.Stub(){
        override fun onReceiveMessage(message: Message?) {


            Handler(Looper.getMainLooper()).post{
                Toast.makeText(this@MainActivity,message!!.content,Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_connection -> {

                connectionServiceProxy.connection()
                Log.i("TAG", "Connection")
            }
            R.id.tv_disconnect -> {
                connectionServiceProxy.disconnect()
                Log.i("TAG", "disconnect")
            }
            R.id.tv_isconnection -> {
                var isConnection = connectionServiceProxy.isConnection
                Toast.makeText(this, isConnection.toString(), Toast.LENGTH_SHORT).show()
            }

            R.id.tv_sendMessage -> {


                var message = Message("this is new Message")

                messageServiceProxy.sendMessage(message)

//                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
            }
            R.id.tv_register -> {
                messageServiceProxy.registerMessageReceiveListener(messageReceiveListener)
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
            }
            R.id.tv_unregister -> {
                messageServiceProxy.unRegisterMessageReceiveListener(messageReceiveListener)

                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
            }
        }

    }


    lateinit var connectionServiceProxy: IConnectionService
    lateinit var messageServiceProxy: IMessageService
    lateinit var serviceManagerProxy: IServiceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_connection.setOnClickListener(this)
        tv_disconnect.setOnClickListener(this)
        tv_isconnection.setOnClickListener(this)

        tv_sendMessage.setOnClickListener(this)
        tv_register.setOnClickListener(this)
        tv_unregister.setOnClickListener(this)


        val intent = Intent(this, RemoteService::class.java)
        bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {

                serviceManagerProxy = IServiceManager.Stub.asInterface(service)

                connectionServiceProxy = IConnectionService.Stub.asInterface(serviceManagerProxy.getService(IConnectionService::javaClass.name))
                messageServiceProxy = IMessageService.Stub.asInterface(serviceManagerProxy.getService(IMessageService::javaClass.name))

            }

            override fun onServiceDisconnected(name: ComponentName) {

            }
        }, Context.BIND_AUTO_CREATE)


    }


}
