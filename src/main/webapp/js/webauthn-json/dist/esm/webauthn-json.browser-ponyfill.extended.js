// src/webauthn-json/base64url.ts
function base64urlToBuffer(baseurl64String) {
  const padding = "==".slice(0, (4 - baseurl64String.length % 4) % 4);
  const base64String = baseurl64String.replace(/-/g, "+").replace(/_/g, "/") + padding;
  const str = atob(base64String);
  const buffer = new ArrayBuffer(str.length);
  const byteView = new Uint8Array(buffer);
  for (let i = 0; i < str.length; i++) {
    byteView[i] = str.charCodeAt(i);
  }
  return buffer;
}
function bufferToBase64url(buffer) {
  const byteView = new Uint8Array(buffer);
  let str = "";
  for (const charCode of byteView) {
    str += String.fromCharCode(charCode);
  }
  const base64String = btoa(str);
  const base64urlString = base64String.replace(/\+/g, "-").replace(
    /\//g,
    "_"
  ).replace(/=/g, "");
  return base64urlString;
}

// src/webauthn-json/convert.ts
var copyValue = "copy";
var convertValue = "convert";
function convert(conversionFn, schema2, input) {
  if (schema2 === copyValue) {
    return input;
  }
  if (schema2 === convertValue) {
    return conversionFn(input);
  }
  if (schema2 instanceof Array) {
    return input.map((v) => convert(conversionFn, schema2[0], v));
  }
  if (schema2 instanceof Object) {
    const output = {};
    for (const [key, schemaField] of Object.entries(schema2)) {
      if (schemaField.derive) {
        const v = schemaField.derive(input);
        if (v !== void 0) {
          input[key] = v;
        }
      }
      if (!(key in input)) {
        if (schemaField.required) {
          throw new Error(`Missing key: ${key}`);
        }
        continue;
      }
      if (input[key] == null) {
        output[key] = null;
        continue;
      }
      output[key] = convert(
        conversionFn,
        schemaField.schema,
        input[key]
      );
    }
    return output;
  }
}
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

// src/webauthn-json/basic/supported.ts
function supported() {
  return !!(navigator.credentials && navigator.credentials.create && navigator.credentials.get && window.PublicKeyCredential);
}

// src/webauthn-json/extended/schema.ts
var authenticationExtensionsClientInputsSchema = {
  appid: optional(copyValue),
  appidExclude: optional(copyValue),
  uvm: optional(copyValue),
  credProps: optional(copyValue),
  largeBlob: optional({
    support: optional(copyValue),
    read: optional(copyValue),
    write: optional(convertValue)
  })
};
var authenticationExtensionsClientOutputsSchema = {
  appid: optional(copyValue),
  appidExclude: optional(copyValue),
  uvm: optional(copyValue),
  credProps: optional(copyValue),
  largeBlob: optional({
    supported: optional(copyValue),
    blob: optional(convertValue),
    written: optional(copyValue)
  })
};
var credentialCreationOptionsExtended = JSON.parse(
  JSON.stringify(credentialCreationOptions)
);
credentialCreationOptionsExtended.publicKey.schema.extensions = optional(authenticationExtensionsClientInputsSchema);
var publicKeyCredentialWithAttestationExtended = JSON.parse(
  JSON.stringify(publicKeyCredentialWithAttestation)
);
publicKeyCredentialWithAttestationExtended.clientExtensionResults = derived(
  authenticationExtensionsClientOutputsSchema,
  publicKeyCredentialWithAttestation.clientExtensionResults.derive
);
publicKeyCredentialWithAttestationExtended.response.schema.transports = publicKeyCredentialWithAttestation.response.schema.transports;
var credentialRequestOptionsExtended = JSON.parse(
  JSON.stringify(credentialRequestOptions)
);
credentialRequestOptionsExtended.publicKey.schema.extensions = optional(authenticationExtensionsClientInputsSchema);
var publicKeyCredentialWithAssertionExtended = JSON.parse(
  JSON.stringify(publicKeyCredentialWithAssertion)
);
publicKeyCredentialWithAssertionExtended.clientExtensionResults = derived(
  authenticationExtensionsClientOutputsSchema,
  publicKeyCredentialWithAssertion.clientExtensionResults.derive
);

// src/webauthn-json/extended/api.ts
function createExtendedRequestFromJSON(requestJSON) {
  return convert(
    base64urlToBuffer,
    credentialCreationOptionsExtended,
    requestJSON
  );
}
function createExtendedResponseToJSON(credential) {
  return convert(
    bufferToBase64url,
    publicKeyCredentialWithAttestationExtended,
    credential
  );
}
function getExtendedRequestFromJSON(requestJSON) {
  return convert(
    base64urlToBuffer,
    credentialRequestOptionsExtended,
    requestJSON
  );
}
function getExtendedResponseToJSON(credential) {
  return convert(
    bufferToBase64url,
    publicKeyCredentialWithAssertionExtended,
    credential
  );
}

// src/webauthn-json/browser-ponyfill.extended.ts
async function createExtended2(options) {
  const response = await navigator.credentials.create(
    options
  );
  response.toJSON = () => createExtendedResponseToJSON(response);
  return response;
}
async function getExtended2(options) {
  const response = await navigator.credentials.get(
    options
  );
  response.toJSON = () => getExtendedResponseToJSON(response);
  return response;
}
export {
  createExtended2 as createExtended,
  getExtended2 as getExtended,
  createExtendedRequestFromJSON as parseExtendedCreationOptionsFromJSON,
  getExtendedRequestFromJSON as parseExtendedRequestOptionsFromJSON,
  supported
};
//# sourceMappingURL=webauthn-json.browser-ponyfill.extended.js.map
