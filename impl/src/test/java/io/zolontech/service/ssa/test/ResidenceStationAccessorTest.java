package io.zolontech.service.ssa.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.zolontech.service.ssa.impl.ResidenceStationAccessor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

/**
 *
 */
public class ResidenceStationAccessorTest {

    @Test
    public void testFindByZipCode() throws Exception {
        final ResidenceStationAccessor residenceStation = new ResidenceStationAccessor();
        final Set<String> zipCodes = Collections.singleton("92225");
        final JsonObject json = residenceStation.findForZipCodes(zipCodes);
        Assert.assertNotNull("Response is null", json);
        final JsonArray features = json.getAsJsonArray("features");
        Assert.assertNotNull("Data wasn't available for zip codes", zipCodes);
        System.out.println("Got " + json);
    }
}
