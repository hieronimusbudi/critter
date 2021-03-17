package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.domain.employee.Employee;
import com.udacity.jdnd.course3.critter.domain.pet.Pet;
import com.udacity.jdnd.course3.critter.domain.schedule.Schedule;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    public Schedule save(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules(){
        return scheduleRepository.findAll();
    }

    public List<Schedule> getSchedulesByPet(Pet pet){
        return scheduleRepository.findAllSchedulesByPetsContaining(pet);
    }

    public List<Schedule> getSchedulesByEmployee(Employee employee){
        return scheduleRepository.findAllSchedulesByEmployeesContaining(employee);
    }
}
