# 📋 TASK TRACKING - Cinema Booking System Android

## 🎯 Project Status Overview

| Phase | Status | Deadline | Assigned To |
|-------|--------|----------|-------------|
| **Phase 1: Authentication** | 🟡 In Progress | Week 2 | Developer 1 |
| **Phase 2: Main Navigation** | ⚪ Not Started | Week 3 | Developer 2 |
| **Phase 3: Booking Flow** | ⚪ Not Started | Week 5-6 | Developer 3 |
| **Phase 4: Payment** | ⚪ Not Started | Week 7 | Developer 4 |
| **Phase 5: Profile** | ⚪ Not Started | Week 8 | Developer 5 |

---

## ✅ Completed Setup Tasks

### Infrastructure (DONE ✅)
- [x] Folder structure theo MVVM
- [x] build.gradle.kts với dependencies
- [x] Utils classes (Constants, SharedPrefsManager, TokenManager, DateUtils, ValidationUtils)
- [x] API setup (ApiClient, ApiService, AuthInterceptor, ApiCallback)
- [x] Base models (ApiResponse, PagedResult)
- [x] Repository skeletons
- [x] Activity/Fragment skeletons
- [x] Resources (colors.xml, dimens.xml, strings.xml)
- [x] AndroidManifest.xml với permissions

---

## 📝 Developer 1: Authentication Module

### Priority: 🔴 HIGH | Deadline: Week 2

#### Layouts to Create
- [ ] `res/layout/activity_splash.xml`
- [ ] `res/layout/activity_login.xml`
- [ ] `res/layout/activity_register.xml`

#### Models to Create
- [ ] `data/models/request/LoginRequest.java`
- [ ] `data/models/request/RegisterRequest.java`
- [ ] `data/models/request/RefreshTokenRequest.java`
- [ ] `data/models/response/LoginResponse.java`
- [ ] `data/models/response/RegisterResponse.java`
- [ ] `data/models/response/TokenResponse.java`

#### Repository Implementation
- [ ] `AuthRepository.login()` - Call API, save token, callback
- [ ] `AuthRepository.register()` - Call API, callback
- [ ] `AuthRepository.refreshToken()` - Refresh JWT token
- [ ] `AuthRepository.logout()` - Clear SharedPrefs

#### Activities Implementation
- [ ] `SplashActivity.java` - Check token, navigate
- [ ] `LoginActivity.java` - Email/password input, validation, API call
- [ ] `RegisterActivity.java` - Form validation, API call

#### Testing
- [ ] Test login với credentials đúng
- [ ] Test login với credentials sai
- [ ] Test auto-login khi token valid
- [ ] Test navigation flow

**Refer to:** `docs-FE/03-Screens/01-Auth.md`

---

## 📝 Developer 2: Main Navigation Module

### Priority: 🔴 HIGH | Deadline: Week 3

#### Layouts to Create
- [ ] `res/layout/activity_main.xml` (ViewPager2 + BottomNavigation)
- [ ] `res/layout/fragment_home.xml`
- [ ] `res/layout/item_movie.xml` (Movie card)
- [ ] `res/layout/item_banner.xml` (Banner slide)
- [ ] `res/menu/bottom_nav_menu.xml` (Already exists?)

#### Models to Create
- [ ] `data/models/response/Movie.java` ⭐ IMPORTANT
  ```java
  int movieId, String title, String posterUrl, String backdropUrl,
  String trailerUrl, int duration, String releaseDate, String genre,
  String ageRating, double rating, String status
  ```
- [ ] `data/models/response/Cinema.java`

#### Repository Implementation
- [ ] `MovieRepository.getNowShowingMovies()` - GET /api/movies?status=NowShowing
- [ ] `MovieRepository.getComingSoonMovies()` - GET /api/movies?status=ComingSoon
- [ ] `MovieRepository.getMovieById()` - GET /api/movies/{id}

#### Adapters to Create
- [ ] `ui/adapters/MovieAdapter.java` - RecyclerView.Adapter<MovieViewHolder>
- [ ] `ui/adapters/BannerAdapter.java` - RecyclerView.Adapter for ViewPager2

