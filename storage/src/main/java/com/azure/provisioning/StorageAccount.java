// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.provisioning;

import com.azure.resourcemanager.storage.models.Kind;
import com.azure.resourcemanager.storage.models.Sku;
import com.azure.resourcemanager.storage.models.SkuName;
import com.azure.resourcemanager.storage.models.StorageAccountCreateParameters;

import java.util.function.Consumer;

public final class StorageAccount {

    private final String RESOURCE_TYPE_NAME = "Microsoft.Storage/storageAccounts";
    private final String API_VERSION = "2023-05-01";

    private final StorageAccountCreateParameters storageAccountData;

    StorageAccount() {
        storageAccountData = new StorageAccountCreateParameters()
            // default settings
            .withKind(Kind.STORAGE_V2)
            .withLocation("westus")
            .withSku(new Sku().withName(SkuName.STANDARD_LRS));
    }

    String build() {
        return BicepSerializationUtils.serializeToBicep(RESOURCE_TYPE_NAME, API_VERSION, "storageAccount", "sa", storageAccountData);
    }

    public StorageAccount assignProperty(Consumer<StorageAccountCreateParameters> assignFunc) {
        assignFunc.accept(storageAccountData);
        return this;
    }
}
