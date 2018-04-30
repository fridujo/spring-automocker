package com.github.fridujo.sample.mvc;

import com.github.fridujo.automocker.base.Automocker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Automocker
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MvcApplication.class)
class MvcApplicationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void repository_is_available_and_consistent() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/create_user?firstName=Alyson&lastName=Hannigan"))
            .andExpect(MockMvcResultMatchers.status()
                .isOk());

        mvc.perform(MockMvcRequestBuilders.get("/create_user?firstName=Angelina&lastName=Jolie"))
            .andExpect(MockMvcResultMatchers.status()
                .isOk());

        mvc.perform(MockMvcRequestBuilders.get("/list_users"))
            .andExpect(MockMvcResultMatchers.status()
                .isOk())
            .andExpect(MockMvcResultMatchers.content()
                .string("[{\"firstName\":\"Alyson\",\"lastName\":\"Hannigan\"},{\"firstName\":\"Angelina\",\"lastName\":\"Jolie\"}]"));
    }
}

