package com.sailfish.datasource.pool;

import javax.sql.DataSource;

/**
 * 定义对于Pool的操作
 * @author sailfish
 * @create 2017-08-12-下午9:44
 */
public interface MyPool {

    //获取连接：需要dataSource，要做到复用，需要给一个标识
    MyPoolConnection getConnection();

    //创建连接

    /**
     * 创建几个连接
     * @param count
     */
    void createConnection(int count);
}
