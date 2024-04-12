package com.onetuks.goguma_bookstore.auth.util.author;

import com.onetuks.goguma_bookstore.auth.jwt.CustomUserDetails;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import java.nio.file.AccessDeniedException;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthorIdResolver implements HandlerMethodArgumentResolver {

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
    boolean isNotAdminOrAuthor =
        authentication.getAuthorities().stream().noneMatch(this::isAdminOrAuthorRole);

    if (isNotAdminOrAuthor) {
      throw new AccessDeniedException("Only Admin or Author Role can access this method.");
    }

    return ((CustomUserDetails) authentication.getPrincipal()).getLoginId();
  }

  private boolean isAdminOrAuthorRole(GrantedAuthority grantedAuthority) {
    return Objects.equals(grantedAuthority.getAuthority(), RoleType.AUTHOR.getRoleName())
        || Objects.equals(grantedAuthority.getAuthority(), RoleType.ADMIN.getRoleName());
  }
}
