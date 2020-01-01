// IConnectionService.aidl
package com.aidl.app;

// Declare any non-default types here with import statements

interface IConnectionService {

    oneway void connection();

    void disconnect();

    boolean isConnection();
}
