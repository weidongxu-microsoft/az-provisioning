// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.provisioning;

import com.azure.resourcemanager.storage.models.Encryption;
import com.azure.resourcemanager.storage.models.EncryptionService;
import com.azure.resourcemanager.storage.models.EncryptionServices;
import com.azure.resourcemanager.storage.models.KeySource;
import com.azure.resourcemanager.storage.models.MinimumTlsVersion;
import com.azure.resourcemanager.storage.models.Sku;
import com.azure.resourcemanager.storage.models.SkuName;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class InfrastructureTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void buildStorageAccount() {
        Infrastructure infrastructure = new Infrastructure();

        StorageAccount storageAccount = infrastructure.addStorageAccount();
        // sku
        storageAccount.assignProperty(data -> data.withSku(new Sku().withName(SkuName.PREMIUM_LRS)));
        // typical security settings
        storageAccount
                .assignProperty(data -> data.withAllowBlobPublicAccess(false))
                .assignProperty(data -> data.withEnableHttpsTrafficOnly(true))
                .assignProperty(data -> data.withMinimumTlsVersion(MinimumTlsVersion.TLS1_2))
                .assignProperty(data -> {
                    data.withEncryption(new Encryption()
                            .withKeySource(KeySource.MICROSOFT_STORAGE)
                            .withServices(new EncryptionServices()
                                    .withBlob(new EncryptionService().withEnabled(true))
                                    .withFile(new EncryptionService().withEnabled(true))));
                });

        infrastructure.build();
    }
}
