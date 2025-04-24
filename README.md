# Remitly Home Assignment 2025

![Java](https://img.shields.io/badge/Java-21-blue)  
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)  
![Spring](https://img.shields.io/badge/Spring-Boot-green)  
[![Docker](https://img.shields.io/badge/docker-ready-blue?logo=docker)](https://www.docker.com/)

## Overview

This Spring Boot application provides a REST API for handling four types of requests that interact with a dataset.  
The dataset includes the following fields: `SwiftCode`, `CountryISOCode`, `Name`, `Address`, and `CountryName`.

### Example Dataset

| **SwiftCode** | **CountryISOCode** | **Name**                     | **Address**                                          | **CountryName** |
|---------------|---------------------|------------------------------|-----------------------------------------------------|-----------------|
| AAISALTRXXX   | AL                  | UNITED BANK OF ALBANIA SH.A | HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023 | ALBANIA         |

### Database Transformation

The initial dataset was transformed into a PostgreSQL database with additional columns:
- `is_headquarters` (boolean): Indicates if the SWIFT code belongs to a headquarters.
- `parent_swift_code` (string): References the parent SWIFT code for branches.

### Transformed Dataset Example

| **SwiftCode** | **CountryISOCode** | **Name**                     | **Address**                                          | **CountryName** | **is_headquarters** | **parent_swift_code** |
|---------------|---------------------|------------------------------|-----------------------------------------------------|-----------------|----------------------|-----------------------|
| AIZKLV22CLN   | LV                  | ABLV BANK, AS IN LIQUIDATION | ELIZABETES STREET 23  RIGA, RIGA, LV-1010          | LATVIA          | false               | AIZKLV22XXX           |
## REST API Endpoints

### 1. Retrieve details of a single SWIFT code
**GET** `/v1/swift-codes/{swift-code}`  
Returns details of a specific SWIFT code. If the SWIFT code belongs to a headquarters, it includes a list of branches.

**Response for Headquarter SWIFT Code:**
```json
{
    "address": "string",
    "bankName": "string",
    "countryISO2": "string",
    "countryName": "string",
    "isHeadquarter": true,
    "swiftCode": "string",
    "branches": [
        {
            "address": "string",
            "bankName": "string",
            "countryISO2": "string",
            "isHeadquarter": false,
            "swiftCode": "string"
        }
    ]
}
```
**Response for Branch SWIFT Code:**
```json
{
    "address": "string",
    "bankName": "string",
    "countryISO2": "string",
    "countryName": "string",
    "isHeadquarter": false,
    "swiftCode": "string"
}
```
### 2.Retrieve all SWIFT codes for a specific country
**GET** `/v1/swift-codes/country/{countryISO2code}`
Returns all SWIFT codes (headquarters and branches) for a given country.
**Response**
```json
{
  "countryISO2": "string",
  "countryName": "string",
  "swiftCodes": [
    {
      "address": "string",
      "bankName": "string",
      "countryISO2": "string",
      "isHeadquarter": true,
      "swiftCode": "string"
    }
  ]
}
```
### 3. Add a new SWIFT code
**POST**  `/v1/swift-codes`
Adds a new SWIFT code entry to the database.
**Request**
```json
{
  "address": "string",
  "bankName": "string",
  "countryISO2": "string",
  "countryName": "string",
  "isHeadquarter": true,
  "swiftCode": "string"
}
```
**Response**
```
{
    "message": "string"
}
```

### 4. Delete a SWIFT code
**DELETE**  `/v1/swift-codes/{swift-code}`
Deletes a SWIFT code entry from the database.
**Response**
```
{
    "message": "string"
}
```

### Installation Guide

#### Prerequisites
1. Install **Docker** and **Docker Compose**.

#### Running with Docker
1. Clone the repository:
   ```bash
   git clone https://github.com/bzabk/RemitlyInternTask2025.git
   cd RemitlyInternTask2025
2. Build and start the containers:
   ```bash
   docker-compose up --build
3. The application will be available at: http://localhost:8080