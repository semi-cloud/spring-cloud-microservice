package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.repository.UserRepository;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(username));

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);
        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalStateException("User not found"));
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        userDto.setOrders(new ArrayList<>());       //??? ?????? ??????
        return userDto;
    }

    @Override
    public List<UserDto> getUserByAll() {
        List<UserEntity> list = Lists.newArrayList(userRepository.findAll());
        return list.stream().map(x -> new ModelMapper().map(x, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email));
        return new ModelMapper().map(user, UserDto.class);
    }
}
