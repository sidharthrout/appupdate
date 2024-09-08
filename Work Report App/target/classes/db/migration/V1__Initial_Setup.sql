CREATE TABLE work_report (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             report_date DATE NOT NULL,
                             start_time TIME NOT NULL,
                             end_time TIME NOT NULL,
                             break_time TIME,
                             total_time TIME NOT NULL,
                             work_description VARCHAR(255),
                             status VARCHAR(50)
);

CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(50) NOT NULL,
                      password VARCHAR(100) NOT NULL,
                      role VARCHAR(20) NOT NULL
);
