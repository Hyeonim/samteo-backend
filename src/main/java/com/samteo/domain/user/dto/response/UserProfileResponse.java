package com.samteo.domain.user.dto.response;

import com.samteo.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserProfileResponse {

    private Long userId;
    private String email;
    private String name;
    private String provider;
    private long postCount;
    private long followerCount;
    private long followingCount;
    private boolean me;
    private boolean followedByMe;

    public static UserProfileResponse of(
            User user,
            long postCount,
            long followerCount,
            long followingCount,
            boolean me,
            boolean followedByMe
    ) {
        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .provider(user.getProvider())
                .postCount(postCount)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .me(me)
                .followedByMe(followedByMe)
                .build();
    }
}
