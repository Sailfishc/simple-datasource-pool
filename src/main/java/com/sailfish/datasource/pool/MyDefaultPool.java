package com.sailfish.datasource.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import javafx.scene.paint.Stop;

/**
 * 连接池的默认实现
 *
 * @author sailfish
 * @create 2017-08-12-下午9:48
 */
public class MyDefaultPool implements MyPool {

    /**
     * 功能： 1、存放、管理 2、支持多线程 3、有效复用，不是使用完就close，而是设置标识
     */

    //连接池的一些参数
    public static String URL;
    public static String USERNAME;
    public static String PASSWORD;
    public static int initCount;
    public static int step;
    public static int maxCount;

    //用来管理连接，并且使用vector用来保证线程安全
    public Vector<MyPoolConnection> connections = new Vector<>();


    //初始化连接池
    public MyDefaultPool() {

        init();
        try {
            Class.forName(DBConfigXML.DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        createConnection(initCount);
    }

    //实际调用的初始化方法
    private void init() {
        //加载
        URL = DBConfigXML.URL;
        USERNAME = DBConfigXML.USERNAME;
        PASSWORD = DBConfigXML.PASSWORD;
        initCount = DBConfigXML.initCount;
        step = DBConfigXML.step;
        maxCount = DBConfigXML.maxCount;
    }

    @Override
    public MyPoolConnection getConnection()  {
        if (connections.size() < 1) {
            throw new RuntimeException("连接初始化失败");
        }

        MyPoolConnection realConnection = null;

        try {
            realConnection = getRealConnection();
            if (realConnection == null) {
                createConnection(step);
                //然后在获取
                realConnection = getRealConnection();
                return realConnection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return realConnection;
    }


    private synchronized MyPoolConnection getRealConnection() throws SQLException {
        //从Vector中获取
        for (MyPoolConnection connection : connections) {
            if (connection == null) {
                return null;
            }
            if (!connection.isBusy()) {
                //不是繁忙状态
                if (connection.getConnection().isValid(3000)) {
                    connection.setBusy(true);
                    return connection;
                }
            } else {
                //是繁忙状态,则创建一个Connection
                //不能使用CreateConnection，没有返回
                // FIXME: 2017/8/12 可以改进，当前线程的Connection
//                createConnection(1);
                Connection connection1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                //这里设置有点不合理，这是替换掉了当前线程
                connection.setConnection(connection1);
                connection.setBusy(true);
                return connection;
            }
        }
        return null;
    }

    @Override
    public void createConnection(int count) {
        if (connections.size() >= maxCount) {
            throw new RuntimeException("连接池已满!");
        }

        if (connections.size() + count >= maxCount) {
            throw new RuntimeException("连接池已满!");
        }

        //可以创建
        for (int i = 0; i < count; i++) {
            try {
                Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                MyPoolConnection connection1 = new MyPoolConnection(connection, false);
                connections.add(connection1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
