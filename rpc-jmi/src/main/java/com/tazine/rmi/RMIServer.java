package com.tazine.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * RMI Server
 *
 * @author frank
 * @since 1.0.0
 */
public class RMIServer {

    public static void main(String[] args) throws Exception {

        // 创建服务
        HelloService helloService = new HelloServiceImpl();

        // 注册服务
        LocateRegistry.createRegistry(8801);
        Naming.bind("rmi://127.0.0.1:8801/helloService", helloService);

        System.out.println("RMIServer is in service...");
    }
}
