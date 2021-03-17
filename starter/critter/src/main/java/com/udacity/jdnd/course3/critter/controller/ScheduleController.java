package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.domain.employee.Employee;
import com.udacity.jdnd.course3.critter.domain.pet.Pet;
import com.udacity.jdnd.course3.critter.domain.schedule.Schedule;
import com.udacity.jdnd.course3.critter.domain.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    PetService petService;

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertScheduleDTOToSchedule(scheduleDTO);
        Schedule savedSchedule = scheduleService.save(schedule);
        return convertScheduleToScheduleDTO(savedSchedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> scheduleList = scheduleService.getAllSchedules();
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        for (Schedule schedule : scheduleList) {
            scheduleDTOList.add(convertScheduleToScheduleDTO(schedule));
        }

        return scheduleDTOList;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        Pet pet = petService.findPet(petId);
        List<Schedule> scheduleList = scheduleService.getSchedulesByPet(pet);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        for (Schedule schedule : scheduleList) {
            scheduleDTOList.add(convertScheduleToScheduleDTO(schedule));
        }

        return scheduleDTOList;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.findEmployee(employeeId);
        List<Schedule> scheduleList = scheduleService.getSchedulesByEmployee(employee);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        for (Schedule schedule : scheduleList) {
            scheduleDTOList.add(convertScheduleToScheduleDTO(schedule));
        }

        return scheduleDTOList;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Pet> petList = customerService.findById(customerId).getPets();
        List<Schedule> scheduleList = new ArrayList<>();

        for (Pet pet : petList) {
            scheduleList.addAll(scheduleService.getSchedulesByPet(pet));
        }

        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        for (Schedule schedule : scheduleList) {
            scheduleDTOList.add(convertScheduleToScheduleDTO(schedule));
        }

        return scheduleDTOList;
    }

    private Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);

        List<Long> petIdList = scheduleDTO.getPetIds();
        if (petIdList != null) {
            List<Pet> pets = new ArrayList<Pet>();
            for (Long petId : petIdList) {
                pets.add(petService.findPet(petId));
            }
            schedule.setPets(pets);
        }

        List<Long> employeeIdList = scheduleDTO.getEmployeeIds();
        if (employeeIdList != null) {
            List<Employee> employees = new ArrayList<Employee>();
            for (Long employeeId : employeeIdList) {
                employees.add(employeeService.findEmployee(employeeId));
            }
            schedule.setEmployees(employees);
        }

        return schedule;
    }

    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        List<Pet> petList = schedule.getPets();
        if (petList != null) {
            List<Long> petIds = new ArrayList<Long>();
            for (Pet pet : petList) {
                petIds.add(pet.getId());
            }
            scheduleDTO.setPetIds(petIds);
        }

        List<Employee> employeeList = schedule.getEmployees();
        if (employeeList != null) {
            List<Long> employeeIds = new ArrayList<Long>();
            for (Employee employee : employeeList) {
                employeeIds.add(employee.getId());
            }
            scheduleDTO.setEmployeeIds(employeeIds);
        }

        return scheduleDTO;
    }
}
