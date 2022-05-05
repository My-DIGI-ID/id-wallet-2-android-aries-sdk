package com.digital_enabling.android_aries_sdk.utils

enum class RecordType(val typeName: String) {

    CONNECTION_RECORD("AF.ConnectionRecord"),
    PROVISIONING_RECORD("AF.ProvisioningRecord"),
    CREDENTIAL_RECORD("AF.CredentialRecord"),
    CREDENTIAL_DEFINITION_RECORD("AF.CredentialDefinition"),
    REVOCATION_REGISTRY_RECORD("AF.RevocationRegistryRecord"),
    SCHEMA_RECORD("AF.SchemaRecord"),
    PAYMENT_RECORD("AF.PaymentRecord"),
    PROOF_RECORD("AF.ProofRecord"),
    TRANSACTION_RECORD("AF.TransactionRecord"),
    BASIC_MESSAGE_RECORD("WebAgent.BasicMessage")

// todo add more record types

}