#!/usr/bin/env node

// src/webauthn-json/convert.ts
var copyValue = "copy";
var convertValue = "convert";
function derived(schema2, derive) {
  return {
    required: true,
    schema: schema2,
    derive
  };
}
function required(schema2) {
  return {
    required: true,
    schema: schema2
  };
}
function optional(schema2) {
  return {
    required: false,
    schema: schema2
  };
}

// src/webauthn-json/basic/schema.ts
var publicKeyCredentialDescriptorSchema = {
  type: required(copyValue),
  id: required(convertValue),
  transports: optional(copyValue)
};
var simplifiedExtensionsSchema = {
  appid: optional(copyValue),
  appidExclude: optional(copyValue),
  credProps: optional(copyValue)
};
var simplifiedClientExtensionResultsSchema = {
  appid: optional(copyValue),
  appidExclude: optional(copyValue),
  credProps: optional(copyValue)
};
var credentialCreationOptions = {
  publicKey: required({
    rp: required(copyValue),
    user: required({
      id: required(convertValue),
      name: required(copyValue),
      displayName: required(copyValue)
    }),
    challenge: required(convertValue),
    pubKeyCredParams: required(copyValue),
    timeout: optional(copyValue),
    excludeCredentials: optional([publicKeyCredentialDescriptorSchema]),
    authenticatorSelection: optional(copyValue),
    attestation: optional(copyValue),
    extensions: optional(simplifiedExtensionsSchema)
  }),
  signal: optional(copyValue)
};
var publicKeyCredentialWithAttestation = {
  type: required(copyValue),
  id: required(copyValue),
  rawId: required(convertValue),
  authenticatorAttachment: optional(copyValue),
  response: required({
    clientDataJSON: required(convertValue),
    attestationObject: required(convertValue),
    transports: derived(
      copyValue,
      (response) => {
        var _a;
        return ((_a = response.getTransports) == null ? void 0 : _a.call(response)) || [];
      }
    )
  }),
  clientExtensionResults: derived(
    simplifiedClientExtensionResultsSchema,
    (pkc) => pkc.getClientExtensionResults()
  )
};
var credentialRequestOptions = {
  mediation: optional(copyValue),
  publicKey: required({
    challenge: required(convertValue),
    timeout: optional(copyValue),
    rpId: optional(copyValue),
    allowCredentials: optional([publicKeyCredentialDescriptorSchema]),
    userVerification: optional(copyValue),
    extensions: optional(simplifiedExtensionsSchema)
  }),
  signal: optional(copyValue)
};
var publicKeyCredentialWithAssertion = {
  type: required(copyValue),
  id: required(copyValue),
  rawId: required(convertValue),
  authenticatorAttachment: optional(copyValue),
  response: required({
    clientDataJSON: required(convertValue),
    authenticatorData: required(convertValue),
    signature: required(convertValue),
    userHandle: required(convertValue)
  }),
  clientExtensionResults: derived(
    simplifiedClientExtensionResultsSchema,
    (pkc) => pkc.getClientExtensionResults()
  )
};
var schema = {
  credentialCreationOptions,
  publicKeyCredentialWithAttestation,
  credentialRequestOptions,
  publicKeyCredentialWithAssertion
};

// src/bin/main.ts
var help = `Usage: ${process.argv[1]} schema`;
var command = process.argv[2];
if (command === "schema") {
  const schemaWithVersion = { ...schema, version: "2.1.1" };
  console.log(JSON.stringify(schemaWithVersion, null, "  "));
} else {
  console.log(help);
}
//# sourceMappingURL=main.js.map
