package com.pollra.test;

import com.pollra.web.test.TestController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestControllerTest {
    private MockMvc mockMvc;

    @MockBean
    TestController testController;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(testController).build();
    }

    @Test
    public void getTest() throws Exception{
//        https://blusky10.tistory.com/288
    }
}
