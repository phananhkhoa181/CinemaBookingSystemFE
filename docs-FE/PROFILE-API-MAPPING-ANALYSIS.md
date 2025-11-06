# 🔍 Profile API Mapping Analysis

## Phân tích giao diện vs Database/API

### 📱 Màn hình 1: Tài khoản (ProfileFragment)

#### UI Elements (từ hình ảnh):
```
┌─────────────────────────────────┐
│  Tài khoản              ⚙️      │
├─────────────────────────────────┤
│   👤  Đoàn Ngọc Trung   📷      │
│   ⭐ Star                        │
│   🎁 11 Stars                   │
│                     [Mã thành viên]
├─────────────────────────────────┤
│  📋 Thông tin                   │
│  💰 Giao dịch                   │
│  🔔 Thông báo                   │
├─────────────────────────────────┤
│  Tổng chi tiêu 2025   349,000đ │
│  ────●────────●────────●────    │
│  0đ      2,000,000đ  4,000,000đ│
└─────────────────────────────────┘
```

#### Required APIs:

**1. GET /api/customers/profile** ✅ (DONE - Screen 2)
```json
{
  "customerid": 3,
  "userid": 6,
  "fullname": "Đoàn Ngọc Trung",
  "email": "noshibi123@gmail.com",
  "phone": "0787171600",
  "address": null,
  "dateofbirth": "2004-02-17",
  "gender": "Nam"
}
```

**⚠️ Missing Fields cần xử lý:**
- ❌ `avatarurl` → Dùng placeholder hoặc icon mặc định
- ❌ `loyaltypoints` (11 Stars) → Tính từ bookings hoặc hardcode
- ❌ `membershiptier` (Star) → Tính dựa trên total spending
- ❌ `membershipcode` → Generate từ customerid hoặc hardcode
- ❌ `totalspending` (349,000đ) → Tính từ SUM(bookings.totalprice)

#### Giải pháp Tạm thời:

**Option 1: Tính toán từ Bookings**
```kotlin
// GET /api/bookings/my-bookings
val totalSpending = bookings.sumOf { it.totalprice }
val loyaltyPoints = (totalSpending / 10000).toInt() // 1 point per 10k VND
val tier = when {
    totalSpending >= 4000000 -> "Platinum"
    totalSpending >= 2000000 -> "Gold"
    else -> "Star"
}
```

**Option 2: Mock Data tạm**
```kotlin
data class CustomerProfileUI(
    val customer: CustomerProfile,
    val loyaltyPoints: Int = 0, // Mock
    val membershipTier: String = "Star", // Mock
    val totalSpending: Double = 0.0, // Mock
    val membershipCode: String = "MEMBER${customer.customerid}" // Generated
)
```

---

### 📱 Màn hình 2: Cập Nhật (EditProfileActivity)

#### UI Elements (từ hình ảnh):
```
┌─────────────────────────────────┐
│  ← Cập Nhật                     │
├─────────────────────────────────┤
│         👤 📷                   │
│                                 │
│  ┌─────────────────────────┐   │
│  │ Đoàn Ngọc Trung         │   │
│  └─────────────────────────┘   │
│  ┌─────────────────────────┐   │
│  │ 0787171600              │   │
│  └─────────────────────────┘   │
│  ┌─────────────────────────┐   │
│  │ 17/02/2004          📅  │   │
│  └─────────────────────────┘   │
│  ┌─────────────────────────┐   │
│  │ noshibi123@gmail.com    │   │
│  └─────────────────────────┘   │
│                                 │
│  ○ Nam  ○ Nữ  ○ Chưa Xác Định  │
│                                 │
│  ┌─────────────────────────┐   │
│  │     Cập nhật            │   │
│  └─────────────────────────┘   │
│                                 │
│  Thay đổi mật khẩu              │
└─────────────────────────────────┘
```

#### Required APIs:

**1. GET /api/customers/profile** ✅ (Load current data)
- Để fill form khi mở màn hình

**2. PUT /api/users/{id}** ❌ TODO
```json
// Request
{
  "fullname": "Đoàn Ngọc Trung",
  "phone": "0787171600"
}
```

**3. PUT /api/customers/profile** ❌ TODO
```json
// Request
{
  "address": "",
  "dateofbirth": "2004-02-17",
  "gender": "Nam"
}
```

**4. POST /api/users/avatar** ❌ SKIP (database chưa hỗ trợ)
- Chưa có field `avatarurl` trong database
- Tạm thời dùng placeholder avatar

**5. POST /api/auth/change-password** ✅ DONE (Screen 1)
- Link "Thay đổi mật khẩu" → Navigate to ChangePasswordActivity

---

## 🎯 Implementation Plan

### Step 1: ProfileFragment (Màn hình chính)

