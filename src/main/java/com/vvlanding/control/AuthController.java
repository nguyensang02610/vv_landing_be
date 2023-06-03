package com.vvlanding.control;

import com.vvlanding.data.Resp;
import com.vvlanding.payload.JwtAuthenticationResponse;
import com.vvlanding.payload.LoginRequest;
import com.vvlanding.payload.SignUpRequest;
import com.vvlanding.repo.RepoUser;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.JwtTokenProvider;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.table.ShopUserRole;
import com.vvlanding.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    RepoUser repoUser;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/login/token")
    public ResponseEntity<?> loginWithToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String jwt = bearerToken.substring(7, bearerToken.length());
        User user = null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {

            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            Optional<User> optionalUser = repoUser.findById(userId);
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
            }
        }
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, user));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser( @RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateToken(authentication);
            UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, user));

        }catch (Exception e) {
            response.put("message", "Tài khoản không đúng");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    private User checkPhoneUser(String phone) {
        List<User> userOpt = repoUser.findByPhone(phone);
        if (userOpt.size() > 0) {
            return userOpt.get(0);
        }
        return null;
    }

    @PostMapping("/adduser")
    public Resp registerUser(@CurrentUser UserPrincipal currentUser,
                              @RequestBody SignUpRequest signUpRequest) {
        Resp resp = new Resp();
        try {
            User result = new User();
            String phone = signUpRequest.getPhone();
            String email = signUpRequest.getEmail();
            String title = signUpRequest.getTitle();
            String image = signUpRequest.getImage();
            String username = signUpRequest.getUsername();
            User user1 = checkPhoneUser(phone);
            if (user1 == null) {
                //Creating user's account if this table is null
                User user = new User(signUpRequest.getImage(), signUpRequest.getUsername(), signUpRequest.getPassword(), signUpRequest.getActive(), signUpRequest.getPhone(), signUpRequest.getEmail(), signUpRequest.getTitle());
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                user.setActive(true);
                user.setPhone(phone);
                user.setEmail(email);
                user.setTitle(title);
                user.setImage(image);
                user.setUsername(username);
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
                result = repoUser.save(user);
                resp.setData(result);
                resp.setSuccess(true);
                resp.setMsg("Thêm mới tài khoản thành công!");
            } else {
                resp.setSuccess(false);
                resp.setMsg("Không tạo được tài khoản - số điện thoại đã tồn tại");
            }
        } catch (Exception ex) {
            resp.setSuccess(false);
            resp.setMsg("Tài khoản này đã tồn tại ");

        }
        return resp;
    }


}

