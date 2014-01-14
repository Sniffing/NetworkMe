package example.networkme.Handler;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.sola.instagram.InstagramSession;
import com.sola.instagram.auth.AccessToken;
import com.sola.instagram.auth.InstagramAuthentication;
import com.sola.instagram.exception.InstagramException;
import com.sola.instagram.model.Media;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ben
 */
public class InstagramHandler {

    String authUrl;
    InstagramSession session;


    String tokenString;

    public InstagramHandler() {
        try {
            InstagramAuthentication auth =  new InstagramAuthentication();

            String authUrl = auth.setRedirectUri("hyperlocalscheme://oauth/callback/instagram/")
                    .setClientSecret("8fb3843010dc4066a4169e7f0c90c3a4")
                    .setClientId("11e87e3a24894b5e84711a015816e9e9")
                    .getAuthorizationUri();

            String authUrl2 = authUrl.replace("code","token");
            System.out.println(authUrl2);

            this.authUrl = authUrl2;
        } catch (InstagramException ex) {
            Logger.getLogger(InstagramHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getAuthorizationUrl() {
        return authUrl;
    }

    public void setToken(String tokenString) {
        AccessToken token = new AccessToken(tokenString);
        session = new InstagramSession(token);
    }

    public List<URL> getUrlListForLocation(Object latitude, Object longitude) {

        List<URL> urlList = new ArrayList<URL>();

        try {

            List<Media> mediaList = session.searchMedia(latitude, longitude, null, null, 1000);

            for(Media media : mediaList ){
                Media.Image image = media.getStandardResolutionImage();
                String uri = image.getUri();
                URL url = new URL(uri);

                urlList.add(url);
            }

        } catch (Exception ex) {
            Logger.getLogger(InstagramHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return urlList;
    }


    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public boolean isLoggedin(){
        return (tokenString != null);
    }



}
