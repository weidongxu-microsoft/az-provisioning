resource storageAccount 'Microsoft.Storage/storageAccounts@2023-05-01' = {
  name: toLower(take('sa${uniqueString('sa')}', 24))
  sku: {
    name: 'Premium_LRS'
  }
  location: 'westus'
  properties: {
    encryption: {
      services: {
        blob: {
          enabled: true
        }
        file: {
          enabled: true
        }
      }
      keySource: 'Microsoft.Storage'
    }
    supportsHttpsTrafficOnly: true
    allowBlobPublicAccess: false
    minimumTlsVersion: 'TLS1_2'
  }
}

