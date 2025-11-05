package com.example.cinemabookingsystemfe.data.api;

import android.os.Handler;
import android.os.Looper;

import com.example.cinemabookingsystemfe.data.models.request.LoginRequest;
import com.example.cinemabookingsystemfe.data.models.request.RegisterRequest;
import com.example.cinemabookingsystemfe.data.models.response.ApiResponse;
import com.example.cinemabookingsystemfe.data.models.response.LoginResponse;
import com.example.cinemabookingsystemfe.data.models.response.MovieDetailResponse;
import com.example.cinemabookingsystemfe.data.models.response.MovieItem;
import com.example.cinemabookingsystemfe.data.models.response.RegisterResponse;
import com.example.cinemabookingsystemfe.data.models.response.SearchMovieData;
import com.example.cinemabookingsystemfe.data.models.response.User;
import com.example.cinemabookingsystemfe.data.models.response.showtimes.Auditorium;
import com.example.cinemabookingsystemfe.data.models.response.showtimes.CinemaShowtimes;
import com.example.cinemabookingsystemfe.data.models.response.showtimes.Showtime;
import com.example.cinemabookingsystemfe.data.models.response.showtimes.ShowtimesDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Mock API Service for testing without backend
 * TODO: Remove this when real backend is ready
 */
public class MockApiService {
    
    private static final int MOCK_DELAY_MS = 1000; // Simulate network delay
    
