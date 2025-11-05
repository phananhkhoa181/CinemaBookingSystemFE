# Backend Bugs Log

Danh sách các lỗi phát hiện từ backend API trong quá trình phát triển frontend.

---

## 🐛 Bug #1: Forgot Password Success Với Email Không Tồn Tại

**Mức độ:** HIGH 🔴  
**Ngày phát hiện:** 05/11/2025  
**API endpoint:** `POST /api/auth/forgot-password`  
**Backend URL:** https://movie88aspnet-app.up.railway.app/

### Mô tả lỗi:
Khi gọi forgot-password với email **không tồn tại trong hệ thống**, backend vẫn trả về **status 200 (success)** nhưng thực tế không sinh ra OTP token.

### Hành vi hiện tại:
**Request:**
```json
POST /api/auth/forgot-password
{
  "email": "nonexistent@example.com"
}
```

**Response:**
```json
{
  "message": "If the email exists, an OTP has been sent. Please check your email.",
  "status": 200,
}

```

### Vấn đề:
- ✅ Status 200 (giống như email hợp lệ)
- ❌ Không có OTP được sinh ra
- ❌ Frontend không phân biệt được email có tồn tại hay không

### Hành vi mong đợi:

**Option 1: Security-first approach (Khuyến nghị) ⭐**
Giữ nguyên response 200 để **không leak thông tin** user có tồn tại hay không (tránh email enumeration attack), nhưng thêm validation:
```json
{
  "message": "Nếu email tồn tại, OTP đã được gửi đến hộp thư của bạn",
  "status": 200,
  "data": {
    "email": "nonexistent@example.com",
    "otpType": "PasswordReset",
    "expiresAt": "2025-11-05T10:30:00Z",
    "message": "Vui lòng kiểm tra email (kể cả thư mục spam)"
  }
}
```
**Backend:** Chỉ gửi OTP thực sự nếu email tồn tại, nhưng response luôn giống nhau.

**Option 2: Clear error response (Dễ debug)**
```json
{
  "message": "Email không tồn tại trong hệ thống",
  "status": 404,
  "data": null
}
```
⚠️ **Lưu ý:** Cách này có thể bị lợi dụng để enumerate danh sách email trong hệ thống.

### Tác động:
- ❌ User nhập sai email → chờ OTP mãi không thấy
- ❌ Frontend navigate sang ResetPasswordActivity nhưng không có OTP
- ❌ User confusion: "Tại sao tôi không nhận được OTP?"
- ❌ Tăng support tickets không cần thiết

### Test case để reproduce:
```
1. Nhập email không tồn tại: "fakeuser999@notexist.com"
2. Click "Gửi OTP"
3. Backend response: 200 success
4. Check email: Không có OTP nào
5. Check expiresAt trong response: null
6. Thử verify OTP: Sẽ fail vì không có token nào được tạo
```

### Đề xuất giải pháp backend:
```csharp
// Pseudo code
var user = await _userRepository.GetByEmailAsync(email);

if (user == null) {
    _logger.LogWarning($"Forgot password attempted for non-existent email: {email}");
    return new ApiResponse {
        Message = "Nếu email tồn tại, OTP đã được gửi đến hộp thư của bạn",
        Status = 200,
        Data = new SendOtpResponse {
            Email = email,
            OtpType = "PasswordReset",
            ExpiresAt = DateTime.UtcNow.AddMinutes(10), // Fake timestamp
            Message = "Vui lòng kiểm tra email"
        }
    };
    // Không gửi email thực sự
}

// Nếu user tồn tại → sinh OTP và gửi email thật
var otp = GenerateOTP();
await _emailService.SendOtpEmail(user.Email, otp);
```

---

## 📊 Tổng hợp

| Bug ID | Mức độ | API Endpoint | Trạng thái | Tác động |
|--------|--------|--------------|------------|----------|
| #1 | HIGH | forgot-password | 🔴 Chưa fix | Email không tồn tại vẫn success 200 |

---

## 📝 Ghi chú

- **Ngày tạo:** 05/11/2025
- **Người phát hiện:** Frontend Team
- **Test environment:** https://movie88aspnet-app.up.railway.app/
- **Test email:** swiftyk18@gmail.com

### Hành động tiếp theo:

**Bug #1 (Email không tồn tại):**
1. [ ] Gửi bug report cho backend team
2. [ ] Frontend thêm basic email validation (format, domain check)
3. [ ] Thêm warning: "Nếu không nhận được email, vui lòng kiểm tra địa chỉ email"
4. [ ] Monitor backend fix và update frontend khi API được cập nhật

---

**Last updated:** 05/11/2025
