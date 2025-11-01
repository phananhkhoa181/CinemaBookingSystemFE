package com.example.cinemabookingsystemfe.data.models;

/**
 * TODO_MODELS.md - Hướng dẫn tạo các model classes
 * 
 * Các file này cần được tạo bởi team developers.
 * Tham khảo backend API documentation để biết structure của từng model.
 * 
 * ============================================
 * RESPONSE MODELS (data/models/response/)
 * ============================================
 * 
 * 1. LoginResponse.java
 *    - String token
 *    - String refreshToken
 *    - User user
 * 
 * 2. RegisterResponse.java
 *    - int userId
 *    - String message
 * 
 * 3. TokenResponse.java
 *    - String token
 *    - String refreshToken
 * 
 * 4. Movie.java ⭐ PRIORITY HIGH
 *    - int movieId
 *    - String title
 *    - String description
 *    - String posterUrl
 *    - String backdropUrl
 *    - String trailerUrl
 *    - int duration (minutes)
 *    - String releaseDate
 *    - String genre
 *    - String ageRating
 *    - double rating
 *    - String status (NowShowing/ComingSoon)
 * 
 * 5. Cinema.java
 *    - int cinemaId
 *    - String name
 *    - String address
 *    - String location
 *    - double latitude
 *    - double longitude
 *    - String phoneNumber
 * 
 * 6. Showtime.java ⭐ PRIORITY HIGH
 *    - int showtimeId
 *    - int movieId
 *    - int cinemaId
 *    - String auditoriumName
 *    - String startTime
 *    - String endTime
 *    - double basePrice
 *    - int availableSeats
 * 
 * 7. Seat.java ⭐ PRIORITY HIGH
 *    - int seatId
 *    - String seatNumber (A1, B2, etc.)
 *    - String type (Regular/VIP/Couple)
 *    - String status (Available/Booked/Selected)
 *    - double price
 *    - String row
 *    - int column
 * 
 * 8. Booking.java ⭐ PRIORITY HIGH
 *    - int bookingId
 *    - int userId
 *    - int showtimeId
 *    - List<Integer> seatIds
 *    - List<Integer> comboIds
 *    - double totalPrice
 *    - String status (Pending/Confirmed/Completed/Cancelled)
 *    - String bookingDate
 *    - Movie movie (nested)
 *    - Cinema cinema (nested)
 *    - List<Seat> seats (nested)
 * 
 * 9. Combo.java
 *    - int comboId
 *    - String name
 *    - String description
 *    - double price
 *    - String imageUrl
 *    - boolean isAvailable
 * 
 * 10. Payment.java
 *     - int paymentId
 *     - int bookingId
 *     - String method (VNPay)
 *     - double amount
 *     - String status (Pending/Success/Failed)
 *     - String transactionId
 *     - String paymentDate
 * 
 * 11. VNPayResponse.java
 *     - String paymentUrl
 *     - String transactionId
 * 
 * 12. Voucher.java
 *     - int voucherId
 *     - String code
 *     - String description
 *     - String discountType (Percentage/Fixed)
 *     - double discountValue
 *     - double maxDiscount
 *     - String expiryDate
 * 
 * 13. VoucherValidationResponse.java
 *     - boolean isValid
 *     - double discountAmount
 *     - String message
 * 
 * 14. Review.java
 *     - int reviewId
 *     - int userId
 *     - int movieId
 *     - String userName
 *     - int rating (1-5)
 *     - String comment
 *     - String reviewDate
 * 
 * 15. User.java
 *     - int userId
 *     - String email
 *     - String fullName
 *     - String phoneNumber
 *     - String avatarUrl
 *     - String dateOfBirth
 *     - String gender
 * 
 * ============================================
 * REQUEST MODELS (data/models/request/)
 * ============================================
 * 
 * 1. LoginRequest.java ⭐ PRIORITY HIGH
 *    - String email
 *    - String password
 * 
 * 2. RegisterRequest.java ⭐ PRIORITY HIGH
 *    - String email
 *    - String password
 *    - String fullName
 *    - String phoneNumber
 * 
 * 3. RefreshTokenRequest.java
 *    - String refreshToken
 * 
 * 4. ForgotPasswordRequest.java
 *    - String email
 * 
 * 5. ChangePasswordRequest.java
 *    - String oldPassword
 *    - String newPassword
 * 
 * 6. CreateBookingRequest.java ⭐ PRIORITY HIGH
 *    - int showtimeId
 *    - List<Integer> seatIds
 *    - List<Integer> comboIds
 *    - String voucherCode (optional)
 * 
 * 7. ReserveSeatRequest.java
 *    - int showtimeId
 *    - List<Integer> seatIds
 * 
 * 8. CreatePaymentRequest.java
 *    - int bookingId
 *    - String returnUrl
 * 
 * 9. ValidateVoucherRequest.java
 *    - String voucherCode
 *    - double totalAmount
 * 
 * 10. CreateReviewRequest.java
 *     - int movieId
 *     - int rating
 *     - String comment
 * 
 * 11. UpdateProfileRequest.java
 *     - String fullName
 *     - String phoneNumber
 *     - String dateOfBirth
 *     - String gender
 * 
 * 12. UploadAvatarRequest.java
 *     - String base64Image
 * 
 * ============================================
 * TEMPLATE CODE FOR MODEL CLASS
 * ============================================
 * 
 * public class ModelName {
 *     private DataType fieldName;
 *     
 *     // Constructor
 *     public ModelName() {}
 *     
 *     public ModelName(DataType fieldName) {
 *         this.fieldName = fieldName;
 *     }
 *     
 *     // Getter
 *     public DataType getFieldName() {
 *         return fieldName;
 *     }
 *     
 *     // Setter
 *     public void setFieldName(DataType fieldName) {
 *         this.fieldName = fieldName;
 *     }
 * }
 * 
 * ============================================
 * ASSIGNMENT SUGGESTION
 * ============================================
 * 
 * Developer 1: Authentication models (Login, Register, Token)
 * Developer 2: Movie & Cinema models
 * Developer 3: Booking & Seat models
 * Developer 4: Payment & Voucher models
 * Developer 5: Review & User models
 */
public class _TODO_MODELS_README {
    // This is a placeholder file for documentation
    // Delete this file after all models are created
}
