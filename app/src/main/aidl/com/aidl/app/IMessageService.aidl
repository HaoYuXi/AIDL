// IMessageService.aidl
package com.aidl.app;
import com.aidl.app.entity.Message;
import com.aidl.app.MessageReceiveListener;

// Declare any non-default types here with import statements

interface IMessageService {
    void sendMessage(in Message message);

    void registerMessageReceiveListener(MessageReceiveListener messageReceiveListener);

    void unRegisterMessageReceiveListener(MessageReceiveListener messageReceiveListener);



}
