package com.sailfish.datasource.pool;

/**
 * 单例创建连接池
 * @author sailfish
 * @create 2017-08-12-下午10:45
 */
public class MyPoolConnectionFactory {

    public static class InnerPool{
        public static MyPool myPool = new MyDefaultPool();
    }

    public static MyPool getInstance(){
        return InnerPool.myPool;
    }
}
