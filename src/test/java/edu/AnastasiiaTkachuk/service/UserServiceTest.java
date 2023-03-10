package edu.AnastasiiaTkachuk.service;

import edu.AnastasiiaTkachuk.dao.UserDao;
import edu.AnastasiiaTkachuk.dto.CreateUserDto;
import edu.AnastasiiaTkachuk.dto.UserDto;
import edu.AnastasiiaTkachuk.entity.Gender;
import edu.AnastasiiaTkachuk.entity.Role;
import edu.AnastasiiaTkachuk.entity.User;
import edu.AnastasiiaTkachuk.exception.ValidationException;
import edu.AnastasiiaTkachuk.mapper.CreateUserMapper;
import edu.AnastasiiaTkachuk.mapper.UserMapper;
import edu.AnastasiiaTkachuk.validator.CreateUserValidator;
import edu.AnastasiiaTkachuk.validator.Error;
import edu.AnastasiiaTkachuk.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private CreateUserValidator createUserValidator;
    @Mock
    private UserDao userDao;
    @Mock
    private CreateUserMapper createUserMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    @Test
    void loginSuccess(){
        User user = getUser();
        UserDto userDto = getUserDto();

        doReturn(Optional.of(user)).when(userDao).findByEmailAndPassword(user.getEmail(), user.getPassword());
        doReturn(userDto).when(userMapper).map(user);

        Optional<UserDto> actualResult = userService.login(user.getEmail(), user.getPassword());
        assertThat(actualResult).isPresent();

        assertThat(actualResult.get()).isEqualTo(userDto);
    }
    @Test
    void loginFailed(){
        doReturn(Optional.empty()).when(userDao).findByEmailAndPassword(any(), any());
        Optional<UserDto> actualResult = userService.login("dummy","123");
        assertThat(actualResult).isEmpty();
        verifyNoInteractions(userMapper);

    }
    @Test
    void create(){
        CreateUserDto createUserDto = getCreateUserDto();
        User user = getUser();
        UserDto userDto = getUserDto();
        doReturn(new ValidationResult()).when(createUserValidator).validate(createUserDto);
        doReturn(user).when(createUserMapper).map(createUserDto);
        doReturn(userDto).when(userMapper).map(user);

        UserDto actualResult = userService.create(createUserDto);

        assertThat(actualResult).isEqualTo(userDto);
        verify(userDao).save(user);
    }
    @Test
    void shouldThrowExceptionIfDtoInvalid(){
        CreateUserDto createUserDto = getCreateUserDto();
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.role", "message"));

        doReturn(validationResult).when(createUserValidator).validate(createUserDto);

        assertThrows(ValidationException.class, () -> userService.create(createUserDto));
        verifyNoInteractions(userMapper, userDao, createUserMapper);

    }

    private static CreateUserDto getCreateUserDto() {
        return CreateUserDto.builder()
                .name("Ivan")
                .email("test@gmail.com")
                .password("123")
                .birthday("2000-01-01")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();
    }

    private static UserDto getUserDto() {
        return UserDto.builder()
                .id(99)
                .name("Ivan")
                .gender(Gender.MALE)
                .role(Role.USER)
                .email("test@gmail.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
    }

    private static User getUser() {
        return User.builder()
                .id(99)
                .name("Ivan")
                .gender(Gender.MALE)
                .role(Role.USER)
                .email("test@gmail.com")
                .password("123")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
    }
}





