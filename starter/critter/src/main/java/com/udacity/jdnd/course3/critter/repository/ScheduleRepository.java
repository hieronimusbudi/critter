package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.domain.employee.Employee;
import com.udacity.jdnd.course3.critter.domain.pet.Pet;
import com.udacity.jdnd.course3.critter.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    public List<Schedule> findAllSchedulesByPetsContaining(Pet pet);
    public List<Schedule> findAllSchedulesByEmployeesContaining(Employee employee);
}
