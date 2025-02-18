//package com.project.backend.config;
//
//import com.project.backend.infrastructure.entities.Pessoa;
//import com.project.backend.infrastructure.repositories.PessoaRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class PessoaUserDetailsService implements UserDetailsService {
//    private final PessoaRepository pessoaRepository;
//
//    /**
//     * This method would be an alternative to the UserDetailsService Bean in the SecurityConfig class
//     */
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Pessoa pessoa = pessoaRepository.findByCpf(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));
//        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(pessoa.getRole()));
//        return new User(pessoa.getCpf(), pessoa.getPassword(), authorities);
//    }
//}
