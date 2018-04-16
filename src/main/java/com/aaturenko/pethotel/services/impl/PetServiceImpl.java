package com.aaturenko.pethotel.services.impl;

import com.aaturenko.pethotel.exceptions.EntityNotFoundException;
import com.aaturenko.pethotel.exceptions.UniqueNameException;
import com.aaturenko.pethotel.models.Pet;
import com.aaturenko.pethotel.repositories.PetRepository;
import com.aaturenko.pethotel.services.PetService;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    @Override
    public Pet saveOrUpdatePet(Pet pet) {
        try {
            return petRepository.save(pet);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            String passport = pet.getPassport();
            if (petRepository.findByPassport(passport).isPresent())
                throw new UniqueNameException(
                        EntityNotFoundException.Entity.Pet,
                        UniqueNameException.UniqueName.Email,
                        passport);
            throw e;
        }
    }

    @Override
    public Pet findPetById(long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.Entity.Pet, id));
    }

    @Override
    public List<Pet> findPetByOwnerId(long ownerId) {
        return petRepository.findByOwner_Id(ownerId);
    }

    @Override
    public void deletePetById(long id) {
        petRepository.deleteById(id);
    }
}
