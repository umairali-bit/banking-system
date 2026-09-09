# Banking System

A production-oriented banking backend built with **Java 21** and
**Spring Boot**. The project goes beyond CRUD to demonstrate enterprise
backend concepts including domain modeling, transactions, concurrency
control, authentication and authorization, auditing, AOP, observability,
asynchronous email notifications, database migrations, Docker, and
CI/CD.

> **Current scope:** Backend REST API. A frontend is not currently part
> of the application.

------------------------------------------------------------------------

## Table of Contents

-   [Overview](#overview)
-   [Technology Stack](#technology-stack)
-   [Architecture](#architecture)
-   [Domain Model](#domain-model)
-   [Package Structure](#package-structure)
-   [Customer Management](#customer-management)
-   [Account Management](#account-management)
-   [Transactions and Currency
    Conversion](#transactions-and-currency-conversion)
-   [Concurrency and Transaction
    Safety](#concurrency-and-transaction-safety)
-   [Security](#security)
-   [JWT Authentication](#jwt-authentication)
-   [OAuth2](#oauth2)
-   [Roles and Authorization](#roles-and-authorization)
-   [Auditing](#auditing)
-   [AOP](#aop)
-   [Notifications](#notifications)
-   [Monitoring](#monitoring)
-   [Exception Handling](#exception-handling)
-   [Validation](#validation)
-   [Database and Flyway](#database-and-flyway)
-   [Docker](#docker)
-   [CI/CD](#cicd)
-   [Local Development](#local-development)
-   [Configuration and Secrets](#configuration-and-secrets)
-   [Testing](#testing)
-   [Sprint Roadmap](#sprint-roadmap)
-   [Design Decisions](#design-decisions)
-   [Future Improvements](#future-improvements)

------------------------------------------------------------------------

# Overview

The Banking System is a Spring Boot REST API designed to model common
banking operations while emphasizing correctness, security,
maintainability, and production readiness.

Major capabilities include:

-   Customer management
-   Savings, checking, and business accounts
-   Account lifecycle management
-   Deposits, withdrawals, and transfers
-   Multi-currency transfers
-   Transaction history
-   Concurrency-safe account updates
-   Spring Security
-   JWT access and refresh tokens
-   OAuth2/OpenID Connect login
-   Role-based and ownership-based authorization
-   JPA and business auditing
-   Aspect-oriented cross-cutting concerns
-   Email notifications
-   Password reset
-   Spring Boot Actuator
-   Micrometer custom metrics
-   Prometheus integration
-   PostgreSQL
-   Flyway migrations
-   Docker and Docker Compose
-   GitHub Actions CI/CD preparation

------------------------------------------------------------------------

# Technology Stack

  Area                     Technology
  ------------------------ -----------------------------------------------
  Language                 Java 21
  Framework                Spring Boot
  REST API                 Spring Web
  Persistence              Spring Data JPA / Hibernate
  Database                 PostgreSQL
  Schema Migration         Flyway
  Validation               Jakarta Bean Validation
  Security                 Spring Security
  API Authentication       JWT
  Social Login             OAuth2 / OpenID Connect
  Password Hashing         BCrypt
  Auditing                 Spring Data JPA Auditing + custom audit trail
  Cross-Cutting Concerns   Spring AOP
  Email                    Spring Mail / JavaMailSender
  Async Processing         Spring `@Async`
  Monitoring               Spring Boot Actuator
  Metrics                  Micrometer
  Metrics Collection       Prometheus
  API Documentation        OpenAPI / Swagger
  Build                    Maven
  Containerization         Docker / Docker Compose
  CI                       GitHub Actions
  Boilerplate Reduction    Lombok

------------------------------------------------------------------------

# Architecture

The project uses a **feature-based layered architecture**.

``` text
                           ┌───────────────────────┐
                           │        Client         │
                           │ Postman / Frontend    │
                           └───────────┬───────────┘
                                       │ HTTP
                                       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         CONTROLLER LAYER                            │
│ Customer │ Account │ Transaction │ Authentication                  │
└───────────────────────────────┬─────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         VALIDATION LAYER                            │
│ Bean Validation │ Custom Validators │ Business Input Validation    │
└───────────────────────────────┬─────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                          SERVICE LAYER                              │
│ Customer │ Account │ Transaction │ Currency │ Security │ Audit     │
│ Notification │ Token │ Authorization                              │
└──────────────┬──────────────────────────────┬───────────────────────┘
               │                              │
               ▼                              ▼
┌──────────────────────────┐       ┌──────────────────────────────┐
│     REPOSITORY LAYER     │       │      INTEGRATION LAYER      │
│ Spring Data JPA          │       │ Currency API │ SMTP │ OAuth │
└─────────────┬────────────┘       └──────────────────────────────┘
              │
              ▼
┌──────────────────────────┐
│       PostgreSQL         │
└──────────────────────────┘
```

Cross-cutting concerns span the application:

``` text
Security │ Exception Handling │ AOP │ Audit │ Logging │ Metrics │ Email
```

## Typical Secured Request Flow

``` text
HTTP Request
    │
    ▼
JWT Authentication Filter
    │
    ▼
Spring Security
    │
    ▼
Controller
    │
    ▼
Bean Validation
    │
    ▼
Service
    │
    ├── Authorization / ownership checks
    ├── Business rules
    ├── Transaction boundary
    ├── Audit / AOP
    ├── Metrics
    └── Notification trigger
    │
    ▼
Repository
    │
    ▼
PostgreSQL
```

------------------------------------------------------------------------

# Domain Model

## Customer → Account

A customer can own multiple accounts.

``` text
Customer
   │ 1
   │
   │ *
   ▼
Account (abstract)
   │
   ├── SavingsAccount
   ├── CheckingAccount
   └── BusinessAccount
```

The abstract `Account` contains shared state such as:

``` text
id
accountNumber
accountType
customer
balance
currency
status
version
createdAt
updatedAt
```

Subtype-specific data remains in the subclasses:

``` text
SavingsAccount
└── interestRate

CheckingAccount
└── overdraftLimit

BusinessAccount
└── business-specific account data
```

The project uses JPA inheritance so common account behavior is
centralized without duplicating shared fields.

## Transaction Model

``` text
                   Transaction
                  /           \
                 /             \
        sourceAccount     destinationAccount
```

A transaction can preserve:

``` text
transactionReference
sourceAccount
destinationAccount
sourceAmount
destinationAmount
sourceCurrency
destinationCurrency
type
status
createdAt
```

------------------------------------------------------------------------

# Package Structure

The codebase is grouped by feature.

``` text
com.umair.banking
│
├── account
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── enums
│   ├── repository
│   └── service
│
├── customer
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── transaction
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── enums
│   ├── repository
│   └── service
│
├── currency
│   ├── client
│   ├── config
│   ├── dto
│   └── service
│
├── security
│   ├── config
│   ├── jwt
│   ├── oauth2
│   ├── token
│   ├── authorization
│   └── service
│
├── audit
│   ├── entity
│   ├── enums
│   ├── repository
│   └── service
│
├── aop
│   ├── annotation
│   └── aspect
│
├── notification
│   ├── dto
│   └── service
│
├── monitoring
├── exception
├── validation
├── config
├── common
└── BankingApplication.java
```

Feature-based packaging keeps related controllers, services,
repositories, entities, and DTOs close together instead of creating one
giant application-wide controller or service package.

------------------------------------------------------------------------

# Customer Management

The customer module supports:

-   Create customer
-   Get customer by ID
-   Get all customers
-   Full update
-   Partial update
-   Delete customer
-   Generated customer numbers
-   Duplicate email protection
-   Duplicate phone protection
-   DTO-based API responses

The public service API returns DTOs instead of exposing JPA entities.

``` text
CustomerRequest
      │
      ▼
CustomerService
      │
      ▼
Customer Entity
      │
      ▼
CustomerRepository
      │
      ▼
CustomerResponse
```

------------------------------------------------------------------------

# Account Management

Supported account types:

``` text
Account
├── SavingsAccount
├── CheckingAccount
└── BusinessAccount
```

Account features include:

-   Generated account numbers
-   Account ownership
-   Account currency
-   Opening balance validation
-   Account status
-   Savings interest rate
-   Checking overdraft support
-   Business-account support

## Account Lifecycle

``` text
ACTIVE
  │
  ├──► FROZEN ─────► ACTIVE
  │
  ├──► SUSPENDED ──► ACTIVE
  │
  └──► CLOSED
```

Financial operations verify account state before modifying balances.

------------------------------------------------------------------------

# Transactions and Currency Conversion

The transaction module implements the core banking operations.

## Deposit

``` text
Request
  ↓
Validate Account
  ↓
Lock Account
  ↓
Increase Balance
  ↓
Create Transaction
  ↓
Commit
```

## Withdrawal

``` text
Request
  ↓
Validate Account
  ↓
Lock Account
  ↓
Validate Funds / Overdraft Rules
  ↓
Decrease Balance
  ↓
Create Transaction
  ↓
Commit
```

## Transfer

``` text
Transfer Request
      │
      ▼
Load + Lock Accounts
      │
      ▼
Validate Accounts / Amount
      │
      ▼
Check Currency
      │
      ├── Same Currency ────────────┐
      │                             │
      └── Different Currency        │
                │                   │
                ▼                   │
       CurrencyConversionService    │
                │                   │
                └─────────────┬─────┘
                              ▼
                     Debit Source
                              │
                              ▼
                   Credit Destination
                              │
                              ▼
                    Save Transaction
                              │
                              ▼
                            Commit
```

The existing currency module is integrated into transfer processing
rather than being treated as an isolated feature.

Supported account currencies include:

-   USD
-   MYR
-   PKR

For cross-currency transfers, source and destination amounts/currencies
are stored so the financial event remains traceable.

------------------------------------------------------------------------

# Concurrency and Transaction Safety

Money movement must remain correct when multiple requests reach the same
account at the same time.

The project uses:

-   `@Transactional`
-   `READ_COMMITTED`
-   `REPEATABLE_READ`
-   `SERIALIZABLE`
-   Optimistic locking with `@Version`
-   Pessimistic locking with `PESSIMISTIC_WRITE`

``` text
Request A ──┐
            ├──► Same Account Balance
Request B ──┘
```

These mechanisms help prevent:

-   Lost updates
-   Double withdrawals
-   Balance corruption
-   Inconsistent transfers

The account model includes a version field for optimistic locking, while
account-update repository operations can acquire pessimistic write locks
when appropriate.

------------------------------------------------------------------------

# Security

Authentication is separated from banking-domain data.

``` text
User
├── authentication identity
├── password
├── enabled
└── roles

Customer
├── customer number
├── contact information
└── accounts
```

This prevents the customer entity from becoming responsible for both
banking data and authentication infrastructure.

------------------------------------------------------------------------

# JWT Authentication

JWT provides stateless API authentication.

## Login Flow

``` text
Login Request
      │
      ▼
AuthenticationManager
      │
      ▼
Validate Credentials
      │
      ▼
Generate Tokens
      │
      ├── Access Token
      └── Refresh Token
      │
      ▼
Persist Token Metadata
```

## Authenticated Request

``` text
Authorization: Bearer <access-token>
              │
              ▼
JwtAuthenticationFilter
              │
              ▼
Validate JWT + Token Store
              │
              ▼
SecurityContext
              │
              ▼
Controller / @PreAuthorize
```

JWT capabilities include:

-   Access tokens
-   Refresh tokens
-   Token-type claims
-   Token persistence
-   Token revocation
-   Logout
-   Revoking active user tokens

JWT signing secrets must be supplied securely and never committed to
source control.

------------------------------------------------------------------------

# OAuth2

Google OAuth2/OpenID Connect login is supported.

``` text
Google Login
    │
    ▼
Spring Security OAuth2
    │
    ▼
OIDC User
    │
    ▼
Resolve Local User
    │
    ▼
Generate Access + Refresh JWT
    │
    ▼
Persist Tokens
    │
    ▼
Authentication Result
```

OAuth2 provides an additional login mechanism while JWT continues to
secure subsequent API requests.

------------------------------------------------------------------------

# Roles and Authorization

The role model includes:

``` text
ADMIN
MANAGER
EMPLOYEE
CUSTOMER
```

Method security uses rules such as `@PreAuthorize`.

### CUSTOMER

Typical access:

-   View own profile
-   View own accounts
-   Operate on owned accounts where allowed
-   View own transactions

### EMPLOYEE

Typical access:

-   Customer servicing
-   Authorized account operations

### MANAGER

Typical access:

-   Elevated operational permissions
-   Management-level functions

### ADMIN

Typical access:

-   User and role administration
-   System-level operations
-   Audit and monitoring administration where permitted

Ownership checks are also enforced. A customer cannot obtain access to
another customer's resources merely by changing a URL ID.

------------------------------------------------------------------------

# Auditing

The project uses both persistence auditing and a business audit trail.

## JPA Auditing

Shared auditable entities can track:

``` text
createdAt
updatedAt
```

using annotations such as:

``` java
@CreatedDate
@LastModifiedDate
```

## Business Audit Trail

Important banking events are persisted to `AuditLog`.

Examples:

``` text
CUSTOMER_CREATED
ACCOUNT_CREATED
ACCOUNT_FROZEN
ACCOUNT_UNFROZEN
```

Audit records contain data such as:

``` text
id
userId
action
entityType
entityId
details
createdAt
```

------------------------------------------------------------------------

# AOP

Spring AOP separates cross-cutting behavior from core banking logic.

## Audit Aspect

A custom annotation:

``` java
@Auditable(...)
```

can mark operations that should generate an audit event. `AuditAspect`
intercepts successful execution and delegates persistence to
`AuditService`.

## Performance Aspect

A custom annotation such as:

``` java
@LogExecutionTime
```

measures service execution time without embedding timing code into the
business method.

AOP is intentionally **not** used to implement deposits, withdrawals,
transfers, or balance validation. Financial business rules remain
explicit in services.

------------------------------------------------------------------------

# Notifications

The notification module uses Spring Mail / `JavaMailSender`.

Email scenarios include:

-   Welcome email
-   Account creation email
-   Deposit confirmation
-   Withdrawal confirmation
-   Transfer confirmation
-   Password reset email

Notifications use asynchronous execution where appropriate:

``` text
Banking Operation
      │
      ▼
Core Work Completes
      │
      ├────────► API Response
      │
      └────────► @Async Email
```

## Password Reset

``` text
Forgot Password
      ↓
Generate Reset Token
      ↓
Send Email
      ↓
Submit Token + New Password
      ↓
Validate Token / Expiration
      ↓
Update Password
```

Reset tokens expire after a limited period.

------------------------------------------------------------------------

# Monitoring

Spring Boot Actuator provides operational endpoints including:

``` text
/actuator/health
/actuator/info
/actuator/metrics
```

Health information includes areas such as:

-   Database
-   Mail
-   Disk space
-   Liveness/readiness

## Custom Metrics

Micrometer is used for application-specific metrics.

A custom transaction metric is:

``` text
banking.transactions.total
```

It can distinguish transaction type and result.

Conceptually:

``` text
banking_transactions_total{type="DEPOSIT",result="SUCCESS"}
banking_transactions_total{type="TRANSFER",result="FAILED"}
```

## Prometheus

``` text
Banking Application
      ↓
Micrometer
      ↓
/actuator/prometheus
      ↓
Prometheus
```

The application exposes metrics while Prometheus scrapes and stores the
time series independently.

------------------------------------------------------------------------

# Exception Handling

The application uses centralized exception handling.

``` text
Service
  ↓
Custom Exception
  ↓
GlobalExceptionHandler
  ↓
ApiErrorResponse
```

Handled error categories include:

-   Customer not found
-   Account not found
-   Duplicate email
-   Duplicate phone
-   Invalid account state
-   Insufficient funds
-   Validation errors
-   Expired tokens
-   Authentication/authorization failures
-   Other application errors

A standardized response can look like:

``` json
{
  "timestamp": "2026-01-01T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with id: 10",
  "path": "/api/customers/10"
}
```

------------------------------------------------------------------------

# Validation

Validation occurs at multiple layers.

## DTO Validation

Jakarta Bean Validation:

``` text
@NotBlank
@NotNull
@Email
@Positive
@Size
```

## Custom Validation

Custom annotations and `ConstraintValidator` implementations handle
domain-specific validation such as opening-balance rules.

## Service Validation

Rules that depend on current database state stay in the service layer:

-   Duplicate email
-   Duplicate phone
-   Ownership
-   Account state
-   Sufficient funds
-   Same-account transfer prevention

------------------------------------------------------------------------

# Database and Flyway

PostgreSQL is the primary database.

Flyway owns production-style schema evolution.

``` text
src/main/resources
└── db
    └── migration
        └── V1__initial_schema.sql
```

Hibernate validates the migrated schema rather than owning production
schema creation.

``` yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

The initial migration can also seed required security roles and
administrative data.

------------------------------------------------------------------------

# Configuration Profiles

Configuration is separated by environment.

``` text
application.yml
application-dev.yml
application-prod.yml
```

Typical activation:

``` bash
SPRING_PROFILES_ACTIVE=dev
```

or:

``` bash
SPRING_PROFILES_ACTIVE=prod
```

Production configuration should obtain credentials and secrets from the
environment rather than source-controlled YAML.

------------------------------------------------------------------------

# Docker

The project includes a Dockerfile for packaging the Spring Boot
application.

``` text
Source
  ↓
Maven
  ↓
JAR
  ↓
Docker Image
  ↓
Container
```

## Docker Compose

Docker Compose runs the application with PostgreSQL.

``` text
Docker Compose
├── PostgreSQL
└── Banking Application
```

Build and start:

``` bash
docker compose up --build
```

Stop:

``` bash
docker compose down
```

Reset local Compose volumes only when intentionally discarding local
database data:

``` bash
docker compose down -v
```

------------------------------------------------------------------------

# CI/CD

GitHub Actions provides continuous integration for pushes and pull
requests targeting `main`.

Conceptual pipeline:

``` text
Push / Pull Request
        ↓
GitHub Actions
        ↓
Checkout
        ↓
Set Up Java
        ↓
Maven Build
        ↓
Tests
        ↓
Docker Image Build
        ↓
Container Registry
        ↓
Deployment Stage
```

The repository permissions and workflow can support publishing the
banking-system image to a package/container registry.

------------------------------------------------------------------------

# Local Development

## Prerequisites

-   Java 21
-   Maven
-   PostgreSQL
-   Git
-   Docker Desktop / Docker Engine
-   Postman or another API client

Check Java:

``` bash
java -version
```

Check Maven:

``` bash
mvn -version
```

## Clone

``` bash
git clone <repository-url>
cd banking-system
```

## Build

``` bash
mvn clean package
```

The JAR is produced under:

``` text
target/
```

## Run with Maven

``` bash
mvn spring-boot:run
```

## Run the JAR

``` bash
java -jar target/<application-jar>.jar
```

## Run with Docker Compose

``` bash
docker compose up --build
```

------------------------------------------------------------------------

# Configuration and Secrets

The exact environment-variable names should match the repository
configuration. Production deployments require secret configuration for
categories including:

``` text
PostgreSQL URL
PostgreSQL username
PostgreSQL password

JWT signing secret

Google OAuth client ID
Google OAuth client secret

SMTP username
SMTP password

Currency API credentials
```

Do **not** commit:

``` text
.env
production passwords
JWT secrets
OAuth client secrets
SMTP credentials
external API keys
```

A safe `.env.example` may contain variable names without real values.

------------------------------------------------------------------------

# Testing

The project architecture supports several testing levels.

## Unit Tests

-   Services
-   Validators
-   Token logic
-   Currency calculations
-   Business rules

## Repository Tests

-   JPA queries
-   Relationships
-   Uniqueness
-   Locking queries

## Controller Tests

Using MockMvc:

-   HTTP status codes
-   Request validation
-   JSON responses
-   Exception responses
-   Authorization

## Security Tests

-   Unauthenticated access
-   Invalid JWT
-   Expired/revoked tokens
-   Role restrictions
-   Ownership restrictions

## Transaction and Concurrency Tests

-   Rollback
-   Concurrent withdrawal
-   Concurrent transfer
-   Balance consistency
-   Lock behavior

------------------------------------------------------------------------

# Sprint Roadmap

The application was developed incrementally.

## Sprint 5 --- Exception Handling + Business Account + Duplicate Checks

-   Global exception handling
-   Standard API errors
-   Business account
-   Duplicate customer/account checks

## Sprint 6 --- Account Lifecycle & Business Rules

-   Activate
-   Suspend
-   Freeze
-   Unfreeze
-   Close
-   Account-state validation

## Sprint 7 --- Transactions

-   Deposit
-   Withdrawal
-   Transfer
-   History
-   Existing currency module integrated into transfers

## Sprint 8 --- Concurrency & Transaction Safety

-   `@Transactional`
-   Isolation levels
-   Optimistic locking
-   Pessimistic locking
-   Race-condition protection

## Sprint 9 --- Spring Security Foundation

-   Users
-   Roles
-   `ADMIN`
-   `MANAGER`
-   `EMPLOYEE`
-   `CUSTOMER`
-   Authentication foundation
-   Authorization

## Sprint 10 --- JWT Authentication

-   Access tokens
-   Refresh tokens
-   JWT filter
-   Token persistence
-   Revocation
-   Logout

## Sprint 11 --- OAuth2

-   Google OAuth2/OIDC
-   Local user resolution
-   JWT issuance after OAuth login

## Sprint 12 --- Audit

-   JPA auditing
-   Business audit trail
-   User/action/entity tracking

## Sprint 13 --- AOP

-   `@Auditable`
-   Audit aspect
-   Execution-time aspect
-   Cross-cutting concerns

## Sprint 14 --- Actuator & Monitoring

-   Health
-   Info
-   Metrics
-   Micrometer
-   Prometheus
-   Custom transaction metrics

## Sprint 15 --- Notifications

-   Welcome email
-   Account email
-   Deposit email
-   Withdrawal email
-   Transfer email
-   Password reset
-   Async email delivery

## Sprint 16 --- Testing

-   Unit
-   Integration
-   MockMvc
-   Security
-   Transactions
-   Concurrency

## Sprint 17 --- Production Readiness & Deployment

-   Flyway
-   Profiles
-   Docker
-   Docker Compose
-   Environment variables
-   OpenAPI
-   GitHub Actions
-   Container publishing
-   Cloud deployment preparation

------------------------------------------------------------------------

# Design Decisions

## DTOs Instead of Exposing Entities

``` text
Request DTO
    ↓
Service
    ↓
Entity
    ↓
Repository
    ↓
Response DTO
```

This protects persistence internals and keeps API contracts independent
of JPA entities.

## Feature-Based Packaging

Instead of one global `controller`, `service`, and `repository` package,
the project groups code by capability:

``` text
customer
account
transaction
security
audit
notification
```

## Separate Customer and User Models

`Customer` represents the banking customer.

`User` represents authentication and authorization.

## Avoid Circular Service Dependencies

Services should not depend on each other bidirectionally. Focused
repository/domain dependencies are preferred.

## AOP Only for Cross-Cutting Concerns

AOP handles auditing and performance measurement---not deposits,
withdrawals, transfers, or balance validation.

## Currency Conversion Belongs to Transfer Processing

The currency module is reusable infrastructure invoked by transaction
logic when source and destination currencies differ.

## Concurrency Is Part of Correctness

Locking and transaction isolation are treated as financial correctness
requirements rather than performance features.

------------------------------------------------------------------------

# Future Improvements

Possible extensions:

-   Frontend application
-   Redis caching
-   Kafka or RabbitMQ
-   Transactional outbox for reliable notifications
-   Idempotency keys for money movement
-   Rate limiting
-   Distributed tracing
-   Grafana dashboards
-   MFA
-   Login/device history
-   More granular permissions
-   Scheduled interest calculation
-   Statements
-   Card management
-   Loan module
-   Branch management
-   Kubernetes
-   Cloud secret manager integration

------------------------------------------------------------------------

# Project Status

The project demonstrates the progression from domain modeling to
production-oriented backend engineering:

``` text
Domain Modeling
      ↓
Validation
      ↓
Exception Handling
      ↓
Transactions
      ↓
Concurrency
      ↓
Spring Security
      ↓
JWT / OAuth2
      ↓
Audit / AOP
      ↓
Notifications
      ↓
Actuator / Prometheus
      ↓
Flyway / Docker
      ↓
CI/CD / Deployment
```

It is intentionally more than a CRUD application: the project focuses on
financial consistency, authorization, auditability, observability, and
deployment concerns expected in a serious backend system.

------------------------------------------------------------------------

## Author

**Umair Ali**

Java/Spring backend and AI-focused software engineer.
