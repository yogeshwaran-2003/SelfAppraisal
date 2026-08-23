package com.yogesh.selfappraisal.repository;

import com.yogesh.selfappraisal.entity.Attendance;
import com.yogesh.selfappraisal.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeAndDate(Employee employee, LocalDate date);

    List<Attendance> findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);

    List<Attendance> findByMonthAndYear(Integer month, Integer year);
}