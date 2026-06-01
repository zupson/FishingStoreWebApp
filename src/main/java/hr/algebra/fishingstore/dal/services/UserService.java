package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.UserDto;
import hr.algebra.fishingstore.dal.repos.UserRepository;
import hr.algebra.fishingstore.model.entities.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    public static final String USER_NOT_FOUND = "User not found.";

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserDto.ResponseDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        return modelMapper.map(user, UserDto.ResponseDto.class);
    }

    public List<UserDto.ResponseDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(u -> modelMapper.map(u, UserDto.ResponseDto.class))
                .toList();
    }

    public UserDto.ResponseDto update(Long id, UserDto.EditDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        modelMapper.map(dto, user);

        return modelMapper.map(userRepository.save(user), UserDto.ResponseDto.class);
    }

    public boolean delete(Long id) {
        if (!userRepository.existsById(id))
            return false;

        userRepository.deleteById(id);
        return true;
    }
}