**ViewState:**
```kotlin
data class ProfileViewState(
    val isLoading: Boolean = false,
    val customer: CustomerProfile? = null,
    val totalSpending: Double = 0.0, // Calculated from bookings
    val loyaltyPoints: Int = 0, // Calculated
    val membershipTier: String = "Star", // Calculated
    val error: String? = null
)
```

**ViewModel:**
```kotlin
class ProfileViewModel : ViewModel() {
    private val _viewState = MutableLiveData<ProfileViewState>()
    val viewState: LiveData<ProfileViewState> = _viewState
    
    fun loadProfile() {
        viewModelScope.launch {
            try {
                _viewState.value = ProfileViewState(isLoading = true)
                
                // Call API
                val customer = customerRepository.getCustomerProfile()
                val bookings = bookingRepository.getMyBookings()
                
                // Calculate
                val totalSpending = bookings.sumOf { it.totalprice }
                val loyaltyPoints = (totalSpending / 10000).toInt()
                val tier = calculateTier(totalSpending)
                
                _viewState.value = ProfileViewState(
                    isLoading = false,
                    customer = customer,
                    totalSpending = totalSpending,
                    loyaltyPoints = loyaltyPoints,
                    membershipTier = tier
                )
            } catch (e: Exception) {
                _viewState.value = ProfileViewState(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    private fun calculateTier(spending: Double): String {
        return when {
            spending >= 4000000 -> "Platinum"
            spending >= 2000000 -> "Gold"
            else -> "Star"
        }
    }
}
```

**Fragment Implementation:**
```kotlin
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        observeViewModel()
        setupListeners()
        
        viewModel.loadProfile()
    }
    
    private fun observeViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when {
                state.isLoading -> showLoading()
                state.error != null -> showError(state.error)
                state.customer != null -> displayProfile(state)
            }
        }
    }
    
    private fun displayProfile(state: ProfileViewState) {
        with(binding) {
            // Basic info
            tvFullName.text = state.customer?.fullname
            tvEmail.text = state.customer?.email
            tvPhone.text = state.customer?.phone
            
            // Membership info
            tvMembershipTier.text = state.membershipTier
            tvLoyaltyPoints.text = "${state.loyaltyPoints} Stars"
            tvTotalSpending.text = formatCurrency(state.totalSpending)
            
            // Progress bar
            val progress = when {
                state.totalSpending >= 4000000 -> 100
                state.totalSpending >= 2000000 -> 50
                else -> (state.totalSpending / 4000000 * 100).toInt()
            }
            progressMembership.progress = progress
            
            // Avatar placeholder
            ivAvatar.setImageResource(R.drawable.ic_avatar_placeholder)
            
            // Membership code
            tvMembershipCode.text = "MEMBER${state.customer?.customerid}"
        }
    }
}
```

---

### Step 2: EditProfileActivity (Màn hình cập nhật)

**ViewState:**
```kotlin
data class EditProfileViewState(
    val isLoading: Boolean = false,
    val customer: CustomerProfile? = null,
    val isSaving: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
```

**ViewModel:**
```kotlin
class EditProfileViewModel : ViewModel() {
    private val _viewState = MutableLiveData<EditProfileViewState>()
    val viewState: LiveData<EditProfileViewState> = _viewState
    
    fun loadProfile() {
        viewModelScope.launch {
            try {
                _viewState.value = EditProfileViewState(isLoading = true)
                val customer = customerRepository.getCustomerProfile()
                _viewState.value = EditProfileViewState(
                    isLoading = false,
                    customer = customer
                )
            } catch (e: Exception) {
                _viewState.value = EditProfileViewState(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun updateProfile(
        fullname: String,
        phone: String,
        dateOfBirth: String,
        gender: String,
        address: String = ""
    ) {
        viewModelScope.launch {
            try {
                _viewState.value = _viewState.value?.copy(isSaving = true)
                
                // Call 2 APIs in parallel
                val userId = getUserIdFromToken() // From JWT
                
                // Update User table
                val userRequest = UpdateUserRequest(
                    fullname = fullname,
                    phone = phone
                )
                userRepository.updateUser(userId, userRequest)
                
                // Update Customer table
                val customerRequest = UpdateCustomerProfileRequest(
                    address = address,
                    dateofbirth = dateOfBirth,
                    gender = gender
                )
                customerRepository.updateCustomerProfile(customerRequest)
                
                _viewState.value = EditProfileViewState(
                    isSaving = false,
                    success = true
                )
            } catch (e: Exception) {
                _viewState.value = _viewState.value?.copy(
                    isSaving = false,
                    error = e.message
                )
            }
        }
    }
}
```

