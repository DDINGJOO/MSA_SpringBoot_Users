CREATE TABLE user_profile (
                              user_id VARCHAR(26) PRIMARY KEY,         -- FK to users.id (ULID/UUID 등 가능)
                              nickname VARCHAR(100) NOT NULL,
                              email VARCHAR(150),
                              phone VARCHAR(20),
                              address_id BIGINT,                       -- FK to address.id (Address 모듈 연동)
                              profile_image_url TEXT,
                              preferred1 VARCHAR(50),
                              preferred2 VARCHAR(50),
                              introduction TEXT,
                              total_point INT DEFAULT 0,
                              user_level INT DEFAULT 1,
                              created_at DATETIME NOT NULL,
                              updated_at DATETIME NOT NULL
);

CREATE INDEX idx_user_profile_nickname ON user_profile(nickname);
CREATE INDEX idx_user_profile_address ON user_profile(address_id);
