package io.zolontech.service.ssa.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.zolontech.service.ssa.impl.FieldOfficeAccessor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

/**
 *
 */
public class FieldOfficeAccesorTest {

    @Test
    public void testFindByZipCode() throws Exception {
        final FieldOfficeAccessor fieldOffice = new FieldOfficeAccessor();
        final Set<String> zipCodes = Collections.singleton("95113");
        final JsonObject json = fieldOffice.findForZipCodes(zipCodes);
        Assert.assertNotNull("Response is null", json);
        final JsonArray features = json.getAsJsonArray("features");
        Assert.assertNotNull("Data wasn't available for zip codes", zipCodes);
        System.out.println("Got " + json);
    }
}
