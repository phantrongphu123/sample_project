package com.mgr.api.config;

import com.mgr.api.service.impl.UserServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Objects;

public class UserTokenGranter extends AbstractTokenGranter {
    private final UserServiceImpl userService;

    public UserTokenGranter(AuthenticationManager authManager, AuthorizationServerTokenServices tokenServices,
                            ClientDetailsService clientDetails, OAuth2RequestFactory requestFactory,
                            UserServiceImpl userService) {
        super(tokenServices, clientDetails, requestFactory, SecurityConstant.GRANT_TYPE_USER);
        this.userService = userService;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = tokenRequest.getRequestParameters();
        String username = parameters.get("username");
        String password = parameters.get("password");

        // 1. Lấy thông tin xác thực người dùng từ Service
        Authentication userAuth = userService.authenticateForUserType(username, password, 2); // kind = 2

        // 2. Tạo OAuth2Request từ client và request hiện tại
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);

        // 3. Kết hợp cả hai để tạo OAuth2Authentication (Giải quyết lỗi của bạn)
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
