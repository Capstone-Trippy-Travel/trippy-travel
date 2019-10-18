package com.fer_mendoza.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApp.class)
@AutoConfigureMockMvc
public class UserIntegrationTests {

    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    @Autowired
    private MockMvc mvc;

    // Sanity Test, just to make sure the mvc bean is working
    @Test
    public void contextLoads() throws Exception {
        assertThat(mvc).isNotNull();
    }

    @Test
    public void testShowLoginPage() throws Exception {
        this.mvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void testShowRegisterPage() throws Exception {
        this.mvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterAUser() throws Exception {

        this.mvc.perform(post("/register")
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .param("username", "stacy")
                .param("email", "stacy@email.com")
                .param("password", "malibu")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

//        this.mvc.perform(post("/register")
//                .param("username", "stacy")
//                .param("email", "stacy@email.com")
//                .param("password", "malibu")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/"));
    }

//    @Test
//    public void testRegisterUserMismatchedPasswords() throws Exception{
//        this.mvc.perform(post("/register")
//                .param("username", "stacy")
//                .param("email", "stacy@email.com")
//                .param("password", "kitty")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(status().isFound())
//                .andExpect(redirectedUrl("/register?error"));
//    }

}