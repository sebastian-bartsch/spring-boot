package com.tld.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import com.tld.dto.SensorDataDTO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SensorDataDeserializer extends JsonDeserializer<SensorDataDTO> {

    @Override
    public SensorDataDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        
        Long datetime = node.get("datetime").asLong();
        Map<String, Object> sensorDataMap = new HashMap<>();
        
        // Iterar sobre los campos dinámicos del JSON (excluyendo datetime)
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String key = entry.getKey();

            // Excluir el campo "datetime" para no agregarlo al mapa
            if (!"datetime".equals(key)) {
                sensorDataMap.put(key, entry.getValue().asDouble());
            }
        }
        
        return new SensorDataDTO(sensorDataMap, datetime);
    }
}
