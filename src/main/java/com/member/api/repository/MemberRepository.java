package com.member.api.repository;

import com.member.api.model.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity,String> {
    MemberEntity save(MemberEntity memberEntity);
    Optional<MemberEntity> findByUserId(String userId);
    Optional<MemberEntity> findByName(String name);
    List<MemberEntity> findAll();
}