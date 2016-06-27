package com.fcuh.hive.client;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class HiveTest {

  public static void main( String[] args ) throws SQLException {
    try {
      Class.forName("org.apache.hive.jdbc.HiveDriver");
    }
    catch(ClassNotFoundException e) {
      e.printStackTrace();
    }
    Connection conn = DriverManager.getConnection("jdbc:hive2://hadoop2:10000/default", "root", "zeliang123");
    Statement stmt = conn.createStatement();
    String sql = "select * from wyp as t1 inner join wyp as t2 on t1.id=t2.id";
    System.out.println("Running: " + sql);
    ResultSet res = stmt.executeQuery(sql);
    ResultSetMetaData rsmd = res.getMetaData();
    int columnCount = rsmd.getColumnCount();
    for( int i = 1; i <= columnCount; i++ ) {
      System.out.println(rsmd.getColumnTypeName(i) + ":" + rsmd.getColumnName(i));
    }
    while(res.next()) {
      for( int i = 1; i <= columnCount; i++ ) {
        System.out.println(res.getString(i));
      }
    }
  }
}
