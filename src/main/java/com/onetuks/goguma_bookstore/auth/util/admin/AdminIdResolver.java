package com.onetuks.goguma_bookstore.auth.util.admin;

import com.onetuks.goguma_bookstore.auth.jwt.CustomUserDetails;
import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import java.nio.file.AccessDeniedException;
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
    return Objects.equals(parameter.getParameterType(), Long.class);
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
                    Objects.equals(grantedAuthority.getAuthority(), RoleType.ADMIN.getRoleName()));

    if (isNotAdmin) {
      throw new AccessDeniedException("Only Admin role can access this method.");
    }
    return ((CustomUserDetails) authentication.getPrincipal()).getLoginId();
  }
}
