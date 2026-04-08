package com.ubt.backend.repository;

import com.ubt.backend.entity.ContactInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactInquiryRepository extends JpaRepository<ContactInquiry, Long> {

    List<ContactInquiry> findAllByOrderBySubmittedAtDesc();

    List<ContactInquiry> findByStatusOrderBySubmittedAtDesc(String status);
}
