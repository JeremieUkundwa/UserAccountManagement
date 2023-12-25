package account.mgt.useraccountmanagment.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        System.out.println("=============Reached on successHandler=========");
        String redirectURL = request.getContextPath();

        if (userDetails.hasRole("ADMIN")) {
            redirectURL = "/account/";
        } else if (userDetails.hasRole("NORMAL")) {
            redirectURL = "/user/";
        }
        response.sendRedirect(redirectURL);
    }
}
