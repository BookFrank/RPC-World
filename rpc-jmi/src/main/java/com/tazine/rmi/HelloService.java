package com.tazine.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * HelloService Interface
 *
 * @author frank
 * @since 1.0.0
 */
public interface HelloService extends Remote {
    /**
     * RMI 接口方法定义必须显式声明抛出 RemoteException
     *
     * @param name
     * @return
     * @throws RemoteException
     */
    String sayHello(String name) throws RemoteException;
}
