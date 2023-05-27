package com.enation.app.javashop.core.data;

import com.enation.app.javashop.framework.database.DaoSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据传输
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/7/5 14:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class DataTransfer {

//    @Autowired
//    @Qualifier("goodsDaoSupport")
//    private DaoSupport goodsDaoSupport;
//
//    @Autowired
//    @Qualifier("memberDaoSupport")
//    private DaoSupport memberDaoSupport;
//
//    @Autowired
//    @Qualifier("systemDaoSupport")
//    private DaoSupport systemDaoSupport;
//
//    @Autowired
//    @Qualifier("tradeDaoSupport")
//    private DaoSupport tradeDaoSupport;
//
//    @Autowired
//    @Qualifier("sssDaoSupport")
//    private DaoSupport sssDaoSupport;

    /**
     * 不同数据库相同表之间数据传输
     */
    @Test
    public void dataTransfer() {

//        // 获取数据库名称，由2向1传输数据
//        String dataBase1 = this.tradeDaoSupport.queryForString("select database()");
//        String dataBase2 = this.sssDaoSupport.queryForString("select database()");
//
//        // 传输数据的表，由2向1传输数据
//        String tableName1 = "es_exchange";
//        String tableName2 = "es_shop";
//
//        // 获取两个表的字段
//        List<Map<String, String>> list = this.tradeDaoSupport.queryForList("select COLUMN_NAME as name from information_schema.COLUMNS where table_name = ? and TABLE_SCHEMA= ? ", tableName1, dataBase1);
//        List<Map<String, String>> list2 = this.sssDaoSupport.queryForList("select COLUMN_NAME as name from information_schema.COLUMNS where table_name = ? and TABLE_SCHEMA= ?", tableName2, dataBase2);
//
//
//        String columns = "";
//        List<String> diff = new ArrayList<>();
//        List<String> diff2 = new ArrayList<>();
//
//        // 获取两个表的差异字段
//        for (Map map : list) {
//            if (!list2.contains(map)) {
//                diff.add(map.get("name").toString());
//            }
//        }
//
//        for (Map map2 : list2) {
//            if (!list.contains(map2)) {
//                diff2.add(map2.get("name").toString());
//            }
//        }

//        System.out.println(Arrays.toString(diff.toArray()));
//        System.out.println(Arrays.toString(diff2.toArray()));

        // 取两个表字段的交集
//        list.retainAll(list2);
//
//        // 拼接字段名
//        int i = 1;
//        for (Map map : list) {
//            if (i == list.size()) {
//                columns += map.get("name").toString();
//            } else {
//                columns += map.get("name").toString() + ",";
//            }
//            i++;
//        }

        // 将表2的数据插入表1
//        this.insertData(dataBase1, dataBase2, tableName1, tableName2, columns);

    }

    private void insertData(String dataBase1, String dataBase2, String tableName1, String tableName2, String columns) {

//        String sql = "delete from " + tableName1;
//
//        this.sssDaoSupport.execute(sql);
//
//        sql = "insert into " + dataBase1 + "." + tableName1 + " ( " + columns + " ) select " + columns + " from " + dataBase2 + "." + tableName2 + " ";
//
//        sql = "insert into " + dataBase1 + "." + tableName1 + " ( " + columns + ",seller_id,seller_name ) select " + columns + ",shop_id as seller_id,shop_name as seller_name from " + dataBase2 + "." + tableName2 + " ";
//
//        this.sssDaoSupport.execute(sql);

    }

//    String dataBaseName1 = "demo_trade";
//    String dataBaseName2 = "v70_trade";
//
//    String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//    String DB_URL_1 = "jdbc:mysql://192.168.2.5:3306/" + dataBaseName1;
//    String DB_URL_2 = "jdbc:mysql://localhost:3306/" + dataBaseName2;
//    String USER1 = "root";
//    String USER2 = "root";
//    String PASSWORD1 = "kingapex";
//    String PASSWORD2 = "752513";


    /**
     * 数据对比
     */
    @Test
    public void dataContrast() {

//        Connection conn1 = null;
//        Connection conn2 = null;
//        Statement stmt = null;
//        Statement stmt2 = null;
//        Statement stmt3 = null;
//        Statement stmt4 = null;
//        try {
//            // 注册 JDBC 驱动
//            Class.forName(JDBC_DRIVER);
//
//            // 打开链接
//            System.out.println("连接数据库...");
//            conn1 = DriverManager.getConnection(DB_URL_1, USER1, PASSWORD1);
//            conn2 = DriverManager.getConnection(DB_URL_2, USER2, PASSWORD2);
//
//            // 执行查询
//            System.out.println(" 实例化Statement对象...");
//            stmt = conn1.createStatement();
//            stmt2 = conn2.createStatement();
//            stmt3 = conn1.createStatement();
//            stmt4 = conn2.createStatement();
//
//            String sql1 = "select table_name from information_schema.tables where table_schema = '" + dataBaseName1 + "' and table_type = 'base table'";
//            String sql2 = "select table_name from information_schema.tables where table_schema = '" + dataBaseName2 + "' and table_type = 'base table'";
//
//            ResultSet rs = stmt.executeQuery(sql1);
//            ResultSet rs2 = stmt2.executeQuery(sql2);
//
//            List<String> tableList = new ArrayList<>();
//            List<String> tableList2 = new ArrayList<>();
//            // 展开结果集数据库，将所有表名放进list
//            while (rs.next()) {
//                tableList.add(rs.getString("table_name"));
//            }
//            while (rs2.next()) {
//                tableList2.add(rs2.getString("table_name"));
//            }
//
//            System.out.println("输出不同数据库的不同表");
//
//            // 获取两个数据库表的差异字段
//            for (String map : tableList) {
//                if (!tableList2.contains(map)) {
//                    System.out.println(map);
//                }
//            }
//
//            for (String map2 : tableList2) {
//                if (!tableList.contains(map2)) {
//                    System.out.println(map2);
//                }
//            }
//
//            System.out.println("=========================================================");
//
//            if (tableList.size() != tableList2.size()) {
//                System.out.println("表数量不同");
//                return;
//            }
//
//            System.out.println("输出不同数据库相同表的不同字段");
//
//            for (int i = 0; i < tableList.size(); i++) {
//                String tableName1 = tableList.get(i);
//                String sql3 = "select COLUMN_NAME from information_schema.COLUMNS where table_name = '" + tableName1 + "' and TABLE_SCHEMA = '" + dataBaseName1 + "'";
//                String tableName2 = tableList2.get(i);
//                String sql4 = "select COLUMN_NAME from information_schema.COLUMNS where table_name = '" + tableName2 + "' and TABLE_SCHEMA = '" + dataBaseName2 + "'";
//
//                if (!tableName1.equals(tableName2)) {
//                    System.out.println("表名对不上");
//                    return;
//                }
//
//                List<String> columnList1 = new ArrayList<>();
//                List<String> columnList2 = new ArrayList<>();
//                ResultSet rs3 = stmt3.executeQuery(sql3);
//                ResultSet rs4 = stmt4.executeQuery(sql4);
//                while (rs3.next()) {
////                    System.out.println(tableName1 + ":" + rs3.getString("COLUMN_NAME"));
//                    columnList1.add(rs3.getString("COLUMN_NAME"));
//                }
//                while (rs4.next()) {
//                    columnList2.add(rs4.getString("COLUMN_NAME"));
//                }
//
//                for (String map3 : columnList1) {
//                    if (!columnList2.contains(map3)) {
//                        System.out.println(tableName1 +"的字段"+ map3);
//                    }
//                }
//
//                for (String map4 : columnList2) {
//                    if (!columnList1.contains(map4)) {
//                        System.out.println(tableName2 +"的字段"+ map4);
//                    }
//                }
//                rs3.close();
//                rs4.close();
//
//            }
//
//            // 完成后关闭
//            rs.close();
//            rs2.close();
//            stmt.close();
//            stmt2.close();
//            stmt3.close();
//            stmt4.close();
//            conn1.close();
//            conn2.close();
//        } catch (SQLException se) {
//            // 处理 JDBC 错误
//            se.printStackTrace();
//        } catch (Exception e) {
//            // 处理 Class.forName 错误
//            e.printStackTrace();
//        } finally {
//            // 关闭资源
//            try {
//                if (stmt != null) {
//                    stmt.close();
//                }
//                if (stmt2 != null) {
//                    stmt2.close();
//                }
//            } catch (SQLException se2) {
//            }// 什么都不做
//            try {
//                if (conn1 != null) {
//                    conn1.close();
//                }
//                if (conn2 != null) {
//                    conn2.close();
//                }
//            } catch (SQLException se) {
//                se.printStackTrace();
//            }
//        }

    }

}
