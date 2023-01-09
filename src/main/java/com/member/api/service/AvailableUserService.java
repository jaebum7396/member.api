package com.member.api.service;

import com.member.api.model.AvailableUserEntity;
import com.member.api.repository.AvailableUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AvailableUserService {
    @Autowired
    AvailableUserRepository availableUserRepository;
    public Optional<AvailableUserEntity> findByNameAndRegNo(String name, String regNo) {
        return availableUserRepository.findByNameAndRegNo(name, regNo);
    }
}