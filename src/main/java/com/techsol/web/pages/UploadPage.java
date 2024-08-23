package com.techsol.web.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;

public class UploadPage extends IndexPage {
    public UploadPage() {
        UploadForm form = new UploadForm("uploadForm");
        add(form);
    }

    public class UploadForm extends Form<UploadForm> {
        private String name;
        private Integer port;
        private FileUploadField fileUpload;

        public UploadForm(String id) {
            super(id);
            setModel(new CompoundPropertyModel<>(this));
            add(new TextField<>("name").setRequired(true));
            add(new NumberTextField<>("port", Integer.class).setRequired(true));
            add(fileUpload = new FileUploadField("fileUpload"));
            add(new AjaxButton("submit") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    // Here you would call your REST API to upload the site
                    // You can use Wicket's built-in HTTP client or a third-party library like Apache HttpClient
                    System.out.println("Uploading site : " + name + " at port : " + port);
                }
            });
        }
    }
}
