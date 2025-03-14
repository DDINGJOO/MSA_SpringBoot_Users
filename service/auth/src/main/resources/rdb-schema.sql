CREATE TABLE auth (
                      id VARCHAR(255) PRIMARY KEY,
                      login_id VARCHAR(100) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      nickname VARCHAR(100) NOT NULL,
                      role VARCHAR(50) NOT NULL,
                      provider VARCHAR(100),
                      provider_id VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
