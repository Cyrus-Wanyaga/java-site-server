package com.techsol.web.pages;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.HtmlImportHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.MetaDataHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.StringHeaderItem;
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
        response.render(CssHeaderItem.forUrl("https://fonts.googleapis.com/css2?family=Rubik:ital,wght@0,300..900;1,300..900&display=swap"));
        response.render(CssHeaderItem.forUrl("https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"));
        response.render(JavaScriptHeaderItem.forUrl("https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"));
        response.render(JavaScriptHeaderItem.forUrl("https://cdn.jsdelivr.net/npm/gsap@3.12.5/dist/gsap.min.js"));
        response.render(JavaScriptHeaderItem.forUrl("https://cdn.jsdelivr.net/npm/gsap@3.12.5/dist/ScrollTrigger.min.js"));
        response.render(JavaScriptHeaderItem.forUrl("https://cdn.jsdelivr.net/npm/gsap@3.12.5/dist/ScrollToPlugin.min.js"));
        response.render(JavaScriptHeaderItem.forUrl("/java-site-server/js/animations.js"));
    }
}
