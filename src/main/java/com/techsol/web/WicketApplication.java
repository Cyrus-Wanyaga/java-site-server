package com.techsol.web;

import com.techsol.web.pages.HomePage;
import com.techsol.web.pages.UploadPage;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class WicketApplication extends WebApplication {
    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

    @Override
    protected void init() {
        super.init();
        System.out.println("Wicket application initialized");
        mountPage("/home", HomePage.class);
        mountPage("/upload", UploadPage.class);
    }
}
