// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.provisioning;

import com.azure.core.util.BinaryData;
import com.azure.json.JsonOptions;
import com.azure.json.JsonReader;
import com.azure.json.JsonToken;
import com.azure.json.implementation.DefaultJsonReader;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Map;

final class BicepSerializationUtils {

    public static String serializeToBicep(
            String resourceType, String apiVersion,
            String symbolicName,
            String name,
            Object resource) {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("resource ").append(symbolicName).append(" '").append(resourceType).append("@").append(apiVersion).append("' = ");

        try (JsonReader jsonReader = DefaultJsonReader.fromBytes(BinaryData.fromObject(resource).toBytes(), new JsonOptions())) {
            writeObject(stringBuilder, 0, jsonReader,
                    Map.of("name", "toLower(take('" + name + "${uniqueString('" + name + "')}', 24))"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return stringBuilder.toString();
    }

    private static void writeObject(StringBuilder stringBuilder, int indent, JsonReader jsonReader, Map<String, String> overrides) throws IOException {
        stringBuilder.append("{\n");

        int objectIndent = indent + 2;
        overrides.forEach((fieldName, expression) -> {
            writeSpaces(stringBuilder, objectIndent);
            stringBuilder.append(fieldName).append(": ").append(expression).append("\n");
        });

        jsonReader.readObject(reader -> {
            while (reader.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = reader.getFieldName();
                writeSpaces(stringBuilder, objectIndent);
                stringBuilder.append(fieldName).append(": ");

                JsonToken token = reader.nextToken();
                if (token == JsonToken.START_OBJECT) {
                    writeObject(stringBuilder, objectIndent, jsonReader, Map.of());
                } else if (token == JsonToken.START_ARRAY) {
                    // TODO
                } else {
                    Object value = reader.readUntyped();
                    writeValue(stringBuilder, value);
                    stringBuilder.append("\n");
                }
            }
            return null;
        });

        writeSpaces(stringBuilder, indent);
        stringBuilder.append("}\n");
    }

    private static void writeSpaces(StringBuilder stringBuilder, int indent) {
        stringBuilder.append(String.join("", Collections.nCopies(indent, " ")));
    }

    private static void writeValue(StringBuilder stringBuilder, Object value) {
        if (value instanceof String) {
            stringBuilder.append("'").append(value).append("'");
        } else {
            stringBuilder.append(value);
        }
    }
}
