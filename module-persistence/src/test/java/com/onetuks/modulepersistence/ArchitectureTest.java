package com.onetuks.modulepersistence;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ArchitectureTest extends PersistenceIntegrationTest {

  JavaClasses javaClasses;

  @BeforeEach
  void setUp() {
    javaClasses =
        new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests()) // 테스트 클래스는 이 검증에서 제외
            .importPackages("com.onetuks.modulepersistence");
  }

  @Nested
  class ClassNameTest {
    @Test
    @DisplayName("repository 패키지 안에 있는 클래스는 Repository 로 끝난다.")
    void repository_ClassNamePostfix_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..repository..")
              .should()
              .haveSimpleNameEndingWith("Repository");

      rule.check(javaClasses);
    }
  }

  @Nested
  class DependancyTest {

    @Test
    @DisplayName("Model은 오직 Service와 Repository, Component에 의해서만 의존한다")
    void model_HaveDependancy_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..model")
              .should()
              .onlyHaveDependentClassesThat()
              .resideInAnyPackage(
                  "..service..", "..repository..", "..model..", "..auth..", "..vo..");

      rule.check(javaClasses);
    }

    @Test
    @DisplayName("Model은 아무것도 의존하지 않는다.")
    void model_NoDependOn_Test() {
      ArchRule rule =
          ArchRuleDefinition.classes()
              .that()
              .resideInAnyPackage("..model")
              .and()
              .haveSimpleNameNotStartingWith("Q")
              .should()
              .onlyDependOnClassesThat()
              .resideInAnyPackage("..model..", "java..", "jakarta..", "lombok..", "..vo..");

      rule.check(javaClasses);
    }
  }
}
