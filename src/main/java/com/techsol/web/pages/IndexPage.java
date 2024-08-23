package com.techsol.web.pages;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class IndexPage extends WebPage {
    public IndexPage() {
        add(new BookmarkablePageLink<Void>("homeLink", HomePage.class));
        add(new BookmarkablePageLink<Void>("uploadLink", UploadPage.class));
        add(new Label("footerText", "© Site Server"));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        System.out.println("IndexPage initialized");
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forUrl("/java-site-server/css/styles.css"));
    }
}
