# VCC - Vintage Car Club Management

The vintage car enthusiast app is a comprehensive tool for managing club memberships, scheduling events, and showcasing member-to-member vintage vehicle collections. Enthusiasts can create profiles, add multiple vehicles, and track memberships. The platform's unique showcase feature fosters a vibrant community where members share stories and photos of their vintage cars, creating a dynamic hub for enthusiasts to connect and celebrate their shared passion.

The application uses:
-  Spring Boot
-  Java 17
-  Maven
-  PostgreSQL
-  H2 in-memory database
-  MockMVC (for integration tests)
-  JUnit (for unit tests)
-  Mockito (to mock dependencies for unit tests)


## Database connection properties
- spring.datasource.url = jdbc:postgresql://localhost:5432/your_database
- spring.datasource.username=pass
- spring.datasource.password=pass
- spring.datasource.driver-class-name=org.postgresql.Driver

# JPA properties
- spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
- spring.jpa.open-in-view=false
- spring.jpa.hibernate.ddl-auto=update

# Thymeleaf
- spring.thymeleaf.prefix=classpath:/templates/
- spring.thymeleaf.suffix=.html



# The APIs -> Swagger

# I. Members
## 1. Create member
- Description: create a new member
- Endpoint: /api/members
- Method: POST
- Request body:
  
 ```yaml
{
   "firstName": "Arnold",
   "lastName": "Schwarzenegger",
   "email": "arnold.schwarzenegger@amazon.com",
   "gender": "M",
   "city": "Los Angeles"
}
```

## 2. Get all members
- Description: Retrieves the list of all member and their assigned cars
- Endpoint: /api/members
- Method: GET
- Request: /api/members
- Response:
```yaml
[
    {
        "memberId": 1,
        "firstName": "Julia",
        "lastName": "Roberts",
        "email": "juia.roberts@amazon.com",
        "gender": "F",
        "city": "New York",
        "vehicles": [
            {
                "licensePlate": "ERS-500",
                "make": "Mercedes",
                "model": "300SL",
                "year": 1985,
                "owners": null
            },
            {
                "licensePlate": "ERS-501",
                "make": "Ford",
                "model": "Mustang",
                "year": 1969,
                "owners": null
            }
        ]
    },
    {
        "memberId": 2,
        "firstName": "Robert Anthony",
        "lastName": "De Niro",
        "email": "de.niro@amazon.com",
        "gender": "M",
        "city": "New York",
        "vehicles": [
            {
                "licensePlate": "ERS-502",
                "make": "Dodge",
                "model": "Charger",
                "year": 1969,
                "owners": null
            },
            {
                "licensePlate": "ERS-503",
                "make": "Cadillac",
                "model": "Series 62",
                "year": 1959,
                "owners": null
            },
            {
                "licensePlate": "ERS-511",
                "make": "Rolls Royce",
                "model": "Silver Ghost Torpedo",
                "year": 1920,
                "owners": null
            }
        ]
    },
    {
        "memberId": 3,
        "firstName": "Barack",
        "lastName": "Obama",
        "email": "barack.obama@amazon.com",
        "gender": "M",
        "city": "Honolulu",
        "vehicles": [
            {
                "licensePlate": "ERS-504",
                "make": "Chevrolet",
                "model": "Impala",
                "year": 1960,
                "owners": null
            },
            {
                "licensePlate": "ERS-505",
                "make": "Bmw",
                "model": "1600",
                "year": 1968,
                "owners": null
            },
            {
                "licensePlate": "ERS-511",
                "make": "Rolls Royce",
                "model": "Silver Ghost Torpedo",
                "year": 1920,
                "owners": null
            }
        ]
    },
    {
        "memberId": 4,
        "firstName": "Will",
        "lastName": "Smith",
        "email": "will.smith@amazon.com",
        "gender": "M",
        "city": "Philadelphia",
        "vehicles": [
            {
                "licensePlate": "ERS-506",
                "make": "Chraysler",
                "model": "Royal",
                "year": 1957,
                "owners": null
            },
            {
                "licensePlate": "ERS-507",
                "make": "GMC",
                "model": "K 52",
                "year": 1925,
                "owners": null
            }
        ]
    },
    {
        "memberId": 5,
        "firstName": "Arnold",
        "lastName": "Schwarzenegger",
        "email": "arnold.schwarzenegger@amazon.com",
        "gender": "M",
        "city": "Los Angeles",
        "vehicles": [
            {
                "licensePlate": "ERS-508",
                "make": "GMC",
                "model": "100",
                "year": 1955,
                "owners": null
            },
            {
                "licensePlate": "ERS-509",
                "make": "Rolls Royce",
                "model": "Phantom Limousine",
                "year": 1920,
                "owners": null
            },
            {
                "licensePlate": "ERS-510",
                "make": "Falcon",
                "model": "Knight Roadster",
                "year": 1928,
                "owners": null
            },
            {
                "licensePlate": "ERS-511",
                "make": "Rolls Royce",
                "model": "Silver Ghost Torpedo",
                "year": 1920,
                "owners": null
            }
        ]
    }
]
```
## 3. Update member
Description: Update the details of a member by id
Endpoint: /api/members{id}
Method: PATCH
Request body:
```yaml
{
    "firstName": "Julia",
        "lastName": "Roberts",
        "email": "juia.roberts@amazon.com",
        "gender": "F",
        "city": "New York"
}
```

