// MessageReceiveListener.aidl
package com.aidl.app;
import com.aidl.app.entity.Message;
// Declare any non-default types here with import statements

interface MessageReceiveListener {

    void onReceiveMessage(in Message message);
}
