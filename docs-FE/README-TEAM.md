# 📱 Cinema Booking System - Android Frontend

## 🎯 Overview

Đây là ứng dụng Android cho hệ thống đặt vé xem phim Movie88, sử dụng kiến trúc MVVM và tích hợp với backend ASP.NET Core.

---

## 🏗️ Project Structure

```
app/src/main/java/com/example/cinemabookingsystemfe/
│
├── ui/                          # VIEW Layer (Activities & Fragments)
│   ├── auth/                   # Module 1: Authentication
│   │   ├── SplashActivity.java
│   │   ├── LoginActivity.java
│   │   └── RegisterActivity.java
│   │
│   ├── main/                   # Module 2: Main Navigation
│   │   ├── MainActivity.java
│   │   ├── HomeFragment.java
│   │   ├── BookingHistoryFragment.java
│   │   └── ProfileFragment.java
│   │
│   ├── booking/                # Module 3: Booking Flow
│   │   ├── SelectCinemaActivity.java
│   │   ├── SelectSeatActivity.java
│   │   └── SelectComboActivity.java
│   │
│   ├── payment/                # Module 4: Payment
│   │   ├── BookingSummaryActivity.java
│   │   ├── VNPayWebViewActivity.java
│   │   └── PaymentResultActivity.java
│   │
│   ├── movie/                  # Module 5: Movie Details (TODO)
│   ├── profile/                # Module 6: User Profile (TODO)
│   └── adapters/               # RecyclerView Adapters (TODO)
│
├── data/                        # DATA Layer
│   ├── api/                    # Retrofit API
│   │   ├── ApiClient.java     ✅ DONE
│   │   ├── ApiService.java    ✅ DONE
│   │   ├── ApiCallback.java   ✅ DONE
│   │   └── interceptors/
│   │       └── AuthInterceptor.java ✅ DONE
│   │
│   ├── models/                 # Request/Response DTOs
│   │   ├── request/           ⚠️ TODO: Tạo các Request models
│   │   └── response/          ⚠️ TODO: Tạo các Response models
│   │       ├── ApiResponse.java ✅ DONE
│   │       └── PagedResult.java ✅ DONE
│   │
│   └── repository/             # Repository Pattern
│       ├── AuthRepository.java     ⚠️ TODO: Implement
│       ├── MovieRepository.java    ⚠️ TODO: Implement
│       ├── BookingRepository.java  ⚠️ TODO: Implement
│       ├── PaymentRepository.java  ⚠️ TODO: Implement
│       └── UserRepository.java     ⚠️ TODO: Implement
│
├── utils/                       # Utility Classes
│   ├── Constants.java          ✅ DONE
│   ├── SharedPrefsManager.java ✅ DONE
│   ├── TokenManager.java       ✅ DONE
│   ├── DateUtils.java          ✅ DONE
│   └── ValidationUtils.java    ✅ DONE
│
└── viewmodels/                  # ViewModel Layer (TODO)
```

---

## 📋 Task Assignment

### **Developer 1: Authentication Module** 🔐
**Priority: HIGH | Deadline: Week 2**

**Tasks:**
1. ✅ Tạo layout files:
   - `activity_splash.xml`
   - `activity_login.xml`
   - `activity_register.xml`

2. ⚠️ Implement Activities:
   - `SplashActivity.java` - Token validation
   - `LoginActivity.java` - Login logic
   - `RegisterActivity.java` - Registration logic

3. ⚠️ Tạo Request/Response models:
   - `LoginRequest.java`
   - `LoginResponse.java`
   - `RegisterRequest.java`
   - `RegisterResponse.java`
   - `TokenResponse.java`

4. ⚠️ Implement Repository:
   - `AuthRepository.login()`
   - `AuthRepository.register()`
   - `AuthRepository.logout()`

**Refer to:** `docs-FE/03-Screens/01-Auth.md`

---

### **Developer 2: Main Navigation & Home** 🏠
**Priority: HIGH | Deadline: Week 3**

**Tasks:**
1. ✅ Tạo layout files:
   - `activity_main.xml` (ViewPager2 + BottomNavigationView)
   - `fragment_home.xml` (Banner + Movie lists)
   - `item_movie.xml` (Movie card)
   - `item_banner.xml` (Banner slide)

2. ⚠️ Implement UI:
   - `MainActivity.java` - Navigation setup
   - `HomeFragment.java` - Banner auto-scroll, movie lists

3. ⚠️ Tạo Adapters:
   - `MovieAdapter.java` - RecyclerView adapter
   - `BannerAdapter.java` - ViewPager2 adapter

4. ⚠️ Tạo Response models:
   - `Movie.java` (title, posterUrl, genre, rating, etc.)
   - `Cinema.java`

5. ⚠️ Implement Repository:
   - `MovieRepository.getNowShowingMovies()`
   - `MovieRepository.getComingSoonMovies()`

**Refer to:** `docs-FE/03-Screens/02-Main.md`

---

### **Developer 3: Booking Flow** 🎫
**Priority: HIGH | Deadline: Week 5-6**

