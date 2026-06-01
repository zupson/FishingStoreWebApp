package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.AddressDto;
import hr.algebra.fishingstore.dal.repos.AddressRepository;
import hr.algebra.fishingstore.model.entities.Address;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    public static final String ADDRESS_NOT_FOUND = "Address not found";
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public AddressDto.ResponseDto getById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADDRESS_NOT_FOUND));
        return modelMapper.map(address, AddressDto.ResponseDto.class);
    }

    public List<AddressDto.ResponseDto> getAll() {
        return addressRepository.findAll()
                .stream()
                .map(a -> modelMapper.map(a, AddressDto.ResponseDto.class))
                .toList();
    }

    public AddressDto.ResponseDto create(AddressDto.CreateDto dto) {
        Address address = modelMapper.map(dto, Address.class);
        return modelMapper.map(addressRepository.save(address), AddressDto.ResponseDto.class);
    }

    public AddressDto.ResponseDto update(Long id, AddressDto.EditDto dto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADDRESS_NOT_FOUND));

        modelMapper.map(dto, address);
        return modelMapper.map(addressRepository.save(address), AddressDto.ResponseDto.class);
    }

    public boolean delete(Long id) {
        if (!addressRepository.existsById(id))
            return false;

        addressRepository.deleteById(id);
        return true;
    }
}