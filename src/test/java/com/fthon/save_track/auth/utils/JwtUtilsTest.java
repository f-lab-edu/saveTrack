package com.fthon.save_track.auth.utils;

import com.fthon.save_track.auth.dto.AuthenticatedUserDto;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class JwtUtilsTest {

    private static final String secretKey = "SECRETSECRETSECRETSECRETSECRETSECRETSECRETSECRETSECRETSECRETSECRET";

    private final JwtUtils jwtUtils = new JwtUtils(secretKey, 1000000L);

    @Test
    @DisplayName("JWT 토큰을 생성하고 Parsing 할 수 있다.")
    void testCreateAndParse() throws Exception{
        // given
        AuthenticatedUserDto givenUser = new AuthenticatedUserDto("userUid", "email", "nickname");
        // when
        String jwt = jwtUtils.sign(givenUser, new Date());
        // then
        AuthenticatedUserDto parsedUser = jwtUtils.getUser(jwt);

        assertThat(parsedUser.getUid()).isEqualTo(givenUser.getUid());
        assertThat(parsedUser.getEmail()).isEqualTo(givenUser.getEmail());
        assertThat(parsedUser.getNickname()).isEqualTo(givenUser.getNickname());
    }

    @ParameterizedTest
    @MethodSource("createJwtVerifyInput")
    @DisplayName("JWT 토큰이 잘못되었는지 검증할 수 있다.")
    void testVerify(String token, boolean expectedResult) throws Exception{

        //when
        ThrowableAssert.ThrowingCallable lambda = () -> jwtUtils.validateToken(token);

        //then
        if(expectedResult){
            assertThatCode(lambda).doesNotThrowAnyException();
            return;
        }
        assertThatThrownBy(lambda).isInstanceOf(RuntimeException.class);

    }

    private static Stream<Arguments> createJwtVerifyInput(){
        JwtUtils jwtUtils = new JwtUtils(secretKey, 1000000L);
        AuthenticatedUserDto userInfo = new AuthenticatedUserDto("userUid", "email", "nickname");

        String goodJwt = jwtUtils.sign(userInfo, new Date());
        String expiredJwt = jwtUtils.sign(userInfo, new Date(0));
        String invalidJwt = "asd.fsaft3t3.sawafxx2321";

        return Stream.of(
                Arguments.of(goodJwt, true),
                Arguments.of(expiredJwt, false),
                Arguments.of(invalidJwt, false)
        );
    }

}