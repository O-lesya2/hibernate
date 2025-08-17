package springboot.mapper;

import org.springframework.stereotype.Component;
import springboot.dto.UserDto;
import springboot.model.User;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getAge());
    }
}
