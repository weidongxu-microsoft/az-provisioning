// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.provisioning;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public final class Infrastructure {

    private final Map<String, StorageAccount> resourceModels = new HashMap<>();

    public Infrastructure() {

    }

    public StorageAccount addStorageAccount() {
        StorageAccount storageAccount = new StorageAccount();
        resourceModels.put("storageAccount", storageAccount);
        return storageAccount;
    }

    public void build() {
        resourceModels.forEach((name, resource) -> {
            String bicep = resource.build();
            String filename = name + ".bicep";
            try (PrintWriter out = new PrintWriter(filename)) {
                out.println(bicep);
            } catch (FileNotFoundException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
