package com.fthon.save_track.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoUserInfo {
    private Long id;

    @JsonProperty("connected_at")
    private String connectedAt;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class KakaoAccount {
        private Profile profile;
        private String name;
        private String email;

        @JsonProperty("age_range")
        private String ageRange;

        private String birthyear;
        private String birthday;
        private String gender;

        @JsonProperty("phone_number")
        private String phoneNumber;


        @JsonIgnoreProperties(ignoreUnknown = true)
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @Getter
        public static class Profile {
            private String nickname;

            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl;

            @JsonProperty("profile_image_url")
            private String profileImageUrl;

        }
    }
}