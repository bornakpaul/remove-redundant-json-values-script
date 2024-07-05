import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        File folder = new File("/Users/bornakpaul/NitaraProjects/com.nitara.farmer.flutter/assets/translations");
        File[] jsonFiles = folder.listFiles();

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

        assert jsonFiles != null;
        for (File jsonFile : jsonFiles) {
            try {
                JsonNode jsonNode = mapper.readTree(jsonFile);
                ObjectNode objectNode = (ObjectNode) jsonNode;
                objectNode.fields().forEachRemaining(entry -> {
                    String fieldValue = entry.getValue().asText();
                    // check if the string includes \n or \r and remove it
                    if(fieldValue.matches(".*((\\r\\n)+).*")){
                        String newFieldValue = fieldValue.replace("(\\r\\n)+", "").trim();
                        //((ObjectNode) entry.getValue()).put(entry.getKey(), newFieldValue);
                        if (entry.getValue() instanceof ObjectNode) {
                            ((ObjectNode) entry.getValue()).put(entry.getKey(), newFieldValue);
                        } else {
                            objectNode.put(entry.getKey(), newFieldValue);
                        }
                        System.out.println(newFieldValue);
                    }
                });
                writer.writeValue(jsonFile, objectNode);
                System.out.println("Completed for file" + jsonFile.getName());
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }
}