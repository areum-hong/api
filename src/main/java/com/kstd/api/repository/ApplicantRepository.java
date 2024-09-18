package com.kstd.api.repository;

import com.kstd.api.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    List<Applicant> findByLectureId(Long lectureId);
    List<Applicant> findByEmployeeNumber(String employeeNumber);
    boolean existsByLectureIdAndEmployeeNumber(Long lectureId, String employeeNumber);
}
