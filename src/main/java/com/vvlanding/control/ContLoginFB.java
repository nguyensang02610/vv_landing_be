package com.vvlanding.control;

import com.vvlanding.repo.RepoUser;
import com.vvlanding.security.CurrentUser;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/fb")
public class ContLoginFB {

    @Autowired
    RepoUser repoUser;

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String LOGIN_DIALOG_URL = "https://www.facebook.com/v2.12/dialog/oauth?auth_type=rerequest&client_id=APP_ID&redirect_uri=REDIRECT_URI&scope=public_profile,email,manage_pages,publish_pages,pages_messaging,pages_messaging_subscriptions,pages_show_list";
    private static final String APP_ID = "1165567707262717";
    private static final String REDIRECT_URI = "https://landapi.vipage.vn/vvlanding/api/fb/success";

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public RedirectView localRedirect(){
        RedirectView redirectView = new RedirectView();
        String dialog = LOGIN_DIALOG_URL;
        dialog = dialog.replaceAll("APP_ID", APP_ID);
        dialog = dialog.replace("REDIRECT_URI", REDIRECT_URI);
        redirectView.setUrl(dialog);
        return redirectView;
    }
    @RequestMapping(value = "/success",method = RequestMethod.GET)
    public RedirectView success(@CurrentUser UserPrincipal userPrincipal, @RequestParam("code") String code) throws URISyntaxException {
        RedirectView redirectView = new RedirectView();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://fbmess.vipage.vn/fb/login/success?code="+code;
        ResponseEntity<String> codeHttp = restTemplate.exchange(new URI(url), HttpMethod.GET, entity, String.class);
        if (!Objects.equals(codeHttp.getBody(), "400")&&codeHttp.getBody() != null){
            User user = repoUser.getOne(userPrincipal.getId());
            user.setFbId(codeHttp.getBody());
            repoUser.save(user);
            redirectView.setUrl("google.com?code=200");
            return redirectView;
        }
        redirectView.setUrl("google.com?code=400");
        return redirectView;
    }

}
