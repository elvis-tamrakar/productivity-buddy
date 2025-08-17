package com.example.productivity_app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ProductivityAppIntegrationTest {

    @Test
    void contextLoads() {
        // This test verifies that the Spring context loads successfully
        // with all beans properly configured
    }
}
