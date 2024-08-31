package dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import dto.response.RoleResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate dob;

    Set<RoleResponse> roles;
}
