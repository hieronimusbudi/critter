package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.domain.customer.Customer;
import com.udacity.jdnd.course3.critter.domain.customer.CustomerDTO;
import com.udacity.jdnd.course3.critter.domain.employee.Employee;
import com.udacity.jdnd.course3.critter.domain.employee.EmployeeDTO;
import com.udacity.jdnd.course3.critter.domain.employee.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.domain.pet.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        List<Long> petIdList = customerDTO.getPetIds();
        List<Pet> petList = new ArrayList<>();

        if(petIdList != null){
            for (Long petId: petIdList){
                petList.add(petService.findPet(petId));
            }
        }

        Customer customer = convertCustomerDTOToCustomer(customerDTO);
        customer.setPets(petList);

        Customer savedCustomer = customerService.save(customer);
        return convertCustomerToCustomerDTO(savedCustomer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customerList = customerService.findAll();
        List<CustomerDTO> customerDTOList = new ArrayList<>();

        for (Customer customer: customerList) {
            customerDTOList.add(convertCustomerToCustomerDTO(customer));
        }

        return customerDTOList;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Pet pet = petService.findPet(petId);
        Customer customer = pet.getCustomer();

        if(customer != null){
            return convertCustomerToCustomerDTO(customer);
        }

        return null;
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertEmployeeDTOToEmployee(employeeDTO);
        employee = employeeService.save(employee);

        return convertEmployeeToEmployeeDTO(employee);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.findEmployee(employeeId);
        return convertEmployeeToEmployeeDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setDaysAvailable(daysAvailable,employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employeeList = employeeService.findEmployeesForService(
                employeeDTO.getDate(),
                employeeDTO.getSkills()
        );
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        for (Employee employee: employeeList){
            employeeDTOList.add(convertEmployeeToEmployeeDTO(employee));
        }

        return employeeDTOList;
    }

    private CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        List<Pet> petList = customer.getPets();

        if (petList != null) {
            List<Long> petIds = new ArrayList<>();

            for (Pet pet : petList) {
                petIds.add(pet.getId());
            }

            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }

    private Customer convertCustomerDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        List<Long> petIdList = customerDTO.getPetIds();

        if (petIdList != null) {
            List<Pet> pets = new ArrayList<Pet>();

            for (Long petId : petIdList) {
                pets.add(petService.findPet(petId));
            }

            customer.setPets(pets);
        }

        return customer;
    }

    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    private Employee convertEmployeeDTOToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }
}
