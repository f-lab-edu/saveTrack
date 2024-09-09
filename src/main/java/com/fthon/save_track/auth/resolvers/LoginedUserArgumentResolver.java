package com.fthon.save_track.auth.resolvers;


import com.fthon.save_track.auth.dto.AuthenticatedUserDto;
import com.fthon.save_track.auth.resolvers.annotation.LoginedUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginedUserArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isParameterTypeSupported = parameter.getParameterType().isAssignableFrom(AuthenticatedUserDto.class);
        boolean hasAnnotation = parameter.hasParameterAnnotation(LoginedUser.class);

        return isParameterTypeSupported && hasAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof AuthenticatedUserDto dto){
            return dto;
        }

        // 개발 단계에서 임시로 사용할 객체
        return new AuthenticatedUserDto(
                "user-001",
                "useremail@email.com",
                "test"
        );
    }
}