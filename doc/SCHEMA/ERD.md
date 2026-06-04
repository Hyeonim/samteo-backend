# ERD

This document summarizes the MariaDB schema used for local schema initialization.

Source SQL:

```text
src/main/resources/schema/mariadb/01_schema.sql
src/main/resources/schema/mariadb/02_seed.sql
```

Reference ERD draft:

```text
src/main/resources/reference/ERD.md
```

## Entity Relationship Diagram

```mermaid
erDiagram
    USER ||--o{ PLAN : creates
    REGION ||--o{ JOB : has
    REGION ||--o{ ACCOMMODATION : has
    REGION ||--o{ PLAN : selected
    REGION ||--o{ FESTIVAL : hosts
    JOB_TYPE ||--o{ JOB : categorizes
    JOB ||--o{ JOB_TAG : has
    JOB ||--o{ PLAN_JOB : included
    JOB ||--o{ ACC_DISPOSABLE : referenced
    ACCOMMODATION ||--o{ ACC_TAG : has
    ACCOMMODATION ||--o{ ACC_DISPOSABLE : calculates
    ACCOMMODATION ||--o{ PLAN : chosen
    PLAN ||--o{ PLAN_JOB : contains
    PLAN ||--o{ PLAN_CALENDAR : has

    USER {
        bigint user_id PK
        varchar email UK
        varchar name
        varchar profile_image
        enum provider
        varchar provider_id
        varchar password_hash
        timestamp created_at
        timestamp updated_at
    }

    REGION {
        int region_id PK
        varchar name UK
        varchar emoji
        varchar description
        varchar badge
        varchar badge_type
        varchar bg_gradient
    }

    JOB_TYPE {
        int type_id PK
        varchar name UK
    }

    JOB {
        bigint job_id PK
        int region_id FK
        int type_id FK
        varchar name
        int salary
        text description
        varchar location_desc
        decimal latitude
        decimal longitude
        varchar emoji
        boolean is_best
        varchar price_label
        varchar unit
        varchar sub_description
        timestamp created_at
    }

    JOB_TAG {
        bigint tag_id PK
        bigint job_id FK
        varchar label
        int sort_order
    }

    ACCOMMODATION {
        bigint acc_id PK
        int region_id FK
        varchar name
        int price_per_month
        varchar location_desc
        varchar district
        decimal latitude
        decimal longitude
        varchar bg_gradient
        varchar color_hex
        int rank
        timestamp created_at
    }

    ACC_TAG {
        bigint tag_id PK
        bigint acc_id FK
        varchar label
        varchar tag_type
    }

    ACC_DISPOSABLE {
        bigint disp_id PK
        bigint acc_id FK
        bigint job_id FK
        int amount
        varchar display_text
    }

    PLAN {
        bigint plan_id PK
        bigint user_id FK
        int region_id FK
        bigint acc_id FK
        char stay_month
        int total_salary
        int accommodation_cost
        int food_cost
        int transport_cost
        int disposable_income
        enum status
        timestamp created_at
        timestamp updated_at
    }

    PLAN_JOB {
        bigint plan_job_id PK
        bigint plan_id FK
        bigint job_id FK
        boolean is_primary
        int sort_order
        timestamp added_at
    }

    PLAN_CALENDAR {
        bigint calendar_id PK
        bigint plan_id FK
        date work_date
        enum day_type
        varchar event_name
    }

    FESTIVAL {
        int festival_id PK
        int region_id FK
        varchar name
        date start_date
        date end_date
        varchar description
    }
```

## Application Login Table

The current Spring login flow uses the JPA entity table named `users`.

For local login testing, seed data inserts BCrypt password hashes into `users`.

```text
planner@samteo.local / 1234
admin@samteo.local   / 1234
```

The uppercase `USER` table exists because it is part of the drafted ERD schema.
