package com.tripmeter.controllers;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Project: rest
 * User: Rajiv R. Nair
 * Created: 10/27/13 1:41 PM
 *
 * TODO: 1. move hard-coded values into props
 * TODO: 2. change from MaV to String
 */
@Controller
public class LoginController {

    private static final String REDIRECT_URI = "https://localhost:8443/trip/redirect";

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView loginModelView = new ModelAndView("login");
        loginModelView.addObject("init", "TripMeter");
        return loginModelView;
    }

    @RequestMapping(value = "authorise", method = RequestMethod.POST)
    public ModelAndView authorise(@RequestParam String email) throws OAuthSystemException {
        try {
            OAuthClientRequest request = OAuthClientRequest
                    .authorizationLocation(OAuthProviderType.FACEBOOK.getAuthzEndpoint())
                    .setClientId("607289622646284")
                    .setRedirectURI(REDIRECT_URI)
                    .setResponseType(ResponseType.CODE.toString())
                    .setScope("email user_about_me")
                    .setState(email)
                    .buildQueryMessage();

            return new ModelAndView(new RedirectView(request.getLocationUri()));
        } catch (Exception e) {
            e.printStackTrace();
            ModelAndView view = new ModelAndView("login");
            view.addObject("error", e.getMessage());
            return view;
        }
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public ModelAndView handleRedirect(HttpServletRequest request) {
        OAuthAuthzResponse oar = null;
        ModelAndView authCodeView = new ModelAndView("login");
        try {
            oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
            authCodeView.addObject("authCode", oar.getCode());
            authCodeView.addObject("state", oar.getState());

        } catch (OAuthProblemException e) {
            e.printStackTrace();
            StringBuffer sb = new StringBuffer();
            sb.append("</br>");
            sb.append("Error code: ").append(e.getError()).append("</br>");
            sb.append("Error description: ").append(e.getDescription()).append("</br>");
            sb.append("Error uri: ").append(e.getUri()).append("</br>");
            sb.append("State: ").append(e.getState()).append("</br>");
            authCodeView.addObject("error", sb.toString());
        }
        return authCodeView;
    }

    @RequestMapping(value = "get_token", method = RequestMethod.POST)
    public ModelAndView getAuthToken(@RequestParam String authCode) {
        ModelAndView tokenView = new ModelAndView("login");
        OAuthClientRequest request = null;
        try {
            request = OAuthClientRequest
                    .tokenLocation(OAuthProviderType.FACEBOOK.getTokenEndpoint())
                    .setClientId("607289622646284")
                    .setClientSecret("7393be389e3d1a704295d5f52e94d884")
                    .setRedirectURI(REDIRECT_URI)
                    .setCode(authCode)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .buildBodyMessage();

            OAuthClient client = new OAuthClient(new URLConnectionClient());

            OAuthAccessTokenResponse oauthResponse = null;
            Class<? extends OAuthAccessTokenResponse> cl = GitHubTokenResponse.class;

            oauthResponse = client.accessToken(request, cl);

            tokenView.addObject("accessToken", oauthResponse.getAccessToken());
            tokenView.addObject("expiresIn", oauthResponse.getExpiresIn());

        } catch (OAuthSystemException e) {
            e.printStackTrace();
            tokenView.addObject("error", e.getMessage());
        } catch (OAuthProblemException e) {
            e.printStackTrace();
            StringBuffer sb = new StringBuffer();
            sb.append("</br>");
            sb.append("Error code: ").append(e.getError()).append("</br>");
            sb.append("Error description: ").append(e.getDescription()).append("</br>");
            sb.append("Error uri: ").append(e.getUri()).append("</br>");
            sb.append("State: ").append(e.getState()).append("</br>");
            tokenView.addObject("error", sb.toString());
        }
        return tokenView;
    }

    @RequestMapping(value = "get_details", method = RequestMethod.POST)
    public ModelAndView getDetails(@RequestParam String accessToken) {
        ModelAndView detailsView = new ModelAndView("login");

        try {
            OAuthClientRequest request = new OAuthBearerClientRequest("https://graph.facebook.com/me").setAccessToken(accessToken).buildQueryMessage();

            OAuthClient client = new OAuthClient(new URLConnectionClient());
            OAuthResourceResponse resourceResponse = client.resource(request, OAuth.HttpMethod.GET, OAuthResourceResponse.class);

            if (resourceResponse.getResponseCode() == 200) {
                detailsView.addObject("details", resourceResponse.getBody());
            } else {
                detailsView.addObject("error", "Could not access resource: " + resourceResponse.getResponseCode() + " " + resourceResponse.getBody());
            }
        } catch (OAuthSystemException e) {
            e.printStackTrace();
            detailsView.addObject("error", e.getMessage());
        } catch (OAuthProblemException e) {
            e.printStackTrace();
            detailsView.addObject("error", e.getMessage());
        }

        return detailsView;
    }
}