# Google Login Error 10 / 12502 - Fix Guide

## 🔴 Lỗi hiện tại:
```
ApiException: 10 (DEVELOPER_ERROR)
ApiException: 12502 (SIGN_IN_CANCELLED)
```

## ✅ SHA-1 Fingerprint (Debug Keystore):
```
F3:56:49:F8:1A:02:1A:E3:87:B4:39:56:C8:E7:1C:81:8C:CF:AE:81
```

## 🔧 Giải pháp:

### **Option 1: Backend team thêm SHA-1 vào Google Console** (RECOMMENDED)

1. **Vào Google Cloud Console**: https://console.cloud.google.com/
2. **Chọn project** có Client ID: `1005211565279-3ffgkjhh0vd8ans8hv8gt9152gf3pim6`
3. **API & Services > Credentials**
4. **Tạo hoặc Edit OAuth 2.0 Client ID** (Android type)
5. **Điền thông tin**:
   - **Application type**: Android
   - **Package name**: `com.example.cinemabookingsystemfe`
   - **SHA-1 certificate fingerprint**: 
     ```
     F3:56:49:F8:1A:02:1A:E3:87:B4:39:56:C8:E7:1C:81:8C:CF:AE:81
     ```
6. **Save** → Đợi 5-10 phút để Google sync
7. **Re-test** app

---

### **Option 2: Tạo OAuth Client ID riêng cho Android**

Nếu bạn có quyền truy cập Google Cloud Console:

1. **Vào**: https://console.cloud.google.com/
2. **Tạo new project** hoặc dùng project hiện tại
3. **Enable Google Sign-In API**
4. **Create OAuth 2.0 Client ID**:
   - Type: **Android**
   - Package: `com.example.cinemabookingsystemfe`
   - SHA-1: `F3:56:49:F8:1A:02:1A:E3:87:B4:39:56:C8:E7:1C:81:8C:CF:AE:81`
5. **Copy Android Client ID** mới
6. **Update trong code**:

```java
private static final String CLIENT_ID = "YOUR_NEW_ANDROID_CLIENT_ID.apps.googleusercontent.com";
```

7. **Backend cũng cần accept Client ID này**

---

### **Option 3: Debug mode - Skip Client ID validation** (For Testing Only)

Nếu chỉ muốn test flow mà không cần Google Console access:

1. **Comment out** `.requestIdToken()` tạm thời
2. **Chỉ dùng** `.requestEmail()` và `.requestProfile()`
3. **Mock ID Token** trong code (for testing)

⚠️ **LƯU Ý**: Cách này chỉ để test UI flow, không thể login thực tế với backend.

---

## 📋 Checklist khi Backend config xong:

- [ ] SHA-1 đã được thêm vào Google Console
- [ ] Package name match: `com.example.cinemabookingsystemfe`
- [ ] Client ID đúng: `1005211565279-3ffgkjhh0vd8ans8hv8gt9152gf3pim6.apps.googleusercontent.com`
- [ ] Đợi 5-10 phút sau khi config
- [ ] Clear app data & cache
- [ ] Re-install app
- [ ] Test lại Google Sign-In

---

## 🎯 Testing sau khi fix:

1. **Uninstall app** cũ
2. **Re-install** app mới (sau khi backend config)
3. **Click "Sign in with Google"**
4. **Select Google account**
5. **Check logs**: Không còn Error 10/12502
6. **Verify**: ID Token được nhận thành công

---

## 📞 Liên hệ Backend Team:

Gửi thông tin này cho backend team:

```
Package name: com.example.cinemabookingsystemfe
SHA-1 Debug: F3:56:49:F8:1A:02:1A:E3:87:B4:39:56:C8:E7:1C:81:8C:CF:AE:81
Client ID hiện tại: 1005211565279-3ffgkjhh0vd8ans8hv8gt9152gf3pim6.apps.googleusercontent.com
```

Yêu cầu: **Thêm SHA-1 fingerprint vào Google OAuth 2.0 Client ID (Android type)**
