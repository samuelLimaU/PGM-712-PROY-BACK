package com.multimedia.spring.multimed.infraestructura.security;

import com.multimedia.spring.multimed.infraestructura.jpa.EntityRol;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityUsuarioRol;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataRolRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataUsuarioRolRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataUsuarioRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityUsuario;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SpringDataUsuarioRepository usuarioRepo;
    private final SpringDataUsuarioRolRepository usuarioRolRepo;
    private final SpringDataRolRepository rolRepo;

    public UserDetailsServiceImpl(
            SpringDataUsuarioRepository usuarioRepo,
            SpringDataUsuarioRolRepository usuarioRolRepo,
            SpringDataRolRepository rolRepo) {
        this.usuarioRepo = usuarioRepo;
        this.usuarioRolRepo = usuarioRolRepo;
        this.rolRepo = rolRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        EntityUsuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // Obtener roles del usuario
        List<Long> rolIds = usuarioRolRepo.findByUsuarioId(usuario.getId())
                .stream()
                .map(EntityUsuarioRol::getRolId)
                .toList();

        List<SimpleGrantedAuthority> authorities = rolRepo.findAllById(rolIds)
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getNombre().toUpperCase()))
                .toList();

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(authorities)
                .build();
    }
}