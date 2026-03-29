# BookMyShow - Low Level Design

## Overview
This is a complete low-level design implementation of a movie/event booking platform similar to BookMyShow. The system handles movie selection, theater management, seat selection, booking, and payment processing.

## Design Patterns Used

### 1. **Singleton Pattern**
- **BookMyShow** class — ensures only one instance of the platform exists across the system
- Manages all theaters, shows, bookings, and users centrally

### 2. **Strategy Pattern**
- **PricingStrategy** interface with implementations:
  - `WeekdayPricing` — 10% discount on weekdays
  - `WeekendPricing` — 20% premium on weekends
- Different pricing strategies can be applied to different show timings

### 3. **State Pattern** (Implicit)
- **SeatStatus** — represents states of a seat (AVAILABLE, BOOKED, RESERVED, BLOCKED)
- **BookingStatus** — represents booking lifecycle (PENDING, CONFIRMED, CANCELLED, EXPIRED)
- **ShowStatus** — represents show status (COMING_SOON, NOW_SHOWING, ENDED)
- **PaymentStatus** — represents payment lifecycle (PENDING, SUCCESS, FAILED, REFUNDED)

## Architecture

### Core Entities

```
BookMyShow (Singleton)
├── Theater
│   └── Screen
│       └── Seat
├── Show
│   ├── ShowStatus
│   └── ShowTiming
│       ├── PricingStrategy
│       └── Screen
├── User
├── Booking
│   ├── List<Seat>
│   ├── BookingStatus
│   └── Payment
└── Payment
    └── PaymentStatus
```

### Key Classes

1. **User** — Represents a user who can book tickets
   - userId, name, email, phoneNumber

2. **Theater** — A movie theater in a city
   - Multiple screens
   - Location-based queries

3. **Screen** — A hall in a theater
   - Contains grid of seats
   - Multiple seat types (STANDARD, PREMIUM, RECLINER)

4. **Seat** — Individual seat in a screen
   - Has row, column, type, and status
   - Can transition between different statuses

5. **Show** — A movie being screened
   - Metadata: name, language, genre, duration
   - Status tracking (COMING_SOON, NOW_SHOWING, ENDED)

6. **ShowTiming** — A specific showing of a movie
   - Combines: Show + Theater + Screen + Time
   - Has associated pricing strategy

7. **Booking** — A user's ticket reservation
   - Contains list of booked seats
   - Handles seat locking/confirmation
   - Manages price calculation based on strategy

8. **Payment** — Payment information
   - Amount, method (CREDIT_CARD, UPI, etc.)
   - Status tracking

### SOLID Principles Applied

- **Single Responsibility**: Each class has one clear purpose
  - Booking handles reservations
  - Payment handles transactions
  - Theater manages screens and locations

- **Open/Closed**: Open for extension, closed for modification
  - New pricing strategies can be added without changing existing code
  - New seat types can be defined via enum

- **Liskov Substitution**: Any PricingStrategy implementation can be used interchangeably

- **Interface Segregation**: 
  - PricingStrategy interface is focused on single responsibility

- **Dependency Inversion**: 
  - ShowTiming depends on PricingStrategy abstraction, not concrete implementations
  - BookMyShow depends on Show, Theater, Screen abstractions

## Usage Flow

### 1. Setup Phase
```
Create Theaters → Add Screens → Define Seats
Create Shows → Create Show Timings (with pricing)
Register Users
```

### 2. Booking Phase
```
User selects Show Timing
↓
Available seats displayed
↓
User selects seats
↓
System calculates price using PricingStrategy
↓
Payment processed
↓
Booking confirmed (seats marked as BOOKED)
```

### 3. Queries
```
Get available seats for a show
Get user's booking history
Search shows by city/theater
Check seat layout with visual representation
```

## Features Implemented

✓ Multi-city, multi-theater support
✓ Multiple screens per theater
✓ Dynamic seat pricing based on day/time
✓ Flexible seat selection
✓ Booking state management
✓ Payment processing simulation
✓ Seat status tracking
✓ User registration and history
✓ Visual seat layout display
✓ Availability queries

## Extensibility

Future enhancements could include:
- Cancellation and refund handling
- Reserved seats for group bookings
- Dynamic pricing based on occupancy
- Discount coupons
- Notifications system
- Real payment gateway integration
- Theater ratings and reviews
- Multiple booking per user limits

## Running the Code

```bash
javac *.java
java Main
```

The demo shows:
- Theater and show setup
- Seat layout visualization
- Complete booking workflow
- Payment processing
- User booking history
- Updated seat availability
