package com.tazine.netty;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 全局配置文件
 *
 * @author frank
 * @since 1.0.0
 */
public class NettyConfig {

    /**
     * 存储每一个客户端接入进来时的 Channel 对象
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

}