#### UI Implementation
- [ ] `MainActivity.java` - ViewPager + BottomNav sync
- [ ] `HomeFragment.java` - Banner auto-scroll (3s), movie lists
- [ ] Implement auto-scroll logic với Handler + Runnable
- [ ] Implement pause on user interaction

#### Testing
- [ ] Test banner auto-scroll
- [ ] Test movie list loading
- [ ] Test bottom navigation switching
- [ ] Test Glide image loading

**Refer to:** `docs-FE/03-Screens/02-Main.md`

---

## 📝 Developer 3: Booking Flow Module

### Priority: 🔴 HIGH | Deadline: Week 5-6

#### Layouts to Create
- [ ] `res/layout/activity_select_cinema.xml`
- [ ] `res/layout/activity_select_seat.xml`
- [ ] `res/layout/activity_select_combo.xml`
- [ ] `res/layout/item_cinema_showtime.xml`
- [ ] `res/layout/item_seat.xml`
- [ ] `res/layout/item_combo.xml`
- [ ] `res/layout/fragment_booking_history.xml`
- [ ] `res/layout/item_booking.xml`

#### Models to Create
- [ ] `data/models/response/Showtime.java` ⭐ IMPORTANT
- [ ] `data/models/response/Seat.java` ⭐ IMPORTANT
  ```java
  int seatId, String seatNumber, String type (Regular/VIP/Couple),
  String status (Available/Booked/Selected), double price
  ```
- [ ] `data/models/response/Combo.java`
- [ ] `data/models/response/Booking.java` ⭐ IMPORTANT
- [ ] `data/models/request/CreateBookingRequest.java`
- [ ] `data/models/request/ReserveSeatRequest.java`

#### Repository Implementation
- [ ] `BookingRepository.getShowtimes()` - GET /api/showtimes
- [ ] `BookingRepository.getShowtimeSeats()` - GET /api/showtimes/{id}/seats
- [ ] `BookingRepository.reserveSeats()` - POST /api/bookings/reserve-seats
- [ ] `BookingRepository.createBooking()` - POST /api/bookings
- [ ] `BookingRepository.getMyBookings()` - GET /api/bookings/my-bookings

#### Adapters to Create
- [ ] `ui/adapters/CinemaShowtimeAdapter.java`
- [ ] `ui/adapters/SeatAdapter.java` - GridLayoutManager
- [ ] `ui/adapters/ComboAdapter.java`
- [ ] `ui/adapters/BookingAdapter.java` (for history)

#### Activities Implementation
- [ ] `SelectCinemaActivity.java` - Date selector, cinema list, showtimes
- [ ] `SelectSeatActivity.java` - Seat map, timer, price calculation
- [ ] `SelectComboActivity.java` - Combo list, quantity selector, skip button
- [ ] `BookingHistoryFragment.java` - Filter chips, booking list

#### Special Features
- [ ] Countdown timer (5 minutes) - CountDownTimer class
- [ ] Timer persists across screens (SelectSeat → SelectCombo → Summary)
- [ ] Real-time price calculation
- [ ] Seat color coding (Available/Selected/Booked/VIP)

#### Testing
- [ ] Test data flow: movieId → showtimeId → seatIds → comboIds
- [ ] Test timer countdown
- [ ] Test timer expiry → redirect
- [ ] Test seat selection
- [ ] Test booking creation

**Refer to:** `docs-FE/03-Screens/03-Booking.md` (⭐ Chi tiết nhất - 2258 lines)

---

## 📝 Developer 4: Payment Module

### Priority: 🟡 MEDIUM | Deadline: Week 7

#### Layouts to Create
- [ ] `res/layout/activity_booking_summary.xml`
- [ ] `res/layout/activity_vnpay_webview.xml` (WebView)
- [ ] `res/layout/activity_payment_result.xml`

