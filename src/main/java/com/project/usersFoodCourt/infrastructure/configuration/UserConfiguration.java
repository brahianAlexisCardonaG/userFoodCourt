package com.project.usersFoodCourt.infrastructure.configuration;

import com.project.usersFoodCourt.domain.api.IUserServicePort;
import com.project.usersFoodCourt.domain.model.util.DomainUserDetails;
import com.project.usersFoodCourt.domain.spi.IRolePersistencePort;
import com.project.usersFoodCourt.domain.spi.IUserPersistencePort;
import com.project.usersFoodCourt.domain.usecase.UserUseCase;
import com.project.usersFoodCourt.infrastructure.out.jpa.adapter.RoleJpaAdapter;
import com.project.usersFoodCourt.infrastructure.out.jpa.adapter.UserJpaAdapter;
import com.project.usersFoodCourt.infrastructure.out.jpa.mapper.IRoleEntityMapper;
import com.project.usersFoodCourt.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.project.usersFoodCourt.infrastructure.out.jpa.repository.IRoleRepository;
import com.project.usersFoodCourt.infrastructure.out.jpa.repository.IUserRepository;
import com.project.usersFoodCourt.utils.GenericValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
@RequiredArgsConstructor
public class UserConfiguration {
    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    private final IRoleEntityMapper roleEntityMapper;
    private final IRoleRepository roleRepository;


    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserJpaAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public IRolePersistencePort rolePersistencePort() {
        return new RoleJpaAdapter(roleRepository, roleEntityMapper);
    }

    @Bean
    public IUserServicePort userServicePort(
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            GenericValidation genericValidation
    ) {
        return new UserUseCase(passwordEncoder,
                userPersistencePort(),
                jwtService,
                authenticationManager,
                rolePersistencePort(),
                genericValidation
                );
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .map(userEntity -> new DomainUserDetails(userEntityMapper.toUserModel(userEntity)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return new SecurityContextLogoutHandler();
    }
}
