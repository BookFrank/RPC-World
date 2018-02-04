package com.tazine.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * HelloServiceImpl
 *
 * @author frank
 * @since 1.0.0
 */
public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {

    /**
     * 服务端方法实现类必须继承 UnicastRemoteObject 类，该类定义了服务调用方与服务提供方对象实例，
     * 并建立一对一连接。
     */


    public HelloServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String sayHello(String name) throws RemoteException {
        return "Hello " + name;
    }
}
