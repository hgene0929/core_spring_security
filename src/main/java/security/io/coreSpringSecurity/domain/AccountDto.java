package security.io.coreSpringSecurity.domain;

import lombok.Data;

@Data
public class AccountDto {

    private String username;

    private String password;

    private String email;

    private String age;
}