    /**
     * Mock login - accepts any username/password
     */
    public static void login(LoginRequest request, ApiCallback<ApiResponse<LoginResponse>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // NOTE: Mock disabled - using real API now
            // Mock success response with nested user structure (matching backend format)
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken("mock_jwt_token_" + System.currentTimeMillis());
            loginResponse.setRefreshToken("mock_refresh_token_" + System.currentTimeMillis());
            loginResponse.setExpiresAt("2025-12-31T23:59:59");
            
            // Create nested user data (matching backend format)
            LoginResponse.UserData userData = new LoginResponse.UserData();
            userData.setUserId(1);
            userData.setFullName("Mock User");
            userData.setEmail(request.getEmail());
            userData.setPhoneNumber("0123456789");
            userData.setRoleId(1);
            userData.setRoleName("Customer");
            loginResponse.setUser(userData);
            
            ApiResponse<LoginResponse> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Login successful");
            response.setData(loginResponse);
            
            callback.onSuccess(response);
        }, MOCK_DELAY_MS);
    }
    
    /**
     * Mock register
     */
    public static void register(RegisterRequest request, ApiCallback<ApiResponse<RegisterResponse>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // NOTE: Mock disabled - using real API now
            // Mock success response with updated request fields
            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setUserId(2);
            registerResponse.setFullname(request.getFullname());
            registerResponse.setEmail(request.getEmail());
            registerResponse.setPhone(request.getPhone());
            registerResponse.setRoleName("Customer");
            
            ApiResponse<RegisterResponse> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Registration successful");
            response.setData(registerResponse);
            
            callback.onSuccess(response);
        }, MOCK_DELAY_MS);
    }
    
    /**
     * Mock forgot password - always success
     */
    public static void forgotPassword(String email, ApiCallback<ApiResponse<Void>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Đã gửi email hướng dẫn đặt lại mật khẩu đến " + email);
            response.setData(null);
            
            callback.onSuccess(response);
        }, MOCK_DELAY_MS);
    }
    
    /**
     * Mock logout - always success
     */
    public static void logout(ApiCallback<ApiResponse<Void>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Đăng xuất thành công");
            response.setData(null);
            
            callback.onSuccess(response);
        }, MOCK_DELAY_MS);
    }



    public static void getMovieDetail(int movieId, ApiCallback<MovieDetailResponse> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            MovieDetailResponse mockMovie = new MovieDetailResponse();
            Date releaseDate;
            
            // Return different movies based on movieId
            switch (movieId) {
                case 1: // Avatar: The Way of Water
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2022-12-16");
                    } catch (ParseException e) {
                        releaseDate = new Date();
                    }
                    mockMovie.setMovieId(1);
                    mockMovie.setTitle("Avatar: The Way of Water");
                    mockMovie.setOverview("Set more than a decade after the events of the first film, Avatar: The Way of Water begins to tell the story of the Sully family, the trouble that follows them, the lengths they go to keep each other safe, the battles they fight to stay alive, and the tragedies they endure.");
                    mockMovie.setDuration(192);
                    mockMovie.setDirector("James Cameron");
                    mockMovie.setTrailerUrl("https://www.youtube.com/watch?v=d9MyW72ELq0");
                    mockMovie.setReleaseDate(releaseDate);
                    mockMovie.setPosterUrl("https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg");
                    mockMovie.setCountry("USA");
                    mockMovie.setAgeRating("PG-13");
                    mockMovie.setGenre("Action, Adventure, Fantasy");
                    mockMovie.setRating(8.5);
                    mockMovie.setTotalReviews(1200);
                    mockMovie.setTotalShowtimes(15);
                    mockMovie.setBackdropUrl("https://image.tmdb.org/t/p/original/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg");
                    mockMovie.setStatus("NowShowing");
                    break;
                    
                case 2: // Black Panther: Wakanda Forever
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2022-11-11");
                    } catch (ParseException e) {
                        releaseDate = new Date();
                    }
                    mockMovie.setMovieId(2);
                    mockMovie.setTitle("Black Panther: Wakanda Forever");
                    mockMovie.setOverview("Queen Ramonda, Shuri, M'Baku, Okoye and the Dora Milaje fight to protect their nation from intervening world powers in the wake of King T'Challa's death. As the Wakandans strive to embrace their next chapter, the heroes must band together with the help of War Dog Nakia and Everett Ross.");
                    mockMovie.setDuration(161);
                    mockMovie.setDirector("Ryan Coogler");
                    mockMovie.setTrailerUrl("https://www.youtube.com/watch?v=_Z3QKkl1WyM");
                    mockMovie.setReleaseDate(releaseDate);
                    mockMovie.setPosterUrl("https://image.tmdb.org/t/p/w500/sv1xJUazXeYqALzczSZ3O6nkH75.jpg");
                    mockMovie.setCountry("USA");
                    mockMovie.setAgeRating("PG-13");
                    mockMovie.setGenre("Action, Adventure, Drama");
                    mockMovie.setRating(8.2);
                    mockMovie.setTotalReviews(980);
                    mockMovie.setTotalShowtimes(12);
                    mockMovie.setBackdropUrl("https://image.tmdb.org/t/p/original/yYrvN5WFeGYjJnRzhY0QXuo4Isw.jpg");
                    mockMovie.setStatus("NowShowing");
                    break;
                    
                case 3: // Top Gun: Maverick
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2022-05-27");
                    } catch (ParseException e) {
                        releaseDate = new Date();
                    }
                    mockMovie.setMovieId(3);
                    mockMovie.setTitle("Top Gun: Maverick");
                    mockMovie.setOverview("After thirty years, Maverick is still pushing the envelope as a top naval aviator, but must confront ghosts of his past when he leads TOP GUN's elite graduates on a mission that demands the ultimate sacrifice from those who choose to fly it.");
                    mockMovie.setDuration(131);
                    mockMovie.setDirector("Joseph Kosinski");
                    mockMovie.setTrailerUrl("https://www.youtube.com/watch?v=giXco2jaZ_4");
                    mockMovie.setReleaseDate(releaseDate);
                    mockMovie.setPosterUrl("https://image.tmdb.org/t/p/w500/62HCnUTziyWcpDaBO2i1DX17ljH.jpg");
                    mockMovie.setCountry("USA");
                    mockMovie.setAgeRating("PG-13");
                    mockMovie.setGenre("Action, Drama");
                    mockMovie.setRating(8.8);
                    mockMovie.setTotalReviews(1500);
                    mockMovie.setTotalShowtimes(18);
                    mockMovie.setBackdropUrl("https://image.tmdb.org/t/p/original/odJ4hx6g6vBt4lBWKFD1tI8WS4x.jpg");
                    mockMovie.setStatus("NowShowing");
                    break;
                
                case 4: // Everything Everywhere All at Once
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2022-03-25");
                    } catch (ParseException e) {
                        releaseDate = new Date();
                    }
                    mockMovie.setMovieId(4);
                    mockMovie.setTitle("Everything Everywhere All at Once");
                    mockMovie.setOverview("An aging Chinese immigrant is swept up in an insane adventure, where she alone can save the world by exploring other universes connecting with the lives she could have led.");
                    mockMovie.setDuration(139);
                    mockMovie.setDirector("Daniel Kwan");
                    mockMovie.setTrailerUrl("https://www.youtube.com/watch?v=wxN1T1uxQ2g");
                    mockMovie.setReleaseDate(releaseDate);
                    mockMovie.setPosterUrl("https://image.tmdb.org/t/p/w500/w3LxiVYdWWRvEVdn5RYq6jIqkb1.jpg");
                    mockMovie.setCountry("USA");
                    mockMovie.setAgeRating("R");
                    mockMovie.setGenre("Action, Adventure, Comedy");
                    mockMovie.setRating(8.7);
                    mockMovie.setTotalReviews(423);
                    mockMovie.setTotalShowtimes(14);
                    mockMovie.setBackdropUrl("https://image.tmdb.org/t/p/original/xDMIl84Qo5Tsu62c9DGWhmPI67A.jpg");
                    mockMovie.setStatus("NowShowing");
                    break;
                
                case 5: // The Batman
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2022-03-04");
                    } catch (ParseException e) {
                        releaseDate = new Date();
                    }
                    mockMovie.setMovieId(5);
                    mockMovie.setTitle("The Batman");
                    mockMovie.setOverview("In his second year of fighting crime, Batman uncovers corruption in Gotham City that connects to his own family while facing a serial killer known as the Riddler.");
                    mockMovie.setDuration(176);
                    mockMovie.setDirector("Matt Reeves");
                    mockMovie.setTrailerUrl("https://www.youtube.com/watch?v=mqqft2x_Aa4");
                    mockMovie.setReleaseDate(releaseDate);
                    mockMovie.setPosterUrl("https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg");
                    mockMovie.setCountry("USA");
                    mockMovie.setAgeRating("PG-13");
                    mockMovie.setGenre("Action, Crime, Drama");
                    mockMovie.setRating(8.3);
                    mockMovie.setTotalReviews(567);
                    mockMovie.setTotalShowtimes(16);
                    mockMovie.setBackdropUrl("https://image.tmdb.org/t/p/original/b0PlSFdDwbyK0cf5RxwDpaOJQvQ.jpg");
                    mockMovie.setStatus("NowShowing");
                    break;
                
                case 6: // Spider-Man: No Way Home  
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2021-12-17");
                    } catch (ParseException e) {
                        releaseDate = new Date();
                    }
                    mockMovie.setMovieId(6);
                    mockMovie.setTitle("Spider-Man: No Way Home");
                    mockMovie.setOverview("Peter Parker is unmasked and no longer able to separate his normal life from the high-stakes of being a super-hero. When he asks for help from Doctor Strange, the stakes become even more dangerous.");
                    mockMovie.setDuration(148);
                    mockMovie.setDirector("Jon Watts");
                    mockMovie.setTrailerUrl("https://www.youtube.com/watch?v=JfVOs4VSpmA");
                    mockMovie.setReleaseDate(releaseDate);
                    mockMovie.setPosterUrl("https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg");
                    mockMovie.setCountry("USA");
                    mockMovie.setAgeRating("PG-13");
                    mockMovie.setGenre("Action, Adventure, Sci-Fi");
                    mockMovie.setRating(8.9);
                    mockMovie.setTotalReviews(789);
                    mockMovie.setTotalShowtimes(20);
                    mockMovie.setBackdropUrl("https://image.tmdb.org/t/p/original/iQFcwSGbZXMkeyKrxbPnwnRo5fl.jpg");
                    mockMovie.setStatus("NowShowing");
                    break;
                
                // Coming Soon Movies (movieId 7-12) - status: "ComingSoon"
                case 7: // Guardians of the Galaxy Vol. 3
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2023-05-05");
                    } catch (ParseException e) {
                        releaseDate = new Date();
                    }
                    mockMovie.setMovieId(7);
                    mockMovie.setTitle("Guardians of the Galaxy Vol. 3");
                    mockMovie.setOverview("Peter Quill, still reeling from the loss of Gamora, must rally his team around him to defend the universe along with protecting one of their own. A mission that, if not completed successfully, could quite possibly lead to the end of the Guardians as we know them.");
                    mockMovie.setDuration(150);
                    mockMovie.setDirector("James Gunn");
                    mockMovie.setTrailerUrl("https://www.youtube.com/watch?v=u3V5KDHRQvk");
                    mockMovie.setReleaseDate(releaseDate);
                    mockMovie.setPosterUrl("https://image.tmdb.org/t/p/w500/r2J02Z2OpNTctfOSN1Ydgii51I3.jpg");
                    mockMovie.setCountry("USA");
                    mockMovie.setAgeRating("PG-13");
                    mockMovie.setGenre("Action, Adventure, Comedy");
                    mockMovie.setRating(8.6);
                    mockMovie.setTotalReviews(0);
                    mockMovie.setTotalShowtimes(0);
                    mockMovie.setBackdropUrl("https://image.tmdb.org/t/p/original/5i3ghCVVCUHeavXzVsF8n5pVleR.jpg");
                    mockMovie.setStatus("ComingSoon");
                    break;
                    
                case 8: // The Flash
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2023-06-16");
                    } catch (ParseException e) {
                        releaseDate = new Date();
                    }
                    mockMovie.setMovieId(8);
                    mockMovie.setTitle("The Flash");
                    mockMovie.setOverview("When Barry Allen uses his super speed to change the past, he creates a world without super heroes, forcing him to race for his life in order to save the future.");
                    mockMovie.setDuration(144);
                    mockMovie.setDirector("Andy Muschietti");
                    mockMovie.setTrailerUrl("https://www.youtube.com/watch?v=hebWYacbdvc");
                    mockMovie.setReleaseDate(releaseDate);
                    mockMovie.setPosterUrl("https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg");
                    mockMovie.setCountry("USA");
                    mockMovie.setAgeRating("PG-13");
                    mockMovie.setGenre("Action, Adventure, Sci-Fi");
                    mockMovie.setRating(7.8);
                    mockMovie.setTotalReviews(0);
                    mockMovie.setTotalShowtimes(0);
                    mockMovie.setBackdropUrl("https://image.tmdb.org/t/p/original/yF1eOkaYvwiORauRCPWznV9xVvi.jpg");
                    mockMovie.setStatus("ComingSoon");
                    break;
                    
                case 9: // Indiana Jones and the Dial of Destiny
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2023-06-30");
                    } catch (ParseException e) {
                        releaseDate = new Date();
                    }
                    mockMovie.setMovieId(9);
                    mockMovie.setTitle("Indiana Jones and the Dial of Destiny");
                    mockMovie.setOverview("Finding himself in a new era, approaching retirement, Indy wrestles with fitting into a world that seems to have outgrown him. But as the tentacles of an all-too-familiar evil return in the form of an old rival, Indy must don his hat and pick up his whip once more.");
                    mockMovie.setDuration(154);
                    mockMovie.setDirector("James Mangold");
                    mockMovie.setTrailerUrl("https://www.youtube.com/watch?v=ZUJcQ8rp_aE");
                    mockMovie.setReleaseDate(releaseDate);
                    mockMovie.setPosterUrl("https://image.tmdb.org/t/p/w500/Af4bXE63pVsb2FtbW8uYIyPBadD.jpg");
                    mockMovie.setCountry("USA");
                    mockMovie.setAgeRating("PG-13");
                    mockMovie.setGenre("Adventure, Action");
                    mockMovie.setRating(7.5);
                    mockMovie.setTotalReviews(0);
                    mockMovie.setTotalShowtimes(0);
                    mockMovie.setBackdropUrl("https://image.tmdb.org/t/p/original/8rpDcsfLJypbO6vREc0547VKqEv.jpg");
                    mockMovie.setStatus("ComingSoon");
                    break;
                    
                case 10: // Mission: Impossible – Dead Reckoning Part One
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2023-07-12");
                    } catch (ParseException e) {
                        releaseDate = new Date();
                    }
                    mockMovie.setMovieId(10);
                    mockMovie.setTitle("Mission: Impossible – Dead Reckoning Part One");
                    mockMovie.setOverview("Ethan Hunt and his IMF team embark on their most dangerous mission yet: To track down a terrifying new weapon that threatens all of humanity before it falls into the wrong hands.");
                    mockMovie.setDuration(163);
                    mockMovie.setDirector("Christopher McQuarrie");
                    mockMovie.setTrailerUrl("https://www.youtube.com/watch?v=avz06PDqDbM");
                    mockMovie.setReleaseDate(releaseDate);
                    mockMovie.setPosterUrl("https://image.tmdb.org/t/p/w500/NNxYkU70HPurnNCSiCjYAmacwm.jpg");
                    mockMovie.setCountry("USA");
                    mockMovie.setAgeRating("PG-13");
                    mockMovie.setGenre("Action, Thriller");
                    mockMovie.setRating(8.4);
                    mockMovie.setTotalReviews(0);
                    mockMovie.setTotalShowtimes(0);
                    mockMovie.setBackdropUrl("https://image.tmdb.org/t/p/original/628Dep6AxEtDxjZoGP78TsOxYbK.jpg");
                    mockMovie.setStatus("ComingSoon");
                    break;
                    
                case 11: // Oppenheimer
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2023-07-21");
                    } catch (ParseException e) {
                        releaseDate = new Date();
                    }
                    mockMovie.setMovieId(11);
                    mockMovie.setTitle("Oppenheimer");
                    mockMovie.setOverview("The story of J. Robert Oppenheimer's role in the development of the atomic bomb during World War II.");
                    mockMovie.setDuration(180);
                    mockMovie.setDirector("Christopher Nolan");
                    mockMovie.setTrailerUrl("https://www.youtube.com/watch?v=uYPbbksJxIg");
                    mockMovie.setReleaseDate(releaseDate);
                    mockMovie.setPosterUrl("https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg");
                    mockMovie.setCountry("USA");
                    mockMovie.setAgeRating("R");
                    mockMovie.setGenre("Drama, History");
                    mockMovie.setRating(9.0);
                    mockMovie.setTotalReviews(0);
                    mockMovie.setTotalShowtimes(0);
                    mockMovie.setBackdropUrl("https://image.tmdb.org/t/p/original/fm6KqXpk3M2HVveHwCrBSSBaO0V.jpg");
                    mockMovie.setStatus("ComingSoon");
                    break;
                    
                case 12: // Barbie
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2023-07-21");
                    } catch (ParseException e) {
                        releaseDate = new Date();
                    }
                    mockMovie.setMovieId(12);
                    mockMovie.setTitle("Barbie");
                    mockMovie.setOverview("Barbie and Ken are having the time of their lives in the colorful and seemingly perfect world of Barbie Land. However, when they get a chance to go to the real world, they soon discover the joys and perils of living among humans.");
                    mockMovie.setDuration(114);
                    mockMovie.setDirector("Greta Gerwig");
                    mockMovie.setTrailerUrl("https://www.youtube.com/watch?v=pBk4NYhWNMM");
                    mockMovie.setReleaseDate(releaseDate);
                    mockMovie.setPosterUrl("https://image.tmdb.org/t/p/w500/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg");
                    mockMovie.setCountry("USA");
                    mockMovie.setAgeRating("PG-13");
                    mockMovie.setGenre("Adventure, Comedy, Fantasy");
                    mockMovie.setRating(8.1);
                    mockMovie.setTotalReviews(0);
                    mockMovie.setTotalShowtimes(0);
                    mockMovie.setBackdropUrl("https://image.tmdb.org/t/p/original/nHf61UzkfFno5X1ofIhugCPus2R.jpg");
                    mockMovie.setStatus("ComingSoon");
                    break;
                    
                default:
                    // Return a generic movie for unknown IDs
                    try {
                        releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2024-01-01");
                    } catch (ParseException e) {
                        releaseDate = new Date();
                    }
                    mockMovie.setMovieId(movieId);
                    mockMovie.setTitle("Unknown Movie");
                    mockMovie.setOverview("Movie details not available.");
                    mockMovie.setDuration(120);
                    mockMovie.setDirector("Unknown");
                    mockMovie.setTrailerUrl("");
                    mockMovie.setReleaseDate(releaseDate);
                    mockMovie.setPosterUrl("");
                    mockMovie.setCountry("Unknown");
                    mockMovie.setAgeRating("N/A");
                    mockMovie.setGenre("Unknown");
                    mockMovie.setRating(0.0);
                    mockMovie.setTotalReviews(0);
                    mockMovie.setTotalShowtimes(0);
                    mockMovie.setBackdropUrl("");
                    mockMovie.setStatus("Unknown");
                    break;
            }

            callback.onSuccess(mockMovie);

        }, 800); // Giả lập delay ~800ms
    }


    public static void searchMovies(int page, int pageSize, String genre, String sort,
                                    ApiCallback<ApiResponse<SearchMovieData>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // Mock data - same as HomeViewModel for consistency
            List<MovieItem> allMovies = new ArrayList<>();
            
            // Now Showing Movies (movieId 1-6)
            allMovies.add(new MovieItem(1, "Avatar: The Way of Water",
                    "Set more than a decade after the events of the first film...",
                    192, "James Cameron",
                    "https://www.youtube.com/watch?v=d9MyW72ELq0",
                    "2022-12-16",
                    "https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg",
                    "USA", "8.5", "Action, Adventure, Fantasy"));
            
            allMovies.add(new MovieItem(2, "Black Panther: Wakanda Forever",
                    "Queen Ramonda, Shuri, M'Baku, Okoye and the Dora Milaje fight...",
                    161, "Ryan Coogler",
                    "https://www.youtube.com/watch?v=_Z3QKkl1WyM",
                    "2022-11-11",
                    "https://image.tmdb.org/t/p/w500/sv1xJUazXeYqALzczSZ3O6nkH75.jpg",
                    "USA", "8.2", "Action, Adventure, Drama"));
            
            allMovies.add(new MovieItem(3, "Top Gun: Maverick",
                    "After thirty years, Maverick is still pushing the envelope...",
                    131, "Joseph Kosinski",
                    "https://www.youtube.com/watch?v=giXco2jaZ_4",
                    "2022-05-27",
                    "https://image.tmdb.org/t/p/w500/62HCnUTziyWcpDaBO2i1DX17ljH.jpg",
                    "USA", "8.8", "Action, Drama"));
            
            allMovies.add(new MovieItem(4, "Everything Everywhere All at Once",
                    "An aging Chinese immigrant is swept up in an insane adventure...",
                    139, "Daniel Kwan",
                    "https://www.youtube.com/watch?v=wxN1T1uxQ2g",
                    "2022-03-25",
                    "https://image.tmdb.org/t/p/w500/w3LxiVYdWWRvEVdn5RYq6jIqkb1.jpg",
                    "USA", "8.7", "Action, Adventure, Comedy"));
            
            allMovies.add(new MovieItem(5, "The Batman",
                    "In his second year of fighting crime, Batman uncovers corruption...",
                    176, "Matt Reeves",
                    "https://www.youtube.com/watch?v=mqqft2x_Aa4",
                    "2022-03-04",
                    "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg",
                    "USA", "8.3", "Action, Crime, Drama"));
            
            allMovies.add(new MovieItem(6, "Spider-Man: No Way Home",
                    "Peter Parker is unmasked and no longer able to separate his normal life...",
                    148, "Jon Watts",
                    "https://www.youtube.com/watch?v=JfVOs4VSpmA",
                    "2021-12-17",
                    "https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
                    "USA", "8.9", "Action, Adventure, Sci-Fi"));
            
            // Coming Soon Movies (movieId 7-12)
            allMovies.add(new MovieItem(7, "Guardians of the Galaxy Vol. 3",
                    "Peter Quill, still reeling from the loss of Gamora...",
                    150, "James Gunn",
                    "https://www.youtube.com/watch?v=u3V5KDHRQvk",
                    "2023-05-05",
                    "https://image.tmdb.org/t/p/w500/r2J02Z2OpNTctfOSN1Ydgii51I3.jpg",
                    "USA", "8.6", "Action, Adventure, Comedy"));
            
            allMovies.add(new MovieItem(8, "The Flash",
                    "Barry Allen uses his super speed to change the past...",
                    144, "Andy Muschietti",
                    "https://www.youtube.com/watch?v=hebWYacbdvc",
                    "2023-06-16",
                    "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
                    "USA", "7.8", "Action, Adventure, Fantasy"));
            
            allMovies.add(new MovieItem(9, "Indiana Jones and the Dial of Destiny",
                    "Archaeologist Indiana Jones races against time...",
                    154, "James Mangold",
                    "https://www.youtube.com/watch?v=eQfMbSe7F2g",
                    "2023-06-30",
                    "https://image.tmdb.org/t/p/w500/Af4bXE63pVsb2FtbW8uYIyPBadD.jpg",
                    "USA", "7.5", "Action, Adventure"));
            
            allMovies.add(new MovieItem(10, "Mission: Impossible - Dead Reckoning",
                    "Ethan Hunt and his IMF team embark on their most dangerous mission...",
                    163, "Christopher McQuarrie",
                    "https://www.youtube.com/watch?v=avz06PDqDbM",
                    "2023-07-12",
                    "https://image.tmdb.org/t/p/w500/NNxYkU70HPurnNCSiCjYAmacwm.jpg",
                    "USA", "8.4", "Action, Thriller"));
            
            allMovies.add(new MovieItem(11, "Oppenheimer",
                    "The story of American scientist J. Robert Oppenheimer...",
                    180, "Christopher Nolan",
                    "https://www.youtube.com/watch?v=uYPbbksJxIg",
                    "2023-07-21",
                    "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
                    "USA", "9.0", "Biography, Drama, History"));
            
            allMovies.add(new MovieItem(12, "Barbie",
                    "Barbie and Ken are having the time of their lives in Barbie Land...",
                    114, "Greta Gerwig",
                    "https://www.youtube.com/watch?v=pBk4NYhWNMM",
                    "2023-07-21",
                    "https://image.tmdb.org/t/p/w500/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg",
                    "USA", "8.1", "Adventure, Comedy, Fantasy"));

            // Paging
            int totalItems = allMovies.size();
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, totalItems);
            List<MovieItem> paged = start < totalItems ? allMovies.subList(start, end) : new ArrayList<>();

            // Gói vào SearchMovieData
            SearchMovieData data = new SearchMovieData(paged, page, pageSize, totalPages, totalItems,
                    page < totalPages, page > 1);

            // Gói vào ApiResponse
            ApiResponse<SearchMovieData> response = new ApiResponse<>(true, "Movies retrieved successfully", data);

            // Callback
            callback.onSuccess(response);

        }, MOCK_DELAY_MS);
    }

    public static void getMovieShowtimes(int movieId, ApiCallback<ApiResponse<List<ShowtimesDate>>> callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                // --- Auditorium ---
                Auditorium auditorium1 = new Auditorium(5, "Cinema 3", 150);
                Auditorium auditorium2 = new Auditorium(6, "Cinema 4", 200);

                // --- Showtime list (2 suất chiếu) ---
                List<Showtime> showtimes = Arrays.asList(
                        new Showtime(
                                42,
                                "2025-11-05T10:30:00",
                                "2025-11-05T12:53:00",
                                100000,
                                "2D",
                                "Phụ đề",
                                auditorium1,
                                145
                        ),
                        new Showtime(
                                43,
                                "2025-11-05T14:00:00",
                                "2025-11-05T16:23:00",
                                120000,
                                "3D",
                                "Lồng tiếng",
                                auditorium2,
                                198
                        )
                );

                // --- Cinema chứa các suất chiếu ---
                CinemaShowtimes cinema = new CinemaShowtimes(
                        1,
                        "CGV Vincom Center",
                        "72 Le Thanh Ton, District 1",
                        "Ho Chi Minh City",
                        showtimes
                );

                // --- Date group (có thể có nhiều ngày sau này) ---
                ShowtimesDate showtimesDate = new ShowtimesDate(
                        "2025-11-05",
                        Collections.singletonList(cinema)
                );

                // --- List wrap (chuẩn cấu trúc API backend) ---
                List<ShowtimesDate> data = Collections.singletonList(showtimesDate);

                // --- Response ---
                ApiResponse<List<ShowtimesDate>> response = new ApiResponse<>(
                        true,
                        "Showtimes retrieved successfully",
                        data
                );

                callback.onSuccess(response);

            } catch (Exception e) {
                callback.onError("Mock showtimes error: " + e.getMessage());
            }
        }, 1000); // delay 1s cho giống gọi API thật
    }

}
