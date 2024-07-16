package com.example.cooks_corner.mapper;

import com.example.cooks_corner.dto.UserListDto;
import com.example.cooks_corner.entity.AppUser;

public class UserMapper {
    public static UserListDto mapToUserListDto(AppUser user) {
        return new UserListDto(
                user.getId(),
                user.getName(),
                user.getBio(),
                user.getImage() != null ? user.getImage().getUrl() : null);
    }
}
