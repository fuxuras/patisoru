package com.fuxuras.patisoru.configuration;

import com.fuxuras.patisoru.entities.Role;
import com.fuxuras.patisoru.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Veritabanında yoksa, varsayılan rolleri ekle
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role("ROLE_USER"));
        }
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
        // Kodun diğer yerlerinde gördüğüm ve ileride ihtiyacımız olacak diğer roller
        if (roleRepository.findByName("ROLE_VET").isEmpty()) {
            roleRepository.save(new Role("ROLE_VET"));
        }
        if (roleRepository.findByName("ROLE_VERIFIED").isEmpty()) {
            roleRepository.save(new Role("ROLE_VERIFIED"));
        }
    }
}
