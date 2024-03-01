package com.neosoft.crudoperation;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CrudOperationApplicationTests {

    @Before
    public void executedBeforeEach() {
        System.out.println("execute before every test");
    }

    @Test
    void contextLoads() {
        int n = 10;
        int num = n / 2;
        assertTrue(num == 5);
    }

}
