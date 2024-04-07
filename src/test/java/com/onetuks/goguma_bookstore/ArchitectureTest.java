package com.onetuks.goguma_bookstore;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

public class ArchitectureTest {

  JavaClasses javaClasses;

  @BeforeEach
  void setUp() {
    javaClasses =
        new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests()) // 테스트 클래스는 이 검증에서 제외
            .importPackages("com.onetuks.goguma_bookstore");
  }

  @Nested
  class ClassNameTest {

    @Test
    @DisplayName("Controller 패키지 안에 있는 클래스는 Controller로 끝나야 한다.")
    void controller_ClassNamePostfix_Test() {
      ArchRule classNameRule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..controller")
              .should()
              .haveSimpleNameEndingWith("Controller");

      ArchRule annotationRule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..controller")
              .should()
              .beAnnotatedWith(RestController.class)
              .orShould()
              .beAnnotatedWith(Controller.class);

      classNameRule.check(javaClasses);
      annotationRule.check(javaClasses);
    }

    @Test
    @DisplayName("request 패키지 안에 있는 클래스는 Request 로 끝난다.")
    void request_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..request..")
              .should()
              .haveSimpleNameEndingWith("Request");

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("response 패키지 안에 있는 클래스는 Response 로 끝난다.")
    void response_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..response..")
              .should()
              .haveSimpleNameEndingWith("Response");

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("repository 패키지 안에 있는 클래스는 Repository 로 끝나고, 인터페이스이다.")
    void repository_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..repository..")
              .should()
              .haveSimpleNameEndingWith("Repository")
              .andShould()
              .beInterfaces();

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("service 패키지 안에 있는 클래스는 Service 로 끝난다.")
    void service_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..service..")
              .and()
              .resideOutsideOfPackage("..dto..")
              .and()
              .resideOutsideOfPackage("..vo..")
              .should()
              .haveSimpleNameEndingWith("Service")
              .andShould()
              .beAnnotatedWith(Service.class)
              .orShould()
              .beAnnotatedWith(Component.class);

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("config 패키지 안에 있는 클래스는 Config 로 끝난다.")
    void config_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..config..")
              .and()
              .doNotHaveSimpleName("AuthPermittedEndpoint")
              .should()
              .haveSimpleNameEndingWith("Config")
              .andShould()
              .beAnnotatedWith(Configuration.class);

      rule.check(javaClasses);
    }
  }

  @Nested
  class DependancyTest {

    @Test
    @DisplayName("Controller 는 Service 와 Request/Response 클래스를 사용할 수 있다.")
    void controller_DependOn_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..controller")
              .should()
              .dependOnClassesThat()
              .resideInAnyPackage("..requsst..", "..response..", "..service..");

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("Controller는 의존되지 않는다.")
    void controller_HaveNoDependancy_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..controller")
              .should()
              .onlyHaveDependentClassesThat()
              .resideInAnyPackage("..controller");

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("Controller는 모델을 사용할 수 없다.")
    void controller_NoDependOn_Test() {
      ArchRule rule =
          ArchRuleDefinition.noClasses()
              .that()
              .resideInAnyPackage("..controller")
              .should()
              .dependOnClassesThat()
              .resideInAnyPackage("..model..")
              .andShould()
              .resideOutsideOfPackage("..vo..");

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("Service는 Controller를 의존하면 안 된다")
    void service_NoDependOn_Test() {
      ArchRule rule =
          ArchRuleDefinition.noClasses()
              .that()
              .resideInAnyPackage("..service")
              .should()
              .dependOnClassesThat()
              .resideInAnyPackage("..controller");

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("Model은 오직 Service와 Repository, Component에 의해서만 의존한다")
    void model_HaveDependancy_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..model")
              .should()
              .onlyHaveDependentClassesThat()
              .resideInAnyPackage("..service..", "..repository..", "..model..", "..auth..");

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("Model은 아무것도 의존하지 않는다.")
    void model_NoDependOn_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..model..")
              .should()
              .onlyDependOnClassesThat()
              .resideInAnyPackage("..model..", "java..", "jakarta..", "lombok..", "..vo..");

      rule.check(javaClasses);
    }
  }
}
