package com.ch.ebusiness.config;

import com.ch.ebusiness.entity.User;
import com.ch.ebusiness.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 数据初始化器
 * 
 * 首次启动时：
 * 1. 创建 admin 用户（密码用 BCrypt 编码）
 * 2. 执行 SQL 脚本导入演示商品/分类/订单数据
 * 
 * 已有数据时自动跳过。如需重置，执行：
 *   mysql -u root -p ebusiness < init-data.sql
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    private final DataSource dataSource;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(DataSource dataSource, 
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.dataSource = dataSource;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // 检查是否已有数据（以 admin 用户是否存在为判断依据）
        Long adminCount = (Long) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM busertable WHERE bemail = 'admin@shop.com'")
                .getSingleResult();

        if (adminCount > 0) {
            System.out.println("检测到已有数据，跳过初始化。");
            return;
        }

        try {
            System.out.println("首次运行，初始化演示数据...");

            // 创建 root / admin123 管理员账号（使用正确的 BCrypt 编码）
            User admin = new User();
            admin.setEmail("admin@shop.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);

            // 创建几个测试普通用户
            String demoPwd = passwordEncoder.encode("123456");
            for (int i = 1; i <= 3; i++) {
                User u = new User();
                u.setEmail("user" + i + "@shop.com");
                u.setPassword(demoPwd);
                u.setRole("USER");
                userRepository.save(u);
            }

            // 执行 SQL 脚本导入商品/分类/订单数据
            try (Connection conn = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(conn, new ClassPathResource("db/init-data.sql"));
            }

            System.out.println("演示数据初始化完成！");
            System.out.println("管理员账号: admin@shop.com / admin123");
            System.out.println("测试账号: user1@shop.com / 123456");

        } catch (Exception e) {
            System.err.println("数据初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}