#### Models to Create
- [ ] `data/models/response/Payment.java`
- [ ] `data/models/response/VNPayResponse.java`
- [ ] `data/models/response/Voucher.java`
- [ ] `data/models/response/VoucherValidationResponse.java`
- [ ] `data/models/request/CreatePaymentRequest.java`
- [ ] `data/models/request/ValidateVoucherRequest.java`

#### Repository Implementation
- [ ] `PaymentRepository.createVNPayPayment()` - POST /api/payments/vnpay/create
- [ ] `PaymentRepository.getPaymentById()` - GET /api/payments/{id}

#### Activities Implementation
- [ ] `BookingSummaryActivity.java` - Display summary, validate voucher
- [ ] `VNPayWebViewActivity.java` - WebView, URL interception
- [ ] `PaymentResultActivity.java` - Result display, QR code

#### Special Features
- [ ] WebView configuration cho VNPay
- [ ] URL callback interception
- [ ] QR code generation (ZXing library)
- [ ] Download ticket as image

#### Testing
- [ ] Test voucher validation
- [ ] Test VNPay WebView loading
- [ ] Test payment success flow
- [ ] Test payment failure flow
- [ ] Test QR code generation

**Refer to:** `docs-FE/03-Screens/05-Payment.md`

---

## 📝 Developer 5: Profile Module

### Priority: 🟢 LOW | Deadline: Week 8

#### Layouts to Create
- [ ] `res/layout/fragment_profile.xml`
- [ ] `res/layout/activity_edit_profile.xml`
- [ ] `res/layout/activity_change_password.xml`

#### Models to Create
- [ ] `data/models/response/User.java`
- [ ] `data/models/request/UpdateProfileRequest.java`
- [ ] `data/models/request/ChangePasswordRequest.java`

#### Repository Implementation
- [ ] `UserRepository.getCurrentUser()` - GET /api/users/me
- [ ] `UserRepository.updateProfile()` - PUT /api/users/profile
- [ ] `UserRepository.uploadAvatar()` - POST /api/users/avatar

#### UI Implementation
- [ ] `ProfileFragment.java` - User info, menu items, logout
- [ ] Logout dialog confirmation
- [ ] Clear SharedPrefs on logout
- [ ] Navigate to Login (FLAG_CLEAR_TASK)

#### Testing
- [ ] Test profile display
- [ ] Test logout flow
- [ ] Test navigation to other screens

**Refer to:** 
- `docs-FE/03-Screens/02-Main.md` (BookingHistoryFragment)
- `docs-FE/03-Screens/06-Profile.md`

---

## 🚨 Critical Dependencies

### Developer 1 → Developer 2
- Developer 2 needs `LoginActivity` working to test MainActivity

### Developer 2 → Developer 3
- Developer 3 needs `Movie.java` model from Developer 2
- Developer 3 needs MovieDetailActivity navigation

### Developer 3 → Developer 4
- Developer 4 needs `Booking.java` model from Developer 3
- Developer 4 needs `bookingId` from SelectComboActivity

---

## 📊 Progress Tracking

Update this daily:

### Week 1 (Nov 4-8)
- [ ] Developer 1: Login layout
- [ ] Developer 1: Login API integration
- [ ] Developer 2: MainActivity layout

### Week 2 (Nov 11-15)
- [ ] Developer 1: Complete Authentication
- [ ] Developer 2: HomeFragment with movies
- [ ] Developer 3: Start SelectCinemaActivity

### Week 3 (Nov 18-22)
- [ ] Developer 2: Complete Main Navigation
- [ ] Developer 3: SelectSeatActivity with timer
- [ ] Developer 4: Start BookingSummaryActivity

### Week 4-6 (Nov 25 - Dec 13)
- [ ] Developer 3: Complete Booking Flow
- [ ] Developer 4: VNPay integration
- [ ] Testing & Integration

---

## 🐛 Known Issues / Blockers

**Log issues here:**

1. Issue: _______________
   - Assigned to: _______________
   - Status: _______________

---

## 📞 Daily Standup

**What did you do yesterday?**
**What will you do today?**
**Any blockers?**

---

**Last Updated:** [Date]
**Updated By:** [Your Name]
