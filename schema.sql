-- =====================================================================
-- UBT TECHNOLOGY — MySQL Database Schema
-- Run this once to create the database and tables manually,
-- OR let Hibernate auto-create them (spring.jpa.hibernate.ddl-auto=update)
-- =====================================================================

-- Create database
CREATE DATABASE IF NOT EXISTS ubt_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE ubt_db;

-- ─── gallery_items ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS gallery_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255)  NOT NULL,
    category    VARCHAR(50)   NOT NULL COMMENT 'college-visit | student-project',
    img_url     VARCHAR(1000) NOT NULL,
    item_date   DATE,
    is_active   TINYINT(1)    NOT NULL DEFAULT 1,
    created_at  DATETIME(6),
    updated_at  DATETIME(6),
    INDEX idx_gallery_category  (category),
    INDEX idx_gallery_active    (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─── projects ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS projects (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255)  NOT NULL,
    domain       VARCHAR(150),
    level        VARCHAR(50)   NOT NULL COMMENT 'PhD R&D | ME/MTech | BE/BTech | MBA Projects',
    img_url      VARCHAR(1000),
    description  TEXT,
    tech_stack   VARCHAR(500)  COMMENT 'Comma-separated technologies',
    duration     VARCHAR(50),
    project_date VARCHAR(10)   COMMENT 'e.g. 2024-03',
    is_active    TINYINT(1)    NOT NULL DEFAULT 1,
    created_at   DATETIME(6),
    updated_at   DATETIME(6),
    INDEX idx_projects_level  (level),
    INDEX idx_projects_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─── updates ──────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS updates (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    type        VARCHAR(30)   NOT NULL DEFAULT 'announcement'
                    COMMENT 'announcement | achievement | event | news',
    title       VARCHAR(500)  NOT NULL,
    content     TEXT          NOT NULL,
    update_date DATE,
    is_pinned   TINYINT(1)    NOT NULL DEFAULT 0,
    is_active   TINYINT(1)    NOT NULL DEFAULT 1,
    created_at  DATETIME(6),
    updated_at  DATETIME(6),
    INDEX idx_updates_pinned (is_pinned),
    INDEX idx_updates_type   (type),
    INDEX idx_updates_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─── contact_inquiries ────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS contact_inquiries (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(150)  NOT NULL,
    email        VARCHAR(200)  NOT NULL,
    phone        VARCHAR(20),
    service      VARCHAR(100),
    message      TEXT,
    status       VARCHAR(20)   NOT NULL DEFAULT 'NEW'
                     COMMENT 'NEW | IN_PROGRESS | RESOLVED | CLOSED',
    submitted_at DATETIME(6),
    INDEX idx_inquiry_status (status),
    INDEX idx_inquiry_email  (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─── testimonials ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS testimonials (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(150)  NOT NULL,
    role       VARCHAR(200),
    text       TEXT,
    rating     INT           DEFAULT 5,
    avatar     VARCHAR(500),
    is_active  TINYINT(1)    NOT NULL DEFAULT 1,
    created_at DATETIME(6),
    INDEX idx_testimonial_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================================
-- Optional: verify all tables were created
-- =====================================================================
SHOW TABLES;
