package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.domain.customer.Customer;
import com.udacity.jdnd.course3.critter.domain.pet.Pet;
import com.udacity.jdnd.course3.critter.domain.pet.PetDTO;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;

    @Autowired
    CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Customer customer = null;

        if((Long) petDTO.getOwnerId() != null) {
            customer = customerService.findById(petDTO.getOwnerId());
        }

        Pet pet = convertPetDTOToPet(petDTO);
        pet.setCustomer(customer);
        Pet savedPet = petService.save(pet);

        if(customer != null) {
            customer.addPet(savedPet);
        }

        return convertPetToPetDTO(savedPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.findPet(petId);
        if(pet != null){
            return convertPetToPetDTO(pet);
        }

        return null;
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> petList = petService.getAllPets();
        List<PetDTO> petDTOList = new ArrayList<>();

        for (Pet pet: petList){
            petDTOList.add(convertPetToPetDTO(pet));
        }

        return petDTOList;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> petList = petService.findPetsByOwner(ownerId);
        List<PetDTO> petDTOList = new ArrayList<>();

        for (Pet pet: petList){
            petDTOList.add(convertPetToPetDTO(pet));
        }

        return petDTOList;
    }

    private PetDTO convertPetToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getCustomer() != null) {
            petDTO.setOwnerId(pet.getCustomer().getId());
        }

        return petDTO;
    }

    private Pet convertPetDTOToPet(PetDTO petDTO){
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        return pet;
    }
}
