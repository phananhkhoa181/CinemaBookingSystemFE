# Google Login Implementation Summary

## ✅ Hoàn thành - Google Sign-In Integration

### 📋 Các file đã tạo/sửa đổi:

#### 1. **build.gradle.kts** (App level)
- ✅ Thêm dependency: `com.google.android.gms:play-services-auth:21.0.0`

#### 2. **GoogleLoginRequest.java** (NEW)
- ✅ Tạo model request cho Google Login
- Path: `app/src/main/java/com/example/cinemabookingsystemfe/data/models/request/GoogleLoginRequest.java`
- Chứa `idToken` để gửi lên backend

#### 3. **ApiService.java**
- ✅ Thêm endpoint: `POST /api/auth/google-login`
```java
@POST("api/auth/google-login")
Call<ApiResponse<LoginResponse>> googleLogin(@Body GoogleLoginRequest request);
```

#### 4. **AuthRepository.java**
- ✅ Thêm method `googleLogin(String idToken, ApiCallback callback)`
- Logic:
  - Tạo GoogleLoginRequest với idToken
  - Call API backend
  - Parse response (ApiResponse<LoginResponse>)
  - Lưu token, refreshToken, user info vào SharedPreferences
  - Set `isLoggedIn = true`
  - Callback success/error

#### 5. **LoginActivity.java**
- ✅ Import Google Sign-In classes
- ✅ Thêm constants:
  - `RC_SIGN_IN = 9001` (Request code)
  - `CLIENT_ID = "1005211565279-3ffgkjhh0vd8ans8hv8gt9152gf3pim6.apps.googleusercontent.com"`
- ✅ Thêm biến `GoogleSignInClient mGoogleSignInClient`
- ✅ Implement method `setupGoogleSignIn()`:
  - Configure GoogleSignInOptions với `.requestIdToken(CLIENT_ID)` và `.requestEmail()`
  - Tạo GoogleSignInClient
- ✅ Update button click listener: `btnGoogleSignIn.setOnClickListener(v -> signInWithGoogle())`
- ✅ Implement method `signInWithGoogle()`:
  - Start Google Sign-In Intent
- ✅ Override `onActivityResult()`:
  - Handle Google Sign-In result
- ✅ Implement method `handleSignInResult()`:
  - Get GoogleSignInAccount
  - Extract ID Token
  - Call `loginWithGoogleToken(idToken)`
- ✅ Implement method `loginWithGoogleToken()`:
  - Show loading
  - Call `authRepository.googleLogin()`
  - Handle success: Show welcome message, navigate to home
  - Handle error: Show error message
- ✅ Implement method `navigateToHome()`:
  - Navigate to MainActivity hoặc return result nếu gọi từ MainActivity
- ✅ Update `setLoading()`:
  - Disable/enable cả btnLogin và btnGoogleSignIn
  - Show/hide progressBar

---

## 🎯 Flow hoạt động:

```
User clicks "Sign in with Google" button
    ↓
signInWithGoogle() → Start Google Sign-In Intent
    ↓
User selects Google account
    ↓
onActivityResult() → Get GoogleSignInAccount
    ↓
handleSignInResult() → Extract ID Token
    ↓
loginWithGoogleToken(idToken) → Call AuthRepository.googleLogin()
    ↓
AuthRepository.googleLogin() → POST /api/auth/google-login
    ↓
Backend validates ID Token with Google
    ↓
Backend returns: { success, data: { token, refreshToken, user } }
    ↓
Save token, refreshToken, user info to SharedPreferences
    ↓
Navigate to MainActivity (Home screen)
```

---

## 🔧 Backend API Configuration:

- **Endpoint**: `POST /api/auth/google-login`
- **Base URL**: `https://movie88aspnet-app.up.railway.app/api/`
- **Client ID**: `1005211565279-3ffgkjhh0vd8ans8hv8gt9152gf3pim6.apps.googleusercontent.com`

**Request:**
```json
{
  "idToken": "eyJhbGciOiJSUzI1NiIs..."
}
```

**Response:**
```json
{
  "success": true,
  "statusCode": 200,
  "message": "Google login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "abc123...",
    "expiresAt": "2024-12-31T23:59:59",
    "user": {
      "userId": 123,
      "fullName": "John Doe",
      "email": "john@gmail.com",
      "phoneNumber": "0123456789",
      "roleId": 2,
      "roleName": "Customer"
    }
  }
}
```

---

## ✅ Testing Checklist:

- [ ] **Sync Gradle** - Build project successfully
- [ ] **Run on device/emulator** - Có Google Play Services
- [ ] **Click "Sign in with Google"** - Mở Google account picker
- [ ] **Select account** - Redirect về app
- [ ] **Check logs** - ID Token received
- [ ] **Check API call** - POST /api/auth/google-login success
- [ ] **Check SharedPreferences** - Token, user info đã lưu
- [ ] **Navigate to MainActivity** - Auto redirect sau khi login
- [ ] **Re-open app** - Skip login nếu đã có token

---

## 🐛 Troubleshooting:

### Error: "Developer Error" hoặc "Sign-In failed: 10"
**Nguyên nhân**: Client ID hoặc SHA-1 fingerprint chưa đúng
**Giải pháp**:
1. Verify Client ID: `1005211565279-3ffgkjhh0vd8ans8hv8gt9152gf3pim6.apps.googleusercontent.com`
2. Thêm SHA-1 fingerprint vào Google Console (nếu cần)
3. Package name phải khớp: `com.example.cinemabookingsystemfe`

### Lấy SHA-1 fingerprint (Debug):
```bash
cd android
./gradlew signingReport
```

Hoặc dùng keytool:
```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

### Error: "ID Token is null"
**Nguyên nhân**: Chưa request ID Token trong GoogleSignInOptions
**Giải pháp**: Đảm bảo `.requestIdToken(CLIENT_ID)` được gọi

### Error: "Network error" hoặc API call failed
**Nguyên nhân**: Backend API không response hoặc network issue
**Giải pháp**:
1. Check internet permission trong AndroidManifest.xml (đã có)
2. Test API với Postman trước
3. Check API base URL

---

## 📱 UI Note:

Button Google Sign-In đã có sẵn trong `activity_login.xml`:
```xml
<com.google.android.material.button.MaterialButton
    android:id="@+id/btnGoogleSignIn"
    ...
    android:text="Sign in with Google" />
```

---

## 🎉 DONE!

Google Login đã được implement thành công! Bạn có thể test ngay trên device có Google Play Services.

### Next Steps (Optional):
1. Handle token expiration & refresh
2. Add logout functionality (sign out from Google)
3. Handle edge cases (no internet, backend down, etc.)
4. Add loading animations
