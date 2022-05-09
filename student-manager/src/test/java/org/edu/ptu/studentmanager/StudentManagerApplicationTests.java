package org.edu.ptu.studentmanager;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan(basePackages = "org.edu.ptu.studentmanager.mapper")

class StudentManagerApplicationTests {

    @Test
    void contextLoads() {
    }

}