**Tasks:**
1. ✅ Tạo layout files:
   - `activity_select_cinema.xml`
   - `activity_select_seat.xml`
   - `activity_select_combo.xml`
   - `item_cinema_showtime.xml`
   - `item_seat.xml`
   - `item_combo.xml`

2. ⚠️ Implement Activities:
   - `SelectCinemaActivity.java` - Cinema & showtime selection
   - `SelectSeatActivity.java` - Seat map với countdown timer
   - `SelectComboActivity.java` - Combo selection

3. ⚠️ Tạo Adapters:
   - `CinemaShowtimeAdapter.java`
   - `SeatAdapter.java` - GridLayoutManager
   - `ComboAdapter.java`

4. ⚠️ Tạo Response models:
   - `Showtime.java`
   - `Seat.java` (seatNumber, type, status, price)
   - `Combo.java`
   - `Booking.java`

5. ⚠️ Implement Repository:
   - `BookingRepository.getShowtimes()`
   - `BookingRepository.getShowtimeSeats()`
   - `BookingRepository.reserveSeats()`
   - `BookingRepository.createBooking()`

6. ⚠️ Implement Countdown Timer (5 minutes)

**Refer to:** `docs-FE/03-Screens/03-Booking.md` (⭐ Chi tiết nhất)

---

### **Developer 4: Payment Module** 💳
**Priority: MEDIUM | Deadline: Week 7**

**Tasks:**
1. ✅ Tạo layout files:
   - `activity_booking_summary.xml`
   - `activity_vnpay_webview.xml` (WebView)
   - `activity_payment_result.xml`

2. ⚠️ Implement Activities:
   - `BookingSummaryActivity.java` - Display summary, validate voucher
   - `VNPayWebViewActivity.java` - WebView integration
   - `PaymentResultActivity.java` - Display result, generate QR code

3. ⚠️ Tạo Response models:
   - `Payment.java`
   - `VNPayResponse.java`
   - `Voucher.java`
   - `VoucherValidationResponse.java`

4. ⚠️ Implement Repository:
   - `PaymentRepository.createVNPayPayment()`
   - `PaymentRepository.getPaymentById()`

5. ⚠️ QR Code Generation (sử dụng ZXing library)

**Refer to:** `docs-FE/03-Screens/05-Payment.md`

---

### **Developer 5: User Profile** 👤
**Priority: LOW | Deadline: Week 8**

**Tasks:**
1. ✅ Tạo layout files:
   - `fragment_profile.xml`
   - `fragment_booking_history.xml`
   - `item_booking.xml`

2. ⚠️ Implement Fragments:
   - `ProfileFragment.java` - User info, menu items
   - `BookingHistoryFragment.java` - Filter chips, booking list

3. ⚠️ Tạo Adapter:
   - `BookingAdapter.java`

4. ⚠️ Tạo Response model:
   - `User.java` (email, fullName, avatarUrl, etc.)

5. ⚠️ Implement Repository:
   - `UserRepository.getCurrentUser()`
   - `UserRepository.updateProfile()`
   - `BookingRepository.getMyBookings()`

**Refer to:** 
- `docs-FE/03-Screens/02-Main.md` (BookingHistoryFragment)
- `docs-FE/03-Screens/06-Profile.md`

---

## 🚀 Getting Started

### 1. Setup Development Environment

```bash
# Clone repository
git clone <repository-url>
cd CinemaBookingSystemFE

# Open in Android Studio
# File → Open → Select project folder

# Sync Gradle
# Android Studio sẽ tự động sync dependencies
```

### 2. Update Base URL

Mở `utils/Constants.java` và cập nhật `BASE_URL`:
```java
public static final String BASE_URL = "https://your-backend-url.com/";
```

### 3. Run App

1. Chọn emulator hoặc device
2. Click Run (Shift + F10)

---

## 📚 Documentation References

Tất cả documentation chi tiết nằm trong folder `docs-FE/`:

| File | Mô tả |
|------|-------|
| `README.md` | Tổng quan documentation |
| `01-Architecture/01-Overview.md` | Kiến trúc tổng thể, 22 screens |
| `01-Architecture/02-MVVM-Pattern.md` | MVVM implementation guide |
| `02-API-Integration/01-Retrofit-Setup.md` | Retrofit configuration |
| `02-API-Integration/02-Repository-Pattern.md` | Repository examples |
| `03-Screens/01-Auth.md` | Authentication screens |
| `03-Screens/02-Main.md` | Home & Navigation |
| `03-Screens/03-Booking.md` | ⭐ Booking flow (chi tiết nhất) |
| `03-Screens/05-Payment.md` | Payment integration |
| `03-Screens/06-Profile.md` | User profile |
| `API-Screen-Mapping-Summary.md` | API endpoints mapping |

---

## ⚙️ Build Configuration

### Dependencies đã được thêm:

