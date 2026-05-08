package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.AddressDto;
import hr.algebra.fishingstore.dal.repos.AddressRepository;
import hr.algebra.fishingstore.model.entities.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressDto.ResponseDto getById(Long id) {
        return mapToResponseDto(addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found")));
    }

    public List<AddressDto.ResponseDto> getAll() {
        return addressRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public AddressDto.ResponseDto create(AddressDto.CreateDto dto) {
        Address address = new Address();

        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());

        return mapToResponseDto(addressRepository.save(address));
    }

    public AddressDto.ResponseDto update(Long id, AddressDto.EditDto dto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());

        return mapToResponseDto(addressRepository.save(address));
    }

    public boolean delete(Long id) {
        if (!addressRepository.existsById(id)) return false;

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