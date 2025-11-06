# 🧪 Hướng Dẫn Test Tính Năng Distance

## ✅ Checklist Test

### 1. **Test Location Permission**
- [ ] Mở app lần đầu → Thấy popup xin quyền location
- [ ] Chọn "Allow" → App lấy được vị trí
- [ ] Chọn "Deny" → App vẫn hoạt động bình thường (không crash)

### 2. **Test Distance Display**
- [ ] Vào màn hình chọn rạp (SelectCinemaActivity)
- [ ] Kiểm tra mỗi cinema có hiện badge khoảng cách (VD: "2.5 km", "850 m")
- [ ] Badge có background màu xám (#E8E8E8) với border radius 12dp
- [ ] Text màu `textSecondary`, size 12sp

### 3. **Test Distance Calculation**
- [ ] Rạp gần nhất phải ở đầu danh sách
- [ ] Rạp xa nhất ở cuối danh sách
- [ ] Khoảng cách hiển thị đúng định dạng:
  - Dưới 1km: "850 m"
  - Trên 1km: "2.5 km"

### 4. **Test Edge Cases**
**Location Permission Denied:**
- [ ] App không crash
- [ ] Cinemas vẫn hiển thị
- [ ] Distance badge không hiển thị (GONE)

**No GPS Signal:**
- [ ] App sử dụng last known location
- [ ] Nếu không có location → badge GONE

**Cinema Without Coordinates:**
- [ ] Rạp không có latitude/longitude → badge GONE
- [ ] Rạp này xuất hiện ở cuối danh sách

### 5. **Test Date/Cinema Filter**
- [ ] Đổi ngày → Distance vẫn hiển thị đúng
- [ ] Filter theo rạp → Distance không thay đổi
- [ ] Expand/collapse rạp → Distance không bị ẩn

### 6. **Test Performance**
- [ ] Load showtimes nhanh (< 2s)
- [ ] Calculate distance không block UI
- [ ] Scroll list mượt mà

## 🎯 Test Scenario Chi Tiết

### Scenario 1: First Time User (Happy Path)
```
1. Cài app mới
2. Login thành công
3. Chọn phim "Movie 88 - Kinh Dương Vương"
4. Vào màn hình chọn rạp
5. ✅ Popup xin quyền location
6. Chọn "Allow"
7. ✅ Thấy "Calculating distances..." (optional)
8. ✅ Danh sách rạp hiển thị với distance badge
9. ✅ Rạp gần nhất ở đầu tiên
```

### Scenario 2: Permission Denied (Graceful Degradation)
```
1. Vào màn hình chọn rạp
2. Popup xin quyền location
3. Chọn "Deny"
4. ✅ Toast: "Không thể xác định vị trí của bạn"
5. ✅ Danh sách rạp vẫn hiển thị
6. ✅ Distance badge KHÔNG hiển thị
7. ✅ App không crash
```

### Scenario 3: No GPS Signal (Indoor)
```
1. Tắt GPS hoặc ở trong nhà
2. Vào màn hình chọn rạp
3. ✅ App sử dụng last known location
4. ✅ Nếu có last location → hiện distance
5. ✅ Nếu không có → badge GONE
```

### Scenario 4: Change Date/Cinema Filter
```
1. Vào màn hình chọn rạp
2. Distance hiển thị đúng
3. Đổi ngày (VD: Thứ Bảy → Chủ Nhật)
4. ✅ Distance được tính lại
5. ✅ Sorting vẫn đúng (gần → xa)
6. Chọn filter "Movie 88 - Nguyễn Du"
7. ✅ Distance vẫn hiển thị
```

## 🐛 Bug Checklist (Cần Check)

- [ ] **NullPointerException**: Nếu cinema.getLatitude() = null
- [ ] **Distance = 0.0**: Khi user location = cinema location
- [ ] **Negative Distance**: Không thể xảy ra với Haversine
- [ ] **Memory Leak**: LocationHelper.stopLocationUpdates() trong onDestroy()
- [ ] **UI Thread Block**: Distance calculation phải async

## 📊 Expected Results

### Khi có Location Permission:
```
┌─────────────────────────────────────────┐
│ 📍 Movie 88 - Kinh Dương Vương  [800 m] │ ← Gần nhất
├─────────────────────────────────────────┤
│ 📍 Movie 88 - Nguyễn Du        [2.3 km] │
├─────────────────────────────────────────┤
│ 📍 Galaxy Nguyễn Văn Linh      [15 km]  │ ← Xa nhất
└─────────────────────────────────────────┘
```

### Khi KHÔNG có Location Permission:
```
┌─────────────────────────────────────────┐
│ 📍 Movie 88 - Kinh Dương Vương          │ ← Không sort
├─────────────────────────────────────────┤
│ 📍 Movie 88 - Nguyễn Du                 │
├─────────────────────────────────────────┤
│ 📍 Galaxy Nguyễn Văn Linh               │
└─────────────────────────────────────────┘
```

## 🔍 Debug Commands

### Check Location Permission (Logcat):
```
adb logcat | grep "SelectCinema"
```

### Expected Logs:
```
D/SelectCinema: Getting user location...
D/SelectCinema: User location: 10.8231, 106.6297
D/SelectCinema: Calculating distances for 3 cinemas
D/SelectCinema: Movie 88 - Kinh Dương Vương distance: 0.8 km
D/SelectCinema: Movie 88 - Nguyễn Du distance: 2.3 km
```

### Grant Permission via ADB:
```bash
adb shell pm grant com.example.cinemabookingsystemfe android.permission.ACCESS_FINE_LOCATION
adb shell pm grant com.example.cinemabookingsystemfe android.permission.ACCESS_COARSE_LOCATION
```

### Revoke Permission:
```bash
adb shell pm revoke com.example.cinemabookingsystemfe android.permission.ACCESS_FINE_LOCATION
```

## 📱 Test Devices

- [ ] **Emulator** (Easy GPS mock)
- [ ] **Real Device** (Indoor - no GPS)
- [ ] **Real Device** (Outdoor - with GPS)

### Mock Location on Emulator:
1. Extended Controls (⋯)
2. Location → Search "Ho Chi Minh City"
3. Set location: 10.8231° N, 106.6297° E
4. Click "Send"

## ✅ Pass Criteria

Feature considered **PASSED** if:
1. ✅ Permission flow works correctly
2. ✅ Distance displayed with correct format
3. ✅ Cinemas sorted by distance (closest first)
4. ✅ No crash when permission denied
5. ✅ Badge visibility handled correctly
6. ✅ Performance acceptable (< 2s load time)
7. ✅ No memory leaks

## 🚨 Critical Issues to Watch

1. **Permission Loop**: App không được spam permission request
2. **UI Freeze**: Distance calculation KHÔNG được block UI thread
3. **Battery Drain**: Location updates phải stop khi leave activity
4. **Crash on Deny**: App phải handle gracefully

---

**Status**: ⏳ Waiting for build  
**Next Step**: Run on device/emulator and follow checklist
