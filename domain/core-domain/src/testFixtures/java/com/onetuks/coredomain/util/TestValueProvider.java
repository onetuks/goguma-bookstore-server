package com.onetuks.coredomain.util;

import com.onetuks.coredomain.book.model.vo.BookConceptualInfo;
import com.onetuks.coredomain.book.model.vo.BookPhysicalInfo;
import com.onetuks.coredomain.book.model.vo.BookPriceInfo;
import com.onetuks.coredomain.member.model.vo.AddressInfo;
import com.onetuks.coredomain.member.model.vo.AuthInfo;
import com.onetuks.coredomain.member.model.vo.Nickname;
import com.onetuks.coredomain.registration.model.vo.ApprovalInfo;
import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.coreobj.enums.member.ClientProvider;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.vo.UUIDProvider;
import java.util.List;
import java.util.Random;

public class TestValueProvider {

  private static final Random random = new Random();

  private static final List<String> titles =
      List.of(
          "빠니보틀의 유라시아 여행기",
          "곽튜브의 유라시아 여행기",
          "빠니보틀의 아메리카 여행기",
          "곽튜브의 아제르바이잔 여행기",
          "침착맨 삼국지",
          "임용한 삼국지",
          "정사 삼국지",
          "연의 삼국지",
          "난장이가 쏘아올린 작은 공",
          "사도세자의 고백",
          "한중록",
          "동물농장",
          "연을 쫓는 아이",
          "내 영혼이 따뜻했던 날들",
          "웃는남자",
          "미움받을 용기");
  private static final List<String> oneLiner =
      List.of(
          "유튜브 대통령의 유라시아 행차 일기",
          "유튜브 부통령의 유라시아 행차 일기",
          "유튜브 대통령의 아메리카 행차 일기",
          "유튜브 부통령의 아제르바이잔 행차 일기",
          "유튜브가 허락한 유일한 보약",
          "밥도둑",
          "점심도둑",
          "올타임 레전드",
          "조세희 소설집",
          "불쌍한 사도세자",
          "불쌍한 사도세자 마누라 홍씨",
          "조지오웰은 옳았다",
          "100미터 13초",
          "언제일까",
          "웃상남자",
          "미음받을 용기그릇");
  private static final List<String> summaries =
      List.of(
          "대충 태국에서 시작해서 유럽에서 끝났나? 기억 안 남",
          "곽튜브는 어디에서 왔는가?",
          "대충 미국 동부에서 시작해서 남극까지 갔던걸로 기억함",
          "곽튜브는 아제르바이잔 사람임",
          "침착맨은 오장원까지다",
          "임용한은 촉빠다.",
          "진수는 인성이 안 좋다",
          "나관중은 신이다.",
          "난장이가 쏘아올린 작은 공은 무엇일까?",
          "사도세자의 고백은 무엇일까?",
          "한중록은 무엇일까?",
          "동물농장은 무엇일까?",
          "연을 쫓는 아이는 누구일까?",
          "내 영혼이 따뜻했던 날들은 무엇일까?",
          "웃는남자는 누구일까?",
          "미움받을 용기는 무엇일까?");
  private static final List<String> publishers =
      List.of("아무거나보틀", "샌드박스", "끄박", "침튜브", "임용한튜브", "이성과힘", "자화상", "현대지성", "비즈니스북스");
  private static final List<String> authorNicknames =
      List.of("빠니보틀", "곽튜브", "임용한", "침착맨", "진수", "나관중", "조지오엘");

  public static long createId() {
    return random.nextLong(1, Long.MAX_VALUE / 2);
  }

  public static String createTitle() {
    return titles.get(random.nextInt(titles.size()));
  }

  public static String createOneLiner() {
    return oneLiner.get(random.nextInt(oneLiner.size()));
  }

  public static String createSummary() {
    return summaries.get(random.nextInt(summaries.size()));
  }

  public static List<Category> createCategories() {
    Random random = new Random();
    int count = random.nextInt(1, 4);
    return random
        .ints(0, Category.values().length)
        .distinct()
        .limit(count)
        .boxed()
        .map(index -> Category.values()[index])
        .toList();
  }

  public static String createPublisher() {
    return publishers.get(random.nextInt(publishers.size()));
  }

  public static String createIsbn() {
    return String.valueOf(random.nextLong(1_000_000_000_000L, 9_999_999_999_999L));
  }

  public static BookConceptualInfo createBookConceptualInfo() {
    return new BookConceptualInfo(
        createTitle(),
        createOneLiner(),
        createSummary(),
        createCategories(),
        createPublisher(),
        createIsbn()
    );
  }

  public static int createHeight() {
    return random.nextInt(100, 300);
  }

  public static int createWidth() {
    return random.nextInt(50, 150);
  }

  public static String createCoverType() {
    return random.nextBoolean() ? "양장본" : "포켓북";
  }

  public static long createPageCount() {
    return random.nextLong(100L, 500L);
  }

  public static BookPhysicalInfo createBookPhysicalInfo() {
    return new BookPhysicalInfo(
        createHeight(),
        createWidth(),
        createCoverType(),
        createPageCount()
    );
  }

  public static long createPrice() {
    return random.nextLong(10_000L, 30_000L);
  }

  public static int createSalesRate() {
    return random.nextInt(0, 11);
  }

  public static boolean createPromotion() {
    return random.nextBoolean();
  }

  public static long createStockCount() {
    return random.nextLong(1L, 100L);
  }

  public static BookPriceInfo createBookPriceInfo() {
    return new BookPriceInfo(
        createPrice(),
        createSalesRate(),
        createPromotion(),
        createStockCount()
    );
  }

  public static String createBusinessNumber() {
    return String.valueOf(random.nextLong(1_000_000_000L, 9_999_999_999L));
  }

  public static String createMailOrderSalesNumber() {
    return String.valueOf(random.nextLong(100_000_000_000_000_000L, 999_999_999_999_999_999L));
  }

  public static Nickname createAuthorNickname() {
    return new Nickname(
        authorNicknames.get(random.nextInt(authorNicknames.size())) + UUIDProvider.provideUUID());
  }

  public static String createInstagramUrl() {
    return "https://www.instagram.com/" + UUIDProvider.provideUUID();
  }

  public static String createSocialId() {
    return UUIDProvider.provideUUID();
  }

  public static ClientProvider createClientProvider() {
    return ClientProvider.values()[random.nextInt(ClientProvider.values().length)];
  }

  public static List<RoleType> createRoles(RoleType roleType) {
    if (roleType == RoleType.ADMIN) {
      return List.of(RoleType.USER, RoleType.AUTHOR, RoleType.ADMIN);
    } else if (roleType == RoleType.AUTHOR) {
      return List.of(RoleType.USER, RoleType.AUTHOR);
    }
    return List.of(RoleType.USER);
  }

  public static AuthInfo createAuthInfo(RoleType roleType) {
    return new AuthInfo(
        createAuthorNickname().nicknameValue(),
        createSocialId(),
        createClientProvider(),
        createRoles(roleType)
    );
  }

  public static String createAddress() {
    return "강원도 춘천시";
  }

  public static String createAddressDetail() {
    return "어딘가";
  }

  public static AddressInfo createAddressInfo() {
    return new AddressInfo(
        createAddress(),
        createAddressDetail()
    );
  }

  public static String createApprovalMemo(boolean isApproved) {
    return isApproved ? "승인되었습니다." : "신간 등록 검수 중입니다.";
  }

  public static ApprovalInfo createApprovalInfo(boolean isApproved) {
    return new ApprovalInfo(
        isApproved,
        createApprovalMemo(isApproved)
    );
  }
}
