
# Pastebin Lite – Spring Boot Project

## 1. Project Overview

Pastebin Lite is a backend-focused web application similar to Pastebin. Users can create text pastes, optionally set an expiry time (TTL) and maximum view count, and access the paste using a unique URL. The project demonstrates real-world backend skills including REST APIs, database integration, exception handling, and cloud deployment.

Live URL: [https://pastebin-application.onrender.com]
(https://pastebin-application.onrender.com)

---

## 2. Tech Stack

* Language: Java 17
* Framework: Spring Boot 3.x
* Database: PostgreSQL (Neon DB)
* ORM: Spring Data JPA (Hibernate)
* Build Tool: Maven
* Deployment: Render (Docker-based)
* Frontend: Basic HTML, CSS, JavaScript

---

## 3. Features

* Create a paste using REST API
* Generate a unique paste URL
* Optional TTL (Time to Live)
* Optional maximum view limit
* View paste in browser using URL
* Expired or invalid pastes handled gracefully
* Health check endpoint for monitoring

---

## 4. Application Architecture

Controller Layer

* PasteApiController – Handles REST APIs (/api/pastes)
* PasteViewController – Handles browser view (/p/{id})

Service Layer

* PasteService – Business logic (create, validate, expiry, view count)

Repository Layer

* PasteRepository – JPA repository for database operations

Entity Layer

* Paste – Represents paste table in database

---

## 5. API Endpoints using Postman (with out deployment testing --NOTE--> We have add APP.prp--> Database url and username and password ) 

****** Core features you MUST implement Mentioned in Assignment; ******

{ A. Health Check
GET method
 https:/api/healthz

You Can see Response;;

return HTTP 200

return JSON

quickly check DB / persistence connectivity

Example:

{ "ok": true }

}

-------------------

 B. Create Paste API
POST 
http://localhost:8080/api/pastes



Request

{
  "content": "string",
  "ttl_seconds": 60,
  "max_views": 5
}



Response

{
  "id": "abc123",
  "url": "https://localhost:8080/p/{id}"
}



Success (200):

------------------------

copy the ID from Response

GET--> http://localhost:8080/api/pastes/{id}


{
  "content": "string",
  "remaining_views": 4,
  "expires_at": "2026-01-01T00:00:00.000Z"
}


Rules:

Each API fetch counts as a view

If expired OR views exceeded → 404 JSON

D. View Paste (HTML)
GET 

http://localhost:8080/p/{id}


Returns HTML page

Paste content must be escaped (u can see above api testing (Post method like Json Format))
If incase unavailable → 404 showing


------  WITH DEPLOYMENT --------

1. Health Check (MANDATORY)

Open in browser or Postman:

https://pastebin-application.onrender.com/api/healthz

Expected response (HTTP 200):
{ "ok": true }


If this works → ✅ Passes health check.

🔹 2. Create a Paste (API)

POST

https://pastebin-application.onrender.com/api/pastes


Body (JSON):

{
  "content": "Hello Aganitha ",
  "ttl_seconds": 60,
  "max_views": 2
}

Expected response:
{
  "id": "some-uuid",
  "url": "https://pastebin-application.onrender.com/p/<id>"
}


✅ Copy the url.

🔹 3. View Paste (HTML)

Paste the copied URL in browser:

https://pastebin-application.onrender.com/p/<id>


✅ You should see the text content rendered safely (no JSON).

🔹 4. Fetch Paste (API – counts views)

GET

https://pastebin-application.onrender.com/api/pastes/<id>

Response:
{
  "content": "Hello Aganitha 👋",
  "remaining_views": 1,
  "expires_at": "..."
}
### Health Check

GET /healthz

Response:
{
"status": "UP"
}

---

## 6. API Flow (Step-by-Step) ---> frontend

### Step 1: User Creates Paste

* User opens index.html
* Enters paste content, TTL, max views
* Clicks "Create Paste"

### Step 2: Frontend Sends Request

* JavaScript sends POST request to /api/pastes
* Payload is sent as JSON

### Step 3: Backend Processes Request

* Controller validates request
* Service generates UUID
* Expiry time calculated (if TTL provided)
* Paste saved in PostgreSQL database

### Step 4: Backend Responds

* Backend returns unique paste URL

### Step 5: User Opens Paste URL

* Browser hits /p/{id}
* PasteViewController fetches paste
* Validates expiry & view count

### Step 6: Paste Displayed

* Content rendered in paste.html
* View count incremented

### Step 7: Error Handling

* If paste expired or not found
* Custom exception triggers error.html

---

## 7. Deployment Details

* Application is Dockerized
* Deployed on Render Web Service
* PostgreSQL hosted on Neon
* Database credentials passed via environment variables

---

## 8. How to Run Locally

1. Clone repository
2. Configure application.properties with DB details
3. Run: mvn spring-boot:run
4. Open [http://localhost:8080](http://localhost:8080)

---

## 9. Author

Prasanna Kumar
Java Backend Developer

---

## 10.  Notes

This project demonstrates:

* REST API design
* Spring Boot best practices
* Database integration
* Cloud deployment
* Real-world backend problem solving
