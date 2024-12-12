package com.example.mockito.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    MemberEntity findByName(String name);
    MemberEntity findByEmail(String email);

    @Transactional
    void deleteById(Long id);
}
