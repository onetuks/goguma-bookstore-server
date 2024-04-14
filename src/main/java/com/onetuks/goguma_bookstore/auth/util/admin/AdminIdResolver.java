package com.onetuks.goguma_bookstore.auth.util.admin;

import com.onetuks.goguma_bookstore.auth.jwt.CustomUserDetails;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AdminIdResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return Arrays.stream(parameter.getParameterAnnotations())
        .anyMatch(annotation -> annotation.annotationType().equals(AdminId.class));
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isNotAdmin =
        authentication.getAuthorities().stream()
            .noneMatch(
                grantedAuthority ->
                    Objects.equals(grantedAuthority.getAuthority(), RoleType.ADMIN.name()));

    if (isNotAdmin) {
      throw new AccessDeniedException("Only Admin role can access this method.");
    }
    return ((CustomUserDetails) authentication.getPrincipal()).getLoginId();
  }
}