**Activity Implementation:**
```kotlin
class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        observeViewModel()
        setupListeners()
        
        viewModel.loadProfile()
    }
    
    private fun observeViewModel() {
        viewModel.viewState.observe(this) { state ->
            when {
                state.isLoading -> showLoading()
                state.customer != null -> fillForm(state.customer)
                state.isSaving -> showSaving()
                state.success -> {
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                    finish()
                }
                state.error != null -> showError(state.error)
            }
        }
    }
    
    private fun fillForm(customer: CustomerProfile) {
        with(binding) {
            etFullName.setText(customer.fullname)
            etPhone.setText(customer.phone)
            etEmail.setText(customer.email)
            etDateOfBirth.setText(customer.dateofbirth)
            
            when (customer.gender) {
                "Nam" -> rbMale.isChecked = true
                "Nữ" -> rbFemale.isChecked = true
                else -> rbOther.isChecked = true
            }
        }
    }
    
    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            saveProfile()
        }
        
        binding.btnChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        
        binding.etDateOfBirth.setOnClickListener {
            showDatePicker()
        }
    }
    
    private fun saveProfile() {
        val fullname = binding.etFullName.text.toString()
        val phone = binding.etPhone.text.toString()
        val dateOfBirth = binding.etDateOfBirth.text.toString()
        val gender = when {
            binding.rbMale.isChecked -> "Nam"
            binding.rbFemale.isChecked -> "Nữ"
            else -> "Chưa Xác Định"
        }
        
        viewModel.updateProfile(fullname, phone, dateOfBirth, gender)
    }
}
```

---

## 📦 API Services cần implement

### UserApiService.kt
```kotlin
interface UserApiService {
    @GET("/api/users/me")
    suspend fun getCurrentUser(): ApiResponse<UserDTO>
    
    @PUT("/api/users/{id}")
    suspend fun updateUser(
        @Path("id") userId: Int,
        @Body request: UpdateUserRequest
    ): ApiResponse<UserDTO>
}
```

### CustomerApiService.kt (extend existing)
```kotlin
interface CustomerApiService {
    // Already exists
    @GET("/api/customers/profile")
    suspend fun getCustomerProfile(): ApiResponse<CustomerProfile>
    
    // New endpoint
    @PUT("/api/customers/profile")
    suspend fun updateCustomerProfile(
        @Body request: UpdateCustomerProfileRequest
    ): ApiResponse<CustomerProfile>
}
```

---

## 📝 Data Models

### Request DTOs
```kotlin
data class UpdateUserRequest(
    val fullname: String,
    val phone: String
)

data class UpdateCustomerProfileRequest(
    val address: String?,
    val dateofbirth: String?, // "yyyy-MM-dd"
    val gender: String?
)
```

### Response DTOs
```kotlin
data class UserDTO(
    val userid: Int,
    val fullname: String,
    val email: String,
    val phone: String?,
    val roleid: Int,
    val rolename: String,
    val createdat: String,
    val updatedat: String
)
```

---

## ⚠️ Known Limitations

### 1. Avatar Upload
- ❌ Database không có field `avatarurl`
- **Solution**: Dùng placeholder icon cho đến khi backend update database

### 2. Loyalty Points & Membership
- ❌ Database không lưu trữ points và tier
- **Solution**: Tính toán realtime từ booking history
- **Future**: Backend nên thêm bảng `memberships` để lưu trữ

### 3. Membership Code
- ❌ Database không có field
- **Solution**: Generate từ customerid: `"MEMBER{customerid}"`

### 4. Total Spending Progress
- ❌ Cần query all bookings để tính
- **Solution**: Cache kết quả hoặc backend thêm endpoint `/api/customers/statistics`

---

## ✅ Testing Checklist

- [ ] Load profile data từ API
- [ ] Display correct membership tier based on spending
- [ ] Calculate loyalty points correctly
- [ ] Edit fullname và phone
- [ ] Edit dateofbirth với date picker
- [ ] Edit gender với radio buttons
- [ ] Validate phone format
- [ ] Validate date format
- [ ] Show loading state
- [ ] Handle API errors
- [ ] Navigate to change password
- [ ] Refresh profile after update
- [ ] Handle empty/null fields

---

## 🔄 Future Enhancements

### Backend cần thêm:
1. **Avatar Upload**
   - Add `avatarurl` field to Customer table
   - Implement `/api/users/avatar` endpoint
   - Cloud storage integration

2. **Membership System**
   - Add `memberships` table
   - Fields: `customerid`, `tier`, `points`, `totalspending`, `membershipcode`
   - Auto-update on booking completion

3. **Statistics Endpoint**
   ```
   GET /api/customers/statistics
   Response:
   {
     "totalspending": 349000,
     "loyaltypoints": 11,
     "tier": "Star",
     "bookingcount": 5
   }
   ```

### Frontend cần thêm:
1. Image cropper cho avatar upload
2. Form validation cho phone number
3. Date picker Vietnamese format
4. Offline caching for profile data
5. Pull-to-refresh
