-- users 테이블에 role 컬럼 추가 (기존 데이터는 USER로 기본값 설정)
ALTER TABLE `users`
    ADD COLUMN IF NOT EXISTS `role` VARCHAR(20) NOT NULL DEFAULT 'USER'
    AFTER `password_hash`;
