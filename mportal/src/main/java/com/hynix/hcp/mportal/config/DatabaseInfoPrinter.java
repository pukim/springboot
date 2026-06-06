package com.hynix.hcp.mportal.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@Configuration
public class DatabaseInfoPrinter implements CommandLineRunner {

    private final DataSource dataSource;

    // 생성자 주입을 통해 Spring이 관리하는 DataSource를 가져옵니다.
    public DatabaseInfoPrinter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            System.out.println("==================================================");
            System.out.println("🌱 [DATABASE CONNECTION INFO] 🌱");
            System.out.println("--------------------------------------------------");
            System.out.println("✔ DB Product Name  : " + metaData.getDatabaseProductName());
            System.out.println("✔ DB Product Ver.  : " + metaData.getDatabaseProductVersion());
            System.out.println("✔ JDBC Driver Name : " + metaData.getDriverName());
            System.out.println("✔ Connection URL   : " + metaData.getURL());
            System.out.println("✔ Database User    : " + metaData.getUserName());
            System.out.println("==================================================");
        } catch (Exception e) {
            System.err.println("❌ 데이터베이스 연결 정보를 가져오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}