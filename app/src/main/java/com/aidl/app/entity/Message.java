package com.aidl.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Message  implements Parcelable
{
    private String content;
    private boolean isSendMessage;

    public Message(String content)
    {
        this.content = content;
    }

    protected Message(Parcel in)
    {
        content = in.readString();
        isSendMessage = in.readByte() != 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>()
    {
        @Override
        public Message createFromParcel(Parcel in)
        {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size)
        {
            return new Message[size];
        }
    };

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public boolean isSendMessage()
    {
        return isSendMessage;
    }

    public void setSendMessage(boolean sendMessage)
    {
        isSendMessage = sendMessage;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(content);
        dest.writeByte((byte) (isSendMessage ? 1 : 0));
    }
}
