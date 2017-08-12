package com.sailfish.datasource.pool;

/**
 * 用来模拟配置管理：用做连接池的一些基础配置
 * @author sailfish
 * @create 2017-08-12-下午9:55
 */
public class DBConfigXML {

    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/sailfish";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root";
    public static final int initCount = 10;
    public static final int step = 2;
    public static final int maxCount = 50;


}