## 4. Get user by id
- Description: Retrieves a member details by id
- Endpoint: /api/members/{id}
Method: GET
Request: /api/members/3
Response:
```yaml
{
    "memberId": 3,
    "firstName": "Barack",
    "lastName": "Obama",
    "email": "barack.obama@amazon.com",
    "gender": "M",
    "city": "Honolulu",
    "vehicles": [
        {
            "licensePlate": "ERS-504",
            "make": "Chevrolet",
            "model": "Impala",
            "year": 1960,
            "owners": null
        },
        {
            "licensePlate": "ERS-505",
            "make": "Bmw",
            "model": "1600",
            "year": 1968,
            "owners": null
        },
        {
            "licensePlate": "ERS-511",
            "make": "Rolls Royce",
            "model": "Silver Ghost Torpedo",
            "year": 1920,
            "owners": null
        }
    ]
```
## 5. Delete member
- Description: Delete a member by id
- Endpoint: /api/members/{id}
- Method: DELETE
- Request: /api/members/5

## 6. Assign vehicle
- Description: Assign vehicle to member
- Endpoint: /api/members/{id}/vehicles/{licensePlate}
- Method: PUT
- Request: /api/members/3/vehicles/ERS-504

# II. Tasks

## 1. Create vechicle
Description: Create a new vehicle
Endpoint: /api/vehicles
Method: POST
Request: /api/vehicles
Request body:
```yaml
{
    "licensePlate": "ERS-511",
    "make": "Rolls Royce",
    "model": "Silver Ghost Torpedo",
    "year": 1920
}
```
## 2. Get all vehicles
- Description: Retrieves the list of all vehicles and their assigned members 
- Endpoint: /api/vehicles
- Method: GET
- Request: /api/vehicles
- Response:
```yaml
[
    {
        "licensePlate": "ERS-500",
        "make": "Mercedes",
        "model": "300SL",
        "year": 1985,
        "owners": [
            {
                "memberId": 1,
                "firstName": "Julia",
                "lastName": "Roberts",
                "email": "juia.roberts@amazon.com",
                "gender": "F",
                "city": "New York",
                "vehicles": null
            }
        ]
    },
    {
        "licensePlate": "ERS-501",
        "make": "Ford",
        "model": "Mustang",
        "year": 1969,
        "owners": [
            {
                "memberId": 1,
                "firstName": "Julia",
                "lastName": "Roberts",
                "email": "juia.roberts@amazon.com",
                "gender": "F",
                "city": "New York",
                "vehicles": null
            }
        ]
    },
    {
        "licensePlate": "ERS-502",
        "make": "Dodge",
        "model": "Charger",
        "year": 1969,
        "owners": [
            {
                "memberId": 2,
                "firstName": "Robert Anthony",
                "lastName": "De Niro",
                "email": "de.niro@amazon.com",
                "gender": "M",
                "city": "New York",
                "vehicles": null
            }
        ]
    },
    {
        "licensePlate": "ERS-503",
        "make": "Cadillac",
        "model": "Series 62",
        "year": 1959,
        "owners": [
            {
                "memberId": 2,
                "firstName": "Robert Anthony",
                "lastName": "De Niro",
                "email": "de.niro@amazon.com",
                "gender": "M",
                "city": "New York",
                "vehicles": null
            }
        ]
    },
    {
        "licensePlate": "ERS-504",
        "make": "Chevrolet",
        "model": "Impala",
        "year": 1960,
        "owners": [
            {
                "memberId": 3,
                "firstName": "Barack",
                "lastName": "Obama",
                "email": "barack.obama@amazon.com",
                "gender": "M",
                "city": "Honolulu",
                "vehicles": null
            }
        ]
    },
    {
        "licensePlate": "ERS-505",
        "make": "Bmw",
        "model": "1600",
        "year": 1968,
        "owners": [
            {
                "memberId": 3,
                "firstName": "Barack",
                "lastName": "Obama",
                "email": "barack.obama@amazon.com",
                "gender": "M",
                "city": "Honolulu",
                "vehicles": null
            }
        ]
    },
    {
        "licensePlate": "ERS-506",
        "make": "Chraysler",
        "model": "Royal",
        "year": 1957,
        "owners": [
            {
                "memberId": 4,
                "firstName": "Will",
                "lastName": "Smith",
                "email": "will.smith@amazon.com",
                "gender": "M",
                "city": "Philadelphia",
                "vehicles": null
            }
        ]
    },
    {
        "licensePlate": "ERS-507",
        "make": "GMC",
        "model": "K 52",
        "year": 1925,
        "owners": [
            {
                "memberId": 4,
                "firstName": "Will",
                "lastName": "Smith",
                "email": "will.smith@amazon.com",
                "gender": "M",
                "city": "Philadelphia",
                "vehicles": null
            }
        ]
    },
    {
        "licensePlate": "ERS-508",
        "make": "GMC",
        "model": "100",
        "year": 1955,
        "owners": [
            {
                "memberId": 5,
                "firstName": "Arnold",
                "lastName": "Schwarzenegger",
                "email": "arnold.schwarzenegger@amazon.com",
                "gender": "M",
                "city": "Los Angeles",
                "vehicles": null
            }
        ]
    },
    {
        "licensePlate": "ERS-509",
        "make": "Rolls Royce",
        "model": "Phantom Limousine",
        "year": 1920,
        "owners": [
            {
                "memberId": 5,
                "firstName": "Arnold",
                "lastName": "Schwarzenegger",
                "email": "arnold.schwarzenegger@amazon.com",
                "gender": "M",
                "city": "Los Angeles",
                "vehicles": null
            }
        ]
    },
    {
        "licensePlate": "ERS-510",
        "make": "Falcon",
        "model": "Knight Roadster",
        "year": 1928,
        "owners": [
            {
                "memberId": 5,
                "firstName": "Arnold",
                "lastName": "Schwarzenegger",
                "email": "arnold.schwarzenegger@amazon.com",
                "gender": "M",
                "city": "Los Angeles",
                "vehicles": null
            }
        ]
    },
    {
        "licensePlate": "ERS-511",
        "make": "Rolls Royce",
        "model": "Silver Ghost Torpedo",
        "year": 1920,
        "owners": [
            {
                "memberId": 5,
                "firstName": "Arnold",
                "lastName": "Schwarzenegger",
                "email": "arnold.schwarzenegger@amazon.com",
                "gender": "M",
                "city": "Los Angeles",
                "vehicles": null
            },
            {
                "memberId": 2,
                "firstName": "Robert Anthony",
                "lastName": "De Niro",
                "email": "de.niro@amazon.com",
                "gender": "M",
                "city": "New York",
                "vehicles": null
            },
            {
                "memberId": 3,
                "firstName": "Barack",
                "lastName": "Obama",
                "email": "barack.obama@amazon.com",
                "gender": "M",
                "city": "Honolulu",
                "vehicles": null
            }
        ]
    }
]
```
## 3. Get vehicle by license plate
- Description: Retrieves a vehicle details by license plate
- Endpoint: /api/vehicles/{licensePlate}
- Method: GET
- Request: /api/members/ERS-511
- Response:
```yaml
{
    "licensePlate": "ERS-511",
    "make": "Rolls Royce",
    "model": "Silver Ghost Torpedo",
    "year": 1920,
    "owners": null
}
```
## 4. Get vehicle by params
- Description: Retrieves a vehicle details by params
- Endpoint: /api/vehicles/byParams
- Method: GET
- Request: api/vehicles/byParams?licensePlate=ERS-510&make=Falcon&model=Knight Roadster&year=1928
- Response:
```yaml
[
    {
        "licensePlate": "ERS-510",
        "make": "Falcon",
        "model": "Knight Roadster",
        "year": 1928,
        "owners": null
    }
]
```
- Request: api/vehicles/byParams?make=Mercedes
- Response: 
```yaml
[
    {
        "licensePlate": "ERS-500",
        "make": "Mercedes",
        "model": "300SL",
        "year": 1985,
        "owners": null
    }
]
```
## 5. Update vehicles
Description: Update the details of a vehicle by license plate
Endpoint: /api/members{licensePlate}
Method: PATCH
Request body:
```yaml
{
    "licensePlate": "ERS-512",
    "make": "Porsche",
    "model": "911",
    "year": 1988
}
```
## 6. Delete vehicle
- Description: Delete a vehicle by license plate
- Endpoint: /api/members/{licensePlate}
- Method: DELETE
- Request: /api/members/ERS-510
