package com.example.cooks_corner.repository;

import com.example.cooks_corner.entity.Role;
import com.example.cooks_corner.entity.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByAuthority(RoleEnum authority);
}
