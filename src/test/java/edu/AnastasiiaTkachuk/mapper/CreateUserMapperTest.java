package edu.AnastasiiaTkachuk.mapper;

import edu.AnastasiiaTkachuk.dto.CreateUserDto;
import edu.AnastasiiaTkachuk.entity.Gender;
import edu.AnastasiiaTkachuk.entity.Role;
import edu.AnastasiiaTkachuk.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserMapperTest {

    private final CreateUserMapper mapper = CreateUserMapper.getInstance();

    @Test
    void map() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .email("test@gmail.com")
                .password("123")
                .birthday("2000-01-01")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();

        User actualResult = mapper.map(dto);

        User expectedResult = User.builder()
                .name("Ivan")
                .gender(Gender.MALE)
                .role(Role.USER)
                .email("test@gmail.com")
                .password("123")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}