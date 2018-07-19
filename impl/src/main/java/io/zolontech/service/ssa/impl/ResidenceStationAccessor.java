package io.zolontech.service.ssa.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.fluent.Request;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static io.zolontech.service.ssa.impl.StringUtil.commaSeparated;

/**
 *
 */
public class ResidenceStationAccessor {

    private static final String residenceStationURL = "";

    private static final String commonQueryParams = "";

    /**
     * Returns the residence stations for the passed zip codes
     *
     * @param zipCodes
     * @return
     */
    public JsonObject findForZipCodes(final Set<String> zipCodes) {
        final String whereClause = "zip5_1 in (" + commaSeparated(zipCodes) + ")";
        final String whereClauseEncoded;
        try {
            whereClauseEncoded = URLEncoder.encode(whereClause, StandardCharsets.US_ASCII.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to create a URL to fetch the field offices for zip codes", e);
        }
        final String uri = residenceStationURL + "?where=" + whereClauseEncoded + "&" + commonQueryParams;
        System.out.println("Invoking on " + uri);
        final String jsonContent;
        try {
            jsonContent = Request.Get(uri).execute().handleResponse(new BasicResponseHandler());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new JsonParser().parse(jsonContent).getAsJsonObject();
    }
}
