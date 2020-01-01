// IServiceManager.aidl
package com.aidl.app;
import android.os.IBinder;

// Declare any non-default types here with import statements

interface IServiceManager {
    IBinder getService(String serviceName);
}
