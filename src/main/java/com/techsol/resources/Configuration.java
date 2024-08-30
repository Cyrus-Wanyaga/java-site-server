package com.techsol.resources;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.json.JSONObject;

import com.techsol.metrics.TrafficMonitor;
import com.techsol.services.SiteStarterService;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/configure")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 100)
public class Configuration {
    @Inject
    private SiteStarterService siteStarterService;

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private HttpServletRequest servletRequest;

    @GET
    @Path("/applicationPort")
    @Produces(MediaType.APPLICATION_JSON)
    public Response applicationPort(@QueryParam("port") String port) {
        JSONObject responseObject = new JSONObject();

        Method method = resourceInfo.getResourceMethod();
        String methodName = null;
        String endpoint = null;
        String httpMethod = null;

        if (method != null) {
            methodName = method.getName();
        }

        if (servletRequest != null) {
            endpoint = servletRequest.getRequestURI();
            httpMethod = servletRequest.getMethod();
        }

        if (port == null || port.isEmpty()) {
            responseObject.put("statusMessage", "Port not provided");

            TrafficMonitor.logAPIRequests(methodName, httpMethod, endpoint, "400");
            return Response.status(400).entity(responseObject.toString()).build();
        }

        if (siteStarterService.isPortAvailable(Integer.parseInt(port))) {
            responseObject.put("statusMessage", "Port is available");

            TrafficMonitor.logAPIRequests(methodName, httpMethod, endpoint, "200");
            return Response.ok(responseObject.toString()).build();
        } else {
            responseObject.put("statusMessage", "Port " + port + " is not available");

            TrafficMonitor.logAPIRequests(methodName, httpMethod, endpoint, "400");
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

        Method method = resourceInfo.getResourceMethod();
        String methodName = null;
        String endpoint = null;
        String httpMethod = null;

        if (method != null) {
            methodName = method.getName();
        }

        if (servletRequest != null) {
            endpoint = servletRequest.getRequestURI();
            httpMethod = servletRequest.getMethod();
        }

        for (Attachment attachment : attachments) {
            if (attachment == null) {
                responseObject.put("statusMessage", "Please check the request payload");

                TrafficMonitor.logAPIRequests(methodName, httpMethod, endpoint, "400");
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

            TrafficMonitor.logAPIRequests(methodName, httpMethod, endpoint, "400");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseObject.toString()).build();
        } else if (fileInputStream == null) {
            responseObject.put("statusMessage", "Site files not provided");

            TrafficMonitor.logAPIRequests(methodName, httpMethod, endpoint, "400");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseObject.toString()).build();
        } else if (siteName == null) {
            responseObject.put("statusMessage", "Site name not provided");

            TrafficMonitor.logAPIRequests(methodName, httpMethod, endpoint, "400");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseObject.toString()).build();
        } else if (port == null) {
            responseObject.put("statusMessage", "Port not provided");

            TrafficMonitor.logAPIRequests(methodName, httpMethod, endpoint, "400");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseObject.toString()).build();
        }

        if (!siteStarterService.uploadFiles(fileInputStream, siteName)) {
            responseObject.put("statusMessage", "Failed to upload site files");

            TrafficMonitor.logAPIRequests(methodName, httpMethod, endpoint, "500");
            return Response.serverError().entity(responseObject.toString()).build();
        }

        String startServerResponse = siteStarterService.startServer(Integer.parseInt(port));

        if (startServerResponse.isEmpty()) {
            responseObject.put("statusMessage", "Failed to start server");

            TrafficMonitor.logAPIRequests(methodName, httpMethod, endpoint, "500");
            return Response.serverError().entity(responseObject.toString()).build();
        }

        responseObject.put("statusMessage", "Started server on port " + port + " successfully");

        TrafficMonitor.logAPIRequests(methodName, httpMethod, endpoint, "200");
        return Response.ok(responseObject.toString()).build();
    }
}
