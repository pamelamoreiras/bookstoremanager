package com.pamelamoreiras.bookstoremanager.users.builder;

import com.pamelamoreiras.bookstoremanager.users.dto.UserDTO;
import com.pamelamoreiras.bookstoremanager.users.enums.Gender;
import com.pamelamoreiras.bookstoremanager.users.enums.Role;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class UserDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Pamela";

    @Builder.Default
    private int age = 23;

    @Builder.Default
    private Gender gender = Gender.FEMALE;

    @Builder.Default
    private String email = "pamela@email.com";

    @Builder.Default
    private String username = "pamelamoreiras";

    @Builder.Default
    private String password = "123456";

    @Builder.Default
    private LocalDate birthDate = LocalDate.of(1999, 9, 24);

    @Builder.Default
    private Role role = Role.USER;

    public UserDTO buildUserDTO() {
        return new UserDTO(
                id,
                name,
                age,
                gender,
                email,
                username,
                password,
                birthDate,
                role
        );
    }

}
