CREATE TABLE IF NOT EXISTS members
(
    member_id BIGINT NOT NULL COMMENT '멤버아이디' AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL COMMENT '멤버 실명',
    social_id VARCHAR(255) NOT NULL COMMENT '로그인 소셜 아이디',
    client_provider VARCHAR(255) NOT NULL COMMENT '로그인 클라이언트',
    role_type enum('USER', 'AUTHOR', 'ADMIN') NOT NULL COMMENT '멤버 타입',
    nickname VARCHAR(255) COMMENT '멤버 닉네임',
    profile_img_uri VARCHAR(255) COMMENT '멤버 프로필',
    default_address VARCHAR(255) COMMENT '기본 배송지',
    default_address_detail VARCHAR(255) COMMENT '기본 배송지 상세',
    default_cash_receipt_type ENUM('PERSON', 'COMPANY') COMMENT '기본 현금영수증 타입',
    default_cash_receipt_number VARCHAR(255) COMMENT '기본 현금영수증 번호',
    PRIMARY KEY (member_id),
    UNIQUE KEY unique_socialid_and_clientprovider (social_id, client_provider)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS authors
(
    author_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '작가 식별자',
    member_id BIGINT NOT NULL UNIQUE COMMENT '멤버 식별자',
    profile_img_uri VARCHAR(255) NOT NULL DEFAULT '' COMMENT '작가 프로필',
    nickname VARCHAR(255) NOT NULL COMMENT '필명',
    introduction VARCHAR(255) NOT NULL COMMENT '한줄소개',
    escrow_service_uri VARCHAR(255) COMMENT '구매안전증',
    mail_order_sales_uri VARCHAR(255) COMMENT '통신판매증',
    enrollment_passed BOOLEAN NOT NULL DEFAULT FALSE COMMENT '입점 승인 여부',
    PRIMARY KEY (author_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS author_statics
(
    author_statics_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '작가 통계 식별자',
    author_id BIGINT NOT NULL UNIQUE COMMENT '작가 식별자',
    subscribe_count BIGINT NOT NULL DEFAULT 0 COMMENT '구독자 수',
    book_count BIGINT NOT NULL DEFAULT 0 COMMENT '등록 도서 수',
    restock_count BIGINT NOT NULL DEFAULT 0 COMMENT '재입고 수',
    PRIMARY KEY (author_statics_id),
    FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS subscribes
(
    subscribe_id BIGINT NOT NULL COMMENT '구독 식별자' AUTO_INCREMENT,
    member_id BIGINT NOT NULL COMMENT '멤버 식별자',
    author_id BIGINT NOT NULL COMMENT '작가 식별자',
    PRIMARY KEY (subscribe_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE ,
    FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE ,
    UNIQUE KEY unique_memberid_and_authorid (member_id, author_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS registrations
(
    registration_id BIGINT NOT NULL COMMENT '신간등록 식별자' AUTO_INCREMENT,
    author_id BIGINT NOT NULL COMMENT '작가 식별자',
    approval_result BOOLEAN NOT NULL DEFAULT FALSE COMMENT '승인여부',
    approval_memo VARCHAR(255) NOT NULL DEFAULT '' COMMENT '승인메모',
    cover_img_uri VARCHAR(255) NOT NULL COMMENT '도서 표지',
    title VARCHAR(255) NOT NULL COMMENT '도서명',
    summary VARCHAR(255) NOT NULL COMMENT '줄거리',
    price BIGINT NOT NULL DEFAULT 0 COMMENT '도서가격',
    stock_count BIGINT NOT NULL DEFAULT 0 COMMENT '재고수량',
    isbn VARCHAR(255) NOT NULL COMMENT 'isbn',
    publisher VARCHAR(255) NOT NULL COMMENT '출판사',
    promotion BOOLEAN NOT NULL DEFAULT FALSE COMMENT '프로모션 선택 여부',
    sample_uri VARCHAR(255) NOT NULL COMMENT '샘플 PDF 파일',
    PRIMARY KEY (registration_id),
    FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS books
(
    book_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '도서 식별자',
    author_id BIGINT NOT NULL COMMENT '작가 식별자',
    author_nickname VARCHAR(255) NOT NULL COMMENT '작가 닉네임',
    cover_img_uri VARCHAR(255) NOT NULL COMMENT '도서 표지',
    title VARCHAR(255) NOT NULL COMMENT '도서명',
    category ENUM('ETC', 'NOBEL', 'ESSEY', 'CARTOON') NOT NULL DEFAULT 'ETC' COMMENT '도서 카테고리',
    summary VARCHAR(255) NOT NULL COMMENT '줄거리',
    price BIGINT NOT NULL DEFAULT 0 COMMENT '도서가격',
    stock_count BIGINT NOT NULL DEFAULT 0 COMMENT '재고수량',
    isbn VARCHAR(255) NOT NULL COMMENT 'isbn',
    publisher VARCHAR(255) NOT NULL COMMENT '출판사',
    promotion BOOLEAN NOT NULL DEFAULT FALSE COMMENT '프로모션 선택 여부',
    PRIMARY KEY (book_id),
    FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS book_statics
(
    book_statics_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '도서 통계 식별자',
    book_id BIGINT NOT NULL UNIQUE COMMENT '도서 식별자',
    favorite_count BIGINT NOT NULL DEFAULT 0 COMMENT '좋아요 수',
    view_count BIGINT NOT NULL DEFAULT 0 COMMENT '조회수',
    PRIMARY KEY (book_statics_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS comments
(
    comment_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '한줄평 식별자',
    book_id BIGINT NOT NULL COMMENT '도서 식별자',
    member_id BIGINT NOT NULL COMMENT '멤버 식별자',
    title VARCHAR(255) NOT NULL COMMENT '한줄평 제목',
    content VARCHAR(255) NOT NULL COMMENT '한줄평 내용',
    PRIMARY KEY (comment_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE ,
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE ,
    UNIQUE KEY unique_bookid_memberid (book_id, member_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS favorites
(
    favorite_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '재입고 식별자',
    member_id BIGINT NOT NULL COMMENT '멤버 식별자',
    book_id BIGINT NOT NULL COMMENT '도서 식별자',
    PRIMARY KEY (favorite_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE ,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE ,
    UNIQUE KEY unique_memberid_bookid (member_id, book_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS restocks
(
    restock_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '재입고 식별자',
    member_id BIGINT NOT NULL COMMENT '멤버 식별자',
    book_id BIGINT NOT NULL COMMENT '도서 식별자',
    PRIMARY KEY (restock_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE ,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE ,
    UNIQUE KEY unique_memberid_bookid (member_id, book_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS orders
(
    order_id BIGINT NOT NULL COMMENT '주문내역 식별자' AUTO_INCREMENT,
    member_id BIGINT NOT NULL COMMENT '멤버 식별자',
    address VARCHAR(255) NOT NULL COMMENT '배송지',
    address_detail VARCHAR(255) NOT NULL COMMENT '배송지 상세',
    delivery_fee BIGINT NOT NULL COMMENT '배송비' DEFAULT 0,
    total_price BIGINT NOT NULL COMMENT '최종 결제 가격' DEFAULT 0,
    payment_client ENUM('ACCOUNT', 'TOSS', 'KAKAO') NOT NULL COMMENT '결제 수단',
    cash_receipt_type ENUM('PERSON', 'COMPANY') COMMENT '현금영수증 구분',
    cash_receipt_number VARCHAR(255) COMMENT '현금영수증 번호',
    ordered_date DATETIME NOT NULL COMMENT '주문일자' DEFAULT NOW(),
    payment_date DATETIME NOT NULL COMMENT '결제일자' DEFAULT NOW(),
    PRIMARY KEY (order_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS items
(
    item_id BIGINT NOT NULL COMMENT '장바구니 아이템 식별자' AUTO_INCREMENT,
    book_id BIGINT NOT NULL COMMENT '도서 식별자',
    member_id BIGINT NOT NULL COMMENT '멤버 식별자',
    order_id BIGINT COMMENT '주문내역 식별자',
    price BIGINT NOT NULL COMMENT '도서가격',
    quantity BIGINT NOT NULL COMMENT '주문수량',
    PRIMARY KEY (item_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE ,
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE ,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
