package com.techsol.resources;

import com.techsol.services.SiteStarterService;
import jakarta.inject.Inject;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.json.JSONObject;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;
import java.util.List;

@Path("/configure")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 100)
public class Configuration {
    @Inject
    private SiteStarterService siteStarterService;

    @GET
    @Path("/applicationPort")
    @Produces(MediaType.APPLICATION_JSON)
    public Response applicationPort(@QueryParam("port") String port) {
        JSONObject responseObject = new JSONObject();

        if (port == null || port.isEmpty()) {
            responseObject.put("statusMessage", "Port not provided");
            return Response.status(400).entity(responseObject.toString()).build();
        }

        if (siteStarterService.isPortAvailable(Integer.parseInt(port))) {
            responseObject.put("statusMessage", "Port is available");
            return Response.ok(responseObject.toString()).build();
        } else {
            responseObject.put("statusMessage", "Port " + port + " is not available");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseObject.toString()).build();
        }
    }

    @POST
    @Path("/uploadZippedSiteFiles")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadZippedSiteFiles(MultipartBody multipartBody) {
        JSONObject responseObject = new JSONObject();
        List<Attachment> attachments = multipartBody.getAllAttachments();
        InputStream fileInputStream = null;
        String siteName = null;
        String port = null;

        for (Attachment attachment : attachments) {
            if (attachment == null) {
                responseObject.put("statusMessage", "Please check the request payload");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseObject.toString()).build();
            } else {
                String paramName = attachment.getContentDisposition().getParameters().get("name");
                switch (paramName) {
                    case "file" -> fileInputStream = attachment.getObject(InputStream.class);
                    case "siteName" -> siteName = attachment.getObject(String.class);
                    case "port" -> port = attachment.getObject(String.class);
                }
            }
        }

        if (fileInputStream == null && siteName == null && port == null) {
            responseObject.put("statusMessage", "Site files, site name, and port not provided");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseObject.toString()).build();
        } else if (fileInputStream == null) {
            responseObject.put("statusMessage", "Site files not provided");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseObject.toString()).build();
        } else if (siteName == null) {
            responseObject.put("statusMessage", "Site name not provided");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseObject.toString()).build();
        } else if (port == null) {
            responseObject.put("statusMessage", "Port not provided");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseObject.toString()).build();
        }

        if (!siteStarterService.uploadFiles(fileInputStream, siteName)) {
            responseObject.put("statusMessage", "Failed to upload site files");
            return Response.serverError().entity(responseObject.toString()).build();
        }

        String startServerResponse = siteStarterService.startServer(Integer.parseInt(port));

        if (startServerResponse.isEmpty()) {
            responseObject.put("statusMessage", "Failed to start server");
            return Response.serverError().entity(responseObject.toString()).build();
        }

        responseObject.put("statusMessage", "Started server on port " + port + " successfully");
        return Response.ok(responseObject.toString()).build();
    }
}
