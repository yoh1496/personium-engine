/**
 * Personium
 * Copyright 2014-2021 - Personium Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.personium.engine.rs;

import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

import io.personium.engine.PersoniumEngineException;
import io.personium.engine.utils.PersoniumEngineConfig;

/**
 * JAX-RS resource class for Personium-Engine status API. (/__status)
 */
@Path("__status")
public class StatusResource {
    /** Logger Object. */
    private static Log log = LogFactory.getLog(AbstractService.class);

    /**
     * GET method processing.
     * @return JAS-RS Response
     */
    @SuppressWarnings("unchecked")
    @GET
    @Produces("application/json")
    public Response get() {
        StringBuilder sb = new StringBuilder();

        // List of properties
        Properties props = PersoniumEngineConfig.getProperties();
        JSONObject responseJson = new JSONObject();
        JSONObject propertiesJson = new JSONObject();
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            propertiesJson.put(key, value);
        }
        responseJson.put("properties", propertiesJson);

        sb.append(responseJson.toJSONString());
        return Response.status(HttpStatus.SC_OK).entity(sb.toString()).build();
    }

    /**
     * POST method processing.
     * @param path リソース名
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param is リクエストストリームオブジェクト
     * @return JAX-RS Response object
     */
    @POST
    public final Response post(@PathParam("id") final String path,
            @Context final HttpServletRequest request,
            @Context final HttpServletResponse response,
            final InputStream is) {
        return run(path, request, response, is);
    }

    /**
     * Service実行.
     * @param path リソース名
     * @param req Requestオブジェクト
     * @param res Responseオブジェクト
     * @param is リクエストストリームオブジェクト
     * @return Response
     */
    public final Response run(final String path,
            final HttpServletRequest req,
            final HttpServletResponse res,
            final InputStream is) {
        StringBuilder msg = new StringBuilder();
        msg.append(">>> Request Started ");
        msg.append(" method:");
        msg.append(req.getMethod());
        msg.append(" method:");
        msg.append(req.getRequestURL());
        msg.append(" url:");
        log.info(msg);

        //  Debug 用 すべてのヘッダをログ出力
        Enumeration<String> multiheaders = req.getHeaderNames();
        for (String headerName : Collections.list(multiheaders)) {
            Enumeration<String> headers = req.getHeaders(headerName);
            for (String header : Collections.list(headers)) {
                log.debug("RequestHeader['" + headerName + "'] = " + header);
            }
        }
        try {
            PersoniumEngineConfig.reload();
        } catch (Exception e) {
            log.warn(" unknown Exception(" + e.getMessage() + ")");
            return errorResponse(new PersoniumEngineException("500 Internal Server Error (Unknown Error)",
                    PersoniumEngineException.STATUSCODE_SERVER_ERROR));
        }
        return Response.status(HttpStatus.SC_NO_CONTENT).build();
    }

    /**
     * Error Response creation.
     * @param e Exception Object
     * @return JAX-RS Response
     */
    final Response errorResponse(final PersoniumEngineException e) {
        return makeErrorResponse(e.getMessage(), e.getStatusCode());
    }

    /**
     * Error Response Creation.
     * @param msg Message
     * @param code Status Code
     * @return JAX-RS Response
     */
    private Response makeErrorResponse(final String msg, final int code) {
        return Response.status(code).entity(msg).build();
    }
}
