package com.bbdsoftware.service.response;

import com.fasterxml.jackson.databind.*;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class ResultMessageTest {

    @Test
    public void testSerialisation_withI18n() throws Exception {

        ResultMessage message = new ResultMessage()
                .withI18nDetails("code", Map.of(
                        "param1", "param1",
                        "param2", 100L
                ));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(mapper.writeValueAsString(message));

        assertEquals("code", node.at("/i18n/code").textValue());
        assertEquals("param1", node.at("/i18n/params/param1").textValue());
        assertEquals(100L, node.at("/i18n/params/param2").longValue());
    }

    @Test
    public void testSerialisation_withI18nNullParams() throws Exception {

        ResultMessage message = new ResultMessage()
                .withI18nDetails("ignored", null);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(mapper.writeValueAsString(message));

        assertTrue(node.at("/i18n/params").isMissingNode());
    }

    @Test
    public void testSerialisation_withI18nEmptyParams() throws Exception {

        ResultMessage message = new ResultMessage()
                .withI18nDetails("ignored", Collections.emptyMap());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(mapper.writeValueAsString(message));

        assertTrue(node.at("/i18n/params").isMissingNode());
    }

    @Test
    public void testSerialisation_withoutI18n() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(mapper.writeValueAsString(new ResultMessage()));

        assertTrue(node.at("/i18n").isMissingNode());
    }
}