# Nearby Restaurants Finder
A mobile application that helps users discover nearby restaurants using Google Maps services.

The application retrieves restaurant information, calculates real driving/walking distances, filters results based on user preferences, and sorts restaurants by relevance.

The project focuses on asynchronous programming, efficient API communication, and responsive user experience.
## Tech Stack

- Kotlin
- Android SDK
- Kotlin Coroutines
- Retrofit
- Google Places API
- Google Directions API
- Picasso
- MVVM Architecture

# Architecture

The application is composed of two main components:

- **Android Client** - mobile application responsible for user interaction and displaying restaurant information.
- **Google Maps Services** - external APIs used for retrieving restaurants, calculating routes, and downloading images.


<p align="center">
<img width="533" height="201" alt="restaurant_architecture" src="https://github.com/user-attachments/assets/c09ad032-d755-4fdb-b0a9-a268122809d3" />
</p>

The application communicates with three Google services:

- **Google Places API** - retrieves nearby restaurants and metadata.
- **Google Directions API** - calculates real travel distances.
- **Places Photo API** - provides restaurant images.

---

# Design Decisions

## Restaurant Search

Restaurant data is retrieved using the Google Places API.

The returned JSON contains various information, from which the application extracts:

- Restaurant name
- Rating
- Coordinates
- Image reference

The API returns results using pagination, therefore pagination tokens are handled to retrieve additional restaurants when available.

---

## Distance Calculation

The distance initially provided by Google Places represents the geometric distance between two points.

However, this does not represent the real distance a user needs to travel due to roads, buildings, and traffic restrictions.

To obtain accurate results, the application uses the Google Directions API.

The API receives:

- Starting location
- Destination coordinates
- Travel mode (car, bicycle, walking)

Only the calculated travel distance is used by the application.

<p align="center">
<img width="493" height="225" alt="distance_update" src="https://github.com/user-attachments/assets/1bcdc698-9901-4e51-95c6-e9486998b0e0" />
</p>

---

## Asynchronous Distance Updates

Calculating distances sequentially would require waiting for each API response before starting the next request.

To improve performance, each restaurant distance calculation is executed independently using Kotlin coroutines.

Benefits:

- Multiple requests are processed concurrently.
- UI remains responsive.
- Total execution time is reduced to approximately the slowest API response.

`coroutineScope` ensures that filtering and sorting only begin after all distance calculations are completed, preventing inconsistent results.
<p align="center">
<img width="503" height="119" alt="sequential_coroutine" src="https://github.com/user-attachments/assets/488d6894-9959-41ac-88da-0128f6ee95e7" />
</p>
<p align="center">
<img width="475" height="200" alt="parallel_coroutine" src="https://github.com/user-attachments/assets/a49fd49d-bf67-4e6c-a5c5-83dace8a1abf" />
</p>


---

## Data Transformation

Google APIs return complex DTO structures containing many unused fields.

To avoid coupling the application logic and UI with external API models, a conversion layer is used:

```
Google API DTO
      |
      ▼
Converter
      |
      ▼
Application Model
      |
      ▼
UI
```

This approach improves maintainability and keeps the application independent from external API changes.

---

## Filtering and Sorting

After updating distances:

- Restaurants outside the selected range are removed.
- Remaining restaurants are sorted by Google Maps rating.

The filtering and sorting operations are executed asynchronously to avoid blocking the main thread.

---

## Image Loading

Restaurant images are loaded using the Picasso framework.

The image reference received from Google Places is converted into a valid image request and downloaded asynchronously.

---

# Demo

<img src="..." width="400"/>
