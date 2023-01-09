package com.member.api.repository;

import com.member.api.model.AvailableUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AvailableUserRepository extends JpaRepository<AvailableUserEntity,Long> {
    Optional<AvailableUserEntity> findByNameAndRegNo(String name, String regNo);
}