- ✅ **Retrofit 2.9.0** - HTTP client
- ✅ **Gson 2.10.1** - JSON parsing
- ✅ **OkHttp 4.12.0** - Network interceptors
- ✅ **Glide 4.16.0** - Image loading
- ✅ **ViewPager2** - Sliding pages
- ✅ **Room Database** - Local caching (optional)
- ✅ **ZXing** - QR code generation
- ✅ **Material Components** - UI library

### Permissions đã được thêm:

- ✅ `INTERNET` - Network access
- ✅ `ACCESS_NETWORK_STATE` - Check network status
- ✅ `READ_EXTERNAL_STORAGE` - Read images
- ✅ `WRITE_EXTERNAL_STORAGE` - Save tickets

---

## 📁 Files Created (Skeleton)

### ✅ Utils (DONE)
- `Constants.java` - App constants
- `SharedPrefsManager.java` - SharedPreferences wrapper
- `TokenManager.java` - JWT token management
- `DateUtils.java` - Date formatting
- `ValidationUtils.java` - Input validation

### ✅ API (DONE)
- `ApiClient.java` - Retrofit builder
- `ApiService.java` - All API endpoints
- `ApiCallback.java` - Callback interface
- `AuthInterceptor.java` - JWT token injection

### ✅ Models (Partial)
- `ApiResponse.java` - Generic wrapper
- `PagedResult.java` - Pagination wrapper
- ⚠️ **TODO:** 30+ Request/Response models (see `_TODO_MODELS_README.java`)

### ✅ Repository (Skeleton)
- `AuthRepository.java` - TODO: Implement
- `MovieRepository.java` - TODO: Implement
- `BookingRepository.java` - TODO: Implement
- `PaymentRepository.java` - TODO: Implement
- `UserRepository.java` - TODO: Implement

### ✅ Activities (Skeleton)
- `SplashActivity.java` - TODO: Implement
- `LoginActivity.java` - TODO: Implement
- `RegisterActivity.java` - TODO: Implement
- `MainActivity.java` - TODO: Implement
- `HomeFragment.java` - TODO: Implement
- `BookingHistoryFragment.java` - TODO: Implement
- `ProfileFragment.java` - TODO: Implement
- `SelectCinemaActivity.java` - TODO: Implement
- `SelectSeatActivity.java` - TODO: Implement
- `SelectComboActivity.java` - TODO: Implement
- `BookingSummaryActivity.java` - TODO: Implement
- `VNPayWebViewActivity.java` - TODO: Implement
- `PaymentResultActivity.java` - TODO: Implement

### ✅ Resources (DONE)
- `colors.xml` - Color scheme
- `dimens.xml` - Spacing & sizes
- `strings.xml` - Text resources

### ✅ AndroidManifest.xml (DONE)
- All activities registered
- Permissions added
- SplashActivity as LAUNCHER

---

## 🔥 Development Workflow

### Phase 1: Week 1-2 (Authentication)
- [ ] Developer 1: Implement Login, Register, Splash
- [ ] Setup API testing với Postman
- [ ] Test JWT token flow

### Phase 2: Week 3 (Main Navigation)
- [ ] Developer 2: Implement MainActivity, HomeFragment
- [ ] Load movies from API
- [ ] Banner auto-scroll

### Phase 3: Week 4-6 (Booking Flow) ⚠️ CRITICAL
- [ ] Developer 3: Implement 3-screen booking flow
- [ ] Seat selection với timer
- [ ] Data flow: movieId → showtimeId → seatIds → bookingId

### Phase 4: Week 7 (Payment)
- [ ] Developer 4: VNPay integration
- [ ] QR code generation
- [ ] Payment result handling

### Phase 5: Week 8 (Profile & Polish)
- [ ] Developer 5: Profile, booking history
- [ ] All developers: Bug fixing, UI polish

---

## 🐛 Testing Checklist

### Authentication
- [ ] Login với credentials hợp lệ
- [ ] Login với credentials sai → Hiện error
- [ ] Auto-login khi token còn valid
- [ ] Logout → Clear token

### Booking Flow
- [ ] Chọn cinema → Load showtimes
- [ ] Chọn seat → Timer countdown
- [ ] Create booking → Receive bookingId
- [ ] Timer hết giờ → Navigate back với error

### Payment
- [ ] VNPay WebView load correctly
- [ ] Payment success → QR code generated
- [ ] Payment failed → Error message

---

## 📞 Support

**Questions?**
- Check `docs-FE/` folder for detailed documentation
- Review existing code comments marked with `TODO`
- Refer to backend API documentation

**Code Review:**
- Create Pull Request khi hoàn thành task
- Tag reviewers: Developer Lead
- Ensure no `TODO` comments còn lại

---

## ⚠️ Important Notes

1. **Không edit các file Utils đã DONE** - Đã hoàn chỉnh
2. **Không hardcode strings** - Sử dụng `strings.xml`
3. **Không hardcode colors** - Sử dụng `colors.xml`
4. **Test trên emulator trước khi commit**
5. **Follow MVVM pattern** - Activity → ViewModel → Repository → API

---

**Good luck! 🚀 Let's build an amazing cinema booking app!**
