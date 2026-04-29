package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dtos.AddressDto;
import hr.algebra.fishingstore.dal.repos.AddressRepository;
import hr.algebra.fishingstore.model.entities.Address;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public AddressDto.ResponseDto getById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        return mapToResponseDto(address);
    }

    public List<AddressDto.ResponseDto> getAll() {
        return addressRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public AddressDto.ResponseDto create(AddressDto.CreateDto dto) {
        Address address = new Address();

        address.setStreet(dto.street());
        address.setCity(dto.city());
        address.setPostalCode(dto.postalCode());
        address.setCountry(dto.country());

        Address createdAddress = addressRepository.save(address);
        return mapToResponseDto(createdAddress);
    }
    public AddressDto.ResponseDto update(Long id,AddressDto.EditDto dto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setStreet(dto.street());
        address.setCity(dto.city());
        address.setPostalCode(dto.postalCode());
        address.setCountry(dto.country());

        Address createdAddress = addressRepository.save(address);
        return mapToResponseDto(createdAddress);
    }

    public boolean delete(Long id) {
        if(!addressRepository.existsById(id)) return false;

        addressRepository.deleteById(id);
        return true;
    }

    private AddressDto.ResponseDto mapToResponseDto(Address address) {
        return new AddressDto.ResponseDto(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getPostalCode(),
                address.getCountry()
        );
    }
}