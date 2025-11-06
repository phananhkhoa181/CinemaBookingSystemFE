# 🔧 Google Login Error 10 - Complete Fix Guide

## 🔴 Vấn đề:
Lỗi 10 (DEVELOPER_ERROR) vẫn xảy ra sau khi thêm SHA-1 vào Google Console.

## ✅ Nguyên nhân thực sự:
**THIẾU FILE `google-services.json`** - Đây là file BẮT BUỘC cho Google Sign-In trên Android!

---

## 🎯 Giải pháp đầy đủ:

### **Option 1: Sử dụng Firebase (RECOMMENDED)**

#### Bước 1: Tạo Firebase Project
1. Vào: https://console.firebase.google.com/
2. Click "Add project" hoặc chọn project có sẵn
3. Follow wizard để create project

#### Bước 2: Add Android App
1. Trong Firebase Console, click "Add app" > Android icon
2. Điền thông tin:
   ```
   Android package name: com.example.cinemabookingsystemfe
   App nickname: CinemaBookingSystemFE
   Debug signing certificate SHA-1: F3:56:49:F8:1A:02:1A:E3:87:B4:39:56:C8:E7:1C:81:8C:CF:AE:81
   ```
3. Click "Register app"

#### Bước 3: Download google-services.json
1. Download file `google-services.json`
2. Copy vào: `app/google-services.json` (đã tạo file tạm thời)
3. **REPLACE** file tạm thời bằng file thật từ Firebase

#### Bước 4: Enable Google Sign-In trong Firebase
1. Trong Firebase Console > Authentication
2. Click "Get started"
3. Sign-in method > Google > Enable
4. Project public-facing name: "CinemaBookingSystem"
5. Support email: your-email@gmail.com
6. Save

#### Bước 5: Sync và Test
1. Trong Android Studio: **File > Sync Project with Gradle Files**
2. Clean & Rebuild: **Build > Clean Project** → **Build > Rebuild Project**
3. Uninstall app cũ trên device
4. Run app mới
5. Test Google Sign-In

---

### **Option 2: Sử dụng Google Cloud Console trực tiếp**

Nếu không muốn dùng Firebase:

#### Bước 1: Tạo OAuth 2.0 Client IDs
1. Vào: https://console.cloud.google.com/apis/credentials
2. Create Credentials > OAuth 2.0 Client ID

#### Tạo 2 Client IDs:

**A. Web Client ID** (cho backend):
- Application type: **Web application**
- Name: "CinemaBookingSystem Web"
- Save và copy Client ID

**B. Android Client ID** (cho mobile app):
- Application type: **Android**
- Package name: `com.example.cinemabookingsystemfe`
- SHA-1: `F3:56:49:F8:1A:02:1A:E3:87:B4:39:56:C8:E7:1C:81:8C:CF:AE:81`
- Save và copy Client ID

#### Bước 2: Update Code
Thay đổi trong `LoginActivity.java`:
```java
// Replace Web Client ID
private static final String WEB_CLIENT_ID = "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com";
```

#### Bước 3: Tạo google-services.json manually
Cập nhật file `app/google-services.json` với Web Client ID thật.

---

## 📋 Checklist sau khi config:

- [ ] File `google-services.json` đã có trong `app/` folder
- [ ] `build.gradle.kts` (project) có `google-services` classpath
- [ ] `build.gradle.kts` (app) có plugin `com.google.gms.google-services`
- [ ] Sync Gradle thành công
- [ ] Clean & Rebuild project
- [ ] Uninstall app cũ
- [ ] Install app mới
- [ ] Test Google Sign-In

---

## 🔍 Verify Configuration:

### Check 1: google-services.json có đúng package name không?
```json
{
  "client": [
    {
      "client_info": {
        "android_client_info": {
          "package_name": "com.example.cinemabookingsystemfe"  // ✅ Phải match
        }
      }
    }
  ]
}
```

### Check 2: Build.gradle sync thành công?
```
Gradle sync successful
```

### Check 3: App đã được uninstall & reinstall?
```
adb uninstall com.example.cinemabookingsystemfe
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 🐛 Nếu vẫn bị lỗi 10:

### 1. Check Logcat chi tiết:
```
adb logcat | findstr "GoogleSignIn"
```

### 2. Verify SHA-1 fingerprint match:
```powershell
keytool -list -v -keystore "$env:USERPROFILE\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
```
SHA-1 phải là: `F3:56:49:F8:1A:02:1A:E3:87:B4:39:56:C8:E7:1C:81:8C:CF:AE:81`

### 3. Clear Google Play Services cache:
- Settings > Apps > Google Play Services
- Storage > Clear Cache & Clear Data
- Restart device

### 4. Wait for propagation:
- Sau khi config Google Console, đợi **5-10 phút**
- Google cần time để sync config

### 5. Check internet connection:
- Google Sign-In cần internet để verify
- Test trên real device với stable network

---

## 📞 Nếu vẫn không được:

Gửi cho tôi:
1. **Full logcat** khi click Google Sign-In button
2. **Screenshot** Google Cloud Console > Credentials
3. **Nội dung** file `google-services.json` (remove sensitive info)

---

## ✅ Expected Flow (khi đúng):

```
Click "Sign in with Google"
  ↓
Google account picker appears
  ↓
Select account
  ↓
Logcat: "ID Token received from Google"
  ↓
Backend API call successful
  ↓
Navigate to MainActivity
```

---

## 🎉 Tóm tắt các file đã update:

1. ✅ `build.gradle.kts` (project) - Added google-services classpath
2. ✅ `build.gradle.kts` (app) - Added google-services plugin
3. ✅ `google-services.json` - Created temp file (need to replace with real one)
4. ✅ `LoginActivity.java` - Updated to use google-services.json or fallback

---

**NEXT STEP**: 
1. **Get real `google-services.json` from Firebase Console**
2. **Replace the temp file**
3. **Sync Gradle**
4. **Test again**
