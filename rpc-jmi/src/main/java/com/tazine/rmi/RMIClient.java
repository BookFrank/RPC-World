package com.tazine.rmi;

import java.rmi.Naming;

/**
 * RMI Client
 *
 * @author frank
 * @since 1.0.0
 */
public class RMIClient {

    public static void main(String[] args) throws Exception {

        // 服务引入
        HelloService helloService = (HelloService) Naming.lookup("rmi://127.0.0.1:8801/helloService");

        // 服务调用
        System.out.println(helloService.sayHello("frank"));

    }

}
