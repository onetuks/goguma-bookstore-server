package com.onetuks.moduleauth.util.author;

import com.onetuks.modulecommon.error.ErrorCode;
import com.onetuks.modulecommon.exception.ApiAccessDeniedException;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.moduleauth.jwt.CustomUserDetails;
import jakarta.annotation.Nonnull;
import java.util.Arrays;
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

  private final AuthorService authorService;

  public AuthorIdResolver(AuthorService authorService) {
    this.authorService = authorService;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return Arrays.stream(parameter.getParameterAnnotations())
        .anyMatch(annotation -> annotation.annotationType().equals(AuthorId.class));
  }

  @Override
  public Object resolveArgument(
      @Nonnull MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      @Nonnull NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isNotAdminOrAuthor =
        authentication.getAuthorities().stream().noneMatch(this::isAdminOrAuthorRole);

    if (isNotAdminOrAuthor) {
      throw new ApiAccessDeniedException(ErrorCode.ONLY_AUTHOR_ADMIN_METHOD);
    }

    Long loginId = ((CustomUserDetails) authentication.getPrincipal()).getLoginId();

    return authorService.getAuthorIdByMemberId(loginId);
  }

  private boolean isAdminOrAuthorRole(GrantedAuthority grantedAuthority) {
    return Objects.equals(grantedAuthority.getAuthority(), RoleType.AUTHOR.name())
        || Objects.equals(grantedAuthority.getAuthority(), RoleType.ADMIN.name());
  }
}