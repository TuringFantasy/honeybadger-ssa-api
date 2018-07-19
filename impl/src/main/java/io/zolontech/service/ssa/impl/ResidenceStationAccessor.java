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

    private static final String fieldOfficeURL = "http://services6.arcgis.com/zFiipv75rloRP5N4/ArcGIS/rest/services/Office_Points/FeatureServer/0/query";

    private static final String commonQueryParams = "&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&resultType=none&distance=0.0&units=esriSRUnit_Meter&returnGeodetic=false&returnGeometry=true&multipatchOption=xyFootprint&maxAllowableOffset=&geometryPrecision=&outSR=&datumTransformation=&applyVCSProjection=false&returnIdsOnly=false&returnUniqueIdsOnly=false&returnCountOnly=false&returnExtentOnly=false&returnDistinctValues=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&having=&resultOffset=&resultRecordCount=&returnZ=false&returnM=false&returnExceededLimitFeatures=true&quantizationParameters=&sqlFormat=none&f=pjson&token=";


    /**
     * Returns the resident stations for the passed zip codes
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
        final String outFieldsEncoded;
        try {
            final String outFields = "OfficeCode,OfficeName,OfficeType," +
                    "AddressLine1,AddressLine2,AddressLine3,City,State,Zip5_1" +
                    ",MON_OPEN_TM,MON_CLOS_TM" +
                    ",TUE_OPEN_TM,TUE_CLOS_TM" +
                    ",WED_OPEN_TM,WED_CLOS_TM" +
                    ",THU_OPEN_TM,THU_CLOS_TM" +
                    ",FRI_OPEN_TM,FRI_CLOS_TM";
            outFieldsEncoded = URLEncoder.encode(outFields, StandardCharsets.US_ASCII.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to create a URL to fetch the field offices for zip codes", e);
        }
        final String uri = fieldOfficeURL + "?where=" + whereClauseEncoded + "&outFields=" + outFieldsEncoded + "&" + commonQueryParams;
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
