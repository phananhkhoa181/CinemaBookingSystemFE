# 🎬 Cinema Booking System - Mock Data Setup

## ✅ Mock Data Đã Được Thiết Lập!

App hiện đang chạy với **mock data** - không cần backend để test!

---

## 📦 Các Model Đã Tạo

### Request Models (`data/models/request/`)
- ✅ `LoginRequest.java` - Username + Password
- ✅ `RegisterRequest.java` - User registration data

### Response Models (`data/models/response/`)
- ✅ `User.java` - User information
- ✅ `LoginResponse.java` - Token + User sau khi login
- ✅ `RegisterResponse.java` - Token + User sau khi register
- ✅ `ApiResponse.java` - Generic wrapper (đã có)
- ✅ `PagedResult.java` - Pagination wrapper (đã có)

---

## 🚀 Mock API Service

**File:** `data/api/MockApiService.java`

### Tính năng:
- ✅ Mock login (chấp nhận bất kỳ username/password nào)
- ✅ Mock register (tự động tạo user mới)
- ✅ Mock get user profile
- ✅ Simulate network delay (1 giây)
- ✅ Trả về response giống backend thật

### Cách hoạt động:
```java
// Auto-login sau 1 giây
MockApiService.login(request, new ApiCallback<LoginResponse>() {
    @Override
    public void onSuccess(ApiResponse<LoginResponse> response) {
        // Nhận token + user info
        LoginResponse data = response.getData();
        String token = data.getToken(); // "mock_jwt_token_1234567890"
        User user = data.getUser();
    }
    
    @Override
    public void onError(String errorMessage) {
        // Handle error
    }
});
```

---

## 🎮 Cách Test App

### 1. Run App
```bash
# Mở Android Studio
# Click Run ▶️
# Chọn Emulator hoặc Device
```

### 2. Flow tự động
1. **SplashActivity** (2 giây) ✅
   - Check token trong SharedPreferences
   - Nếu có token → MainActivity
   - Nếu không → LoginActivity

2. **LoginActivity** (hiện tại auto-login) ✅
   - Tự động gọi `login("testuser", "password123")`
   - Save token vào SharedPreferences
   - Navigate to MainActivity

3. **MainActivity** ✅
   - Hiển thị placeholder screens
   - Đã có token → User đã login

---

## 🔧 Chuyển Đổi Mock/Real API

**File:** `utils/Constants.java`

```java
// Mock Mode - Set to true to use mock data (no backend needed)
public static final boolean USE_MOCK_API = true; // ← Đổi thành false khi có backend
```

### Khi có Backend:
1. Set `USE_MOCK_API = false`
2. Update `BASE_URL` thành URL thật
3. Implement real API calls trong `AuthRepository.java`
4. Xóa MockApiService (optional)

---

## 📝 TODO - Thêm Mock Data Cho Modules Khác

### Movies Module
```java
// TODO: Thêm vào MockApiService.java
public static void getMovies(ApiCallback<PagedResult<Movie>> callback) {
    // Return list of mock movies
}
```

### Cinemas Module
```java
public static void getCinemas(ApiCallback<List<Cinema>> callback) {
    // Return list of mock cinemas
}
```

### Bookings Module
```java
public static void createBooking(BookingRequest request, ApiCallback<Booking> callback) {
    // Return mock booking confirmation
}
```

---

## 💡 Lợi Ích Mock Data

✅ **Test ngay** - Không phải đợi backend  
✅ **Develop song song** - FE và BE làm việc độc lập  
✅ **Demo dễ dàng** - Show app cho stakeholders  
✅ **Unit testing** - Dễ viết test cases  
✅ **Offline development** - Làm việc không cần internet  

---

## 🎯 Next Steps

1. **Developer 1**: Tạo UI thật cho LoginActivity (EditText, Button, validation)
2. **Developer 2**: Thêm mock data cho Movies
3. **Developer 3**: Thêm mock data cho Cinemas & Showtimes
4. **Developer 4**: Thêm mock data cho Bookings & Payment
5. **Developer 5**: Thêm mock data cho User Profile

---

## 📚 Reference

- `docs-FE/03-Screens/01-Auth.md` - Auth screens design
- `docs-FE/05-API-Integration/` - API integration guide
- `README-TEAM.md` - Comprehensive team guide
- `TASK-TRACKING.md` - Task assignments

---

**Happy Coding! 🚀**

*Last Updated: ${new Date().toLocaleDateString('vi-VN')}*
