package com.project.usersFoodCourt.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.usersFoodCourt.application.dto.request.UserAuthenticateRequestDto;
import com.project.usersFoodCourt.application.dto.request.UserRegisterRequestDto;
import com.project.usersFoodCourt.application.handler.IUserHandler;
import com.project.usersFoodCourt.domain.model.response.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IUserHandler userHandler;

    @InjectMocks
    private UserRestController userRestController;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private UserRegisterRequestDto registerRequest;
    private UserAuthenticateRequestDto authenticateRequest;
    private AuthenticationResponse authResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
        
        registerRequest = new UserRegisterRequestDto();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setDocumentNumber("12345678");
        registerRequest.setPhone("+573001234567");
        registerRequest.setBirthDate(LocalDate.now().minusYears(25));
        registerRequest.setEmail("test@test.com");
        registerRequest.setPassword("password");
        registerRequest.setRoleId(1L);

        authenticateRequest = new UserAuthenticateRequestDto();
        authenticateRequest.setEmail("test@test.com");
        authenticateRequest.setPassword("password");

        authResponse = new AuthenticationResponse();
        authResponse.setAccessToken("jwt-token");
    }

    @Test
    void registerUser_ShouldReturnAuthenticationResponse() throws Exception {
        when(userHandler.registerUserWithRole(any(UserRegisterRequestDto.class), any())).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authResponse)));
    }

    @Test
    void authenticate_ShouldReturnAuthenticationResponse() throws Exception {
        when(userHandler.authenticateUser(any(UserAuthenticateRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authResponse)));
    }
}