import com.example.authservice.classes.AuthRequest;
import com.example.authservice.entity.UserInfo;
import com.example.authservice.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddNewUser() {
        // Mock data
        UserInfo userInfo = new UserInfo();

        when(authService.saveUser(userInfo)).thenReturn("User saved successfully");

        // Test
        String response = authController.addNewUser(userInfo);

        // Assertions
        assertEquals("User saved successfully", response);
        verify(authService, times(1)).saveUser(userInfo);
    }

    @Test
    void testGetToken() {
        // Mock data
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testuser");
        authRequest.setPassword("testpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(authService.generateToken(authRequest.getUsername())).thenReturn("generatedtoken");

        // Test
        String response = authController.getToken(authRequest);

        // Assertions
        assertEquals("generatedtoken", response);
        verify(authService, times(1)).generateToken(authRequest.getUsername());
    }

    @Test
    void testGetTokenInvalidAccess() {
        // Mock data
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("invaliduser");
        authRequest.setPassword("invalidpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("invalid access"));

        // Test and Assertions
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authController.getToken(authRequest));
        assertEquals("invalid access", exception.getMessage());
        verify(authService, never()).generateToken(anyString());
    }

    @Test
    void testValidateToken() {
        // Mock data
        String token = "validtoken";

        // Test
        String response = authController.validateToken(token);

        // Assertions
        assertEquals("valid", response);
        verify(authService, times(1)).validateToken(token);
    }
}
