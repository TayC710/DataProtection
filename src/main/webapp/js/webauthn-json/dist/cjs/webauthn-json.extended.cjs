"use strict";
var __defProp = Object.defineProperty;
var __getOwnPropDesc = Object.getOwnPropertyDescriptor;
var __getOwnPropNames = Object.getOwnPropertyNames;
var __hasOwnProp = Object.prototype.hasOwnProperty;
var __export = (target, all) => {
  for (var name in all)
    __defProp(target, name, { get: all[name], enumerable: true });
};
var __copyProps = (to, from, except, desc) => {
  if (from && typeof from === "object" || typeof from === "function") {
    for (let key of __getOwnPropNames(from))
      if (!__hasOwnProp.call(to, key) && key !== except)
        __defProp(to, key, { get: () => from[key], enumerable: !(desc = __getOwnPropDesc(from, key)) || desc.enumerable });
  }
  return to;
};
var __toCommonJS = (mod) => __copyProps(__defProp({}, "__esModule", { value: true }), mod);
var __async = (__this, __arguments, generator) => {
  return new Promise((resolve, reject) => {
    var fulfilled = (value) => {
      try {
        step(generator.next(value));
      } catch (e) {
        reject(e);
      }
    };
    var rejected = (value) => {
      try {
        step(generator.throw(value));
      } catch (e) {
        reject(e);
      }
    };
    var step = (x) => x.done ? resolve(x.value) : Promise.resolve(x.value).then(fulfilled, rejected);
    step((generator = generator.apply(__this, __arguments)).next());
  });
};

// src/webauthn-json/extended.ts
var extended_exports = {};
__export(extended_exports, {
  base64urlToBuffer: () => base64urlToBuffer,
  bufferToBase64url: () => bufferToBase64url,
  convert: () => convert,
  create: () => create,
  createExtended: () => createExtended,
  createExtendedRequestFromJSON: () => createExtendedRequestFromJSON,
  createExtendedResponseToJSON: () => createExtendedResponseToJSON,
  createRequestFromJSON: () => createRequestFromJSON,
  createResponseToJSON: () => createResponseToJSON,
  get: () => get,
  getExtended: () => getExtended,
  getExtendedRequestFromJSON: () => getExtendedRequestFromJSON,
  getExtendedResponseToJSON: () => getExtendedResponseToJSON,
  getRequestFromJSON: () => getRequestFromJSON,
  getResponseToJSON: () => getResponseToJSON,
  schema: () => schema,
  supported: () => supported
});
module.exports = __toCommonJS(extended_exports);

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
var schema = {
  credentialCreationOptions,
  publicKeyCredentialWithAttestation,
  credentialRequestOptions,
  publicKeyCredentialWithAssertion
};

// src/webauthn-json/basic/api.ts
function createRequestFromJSON(requestJSON) {
  return convert(base64urlToBuffer, credentialCreationOptions, requestJSON);
}
function createResponseToJSON(credential) {
  return convert(
    bufferToBase64url,
    publicKeyCredentialWithAttestation,
    credential
  );
}
function create(requestJSON) {
  return __async(this, null, function* () {
    const credential = yield navigator.credentials.create(
      createRequestFromJSON(requestJSON)
    );
    return createResponseToJSON(credential);
  });
}
function getRequestFromJSON(requestJSON) {
  return convert(base64urlToBuffer, credentialRequestOptions, requestJSON);
}
function getResponseToJSON(credential) {
  return convert(
    bufferToBase64url,
    publicKeyCredentialWithAssertion,
    credential
  );
}
function get(requestJSON) {
  return __async(this, null, function* () {
    const credential = yield navigator.credentials.get(
      getRequestFromJSON(requestJSON)
    );
    return getResponseToJSON(credential);
  });
}

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
function makeCallable(jsonResponse, credentialResponse) {
  const callable = {};
  if (credentialResponse.getTransports) {
    callable.getTransports = () => {
      return credentialResponse.getTransports();
    };
  }
  if (credentialResponse.getAuthenticatorData) {
    callable.getAuthenticatorData = () => {
      return bufferToBase64url(credentialResponse.getAuthenticatorData());
    };
  }
  if (credentialResponse.getPublicKey) {
    callable.getPublicKey = () => {
      const publicKey = credentialResponse.getPublicKey();
      return publicKey && bufferToBase64url(publicKey);
    };
  }
  if (credentialResponse.getPublicKeyAlgorithm) {
    callable.getPublicKeyAlgorithm = () => {
      return credentialResponse.getPublicKeyAlgorithm();
    };
  }
  const newJSON = Object.create(callable);
  Object.assign(newJSON, jsonResponse);
  return newJSON;
}
function createExtended(requestJSON) {
  return __async(this, null, function* () {
    const credential = yield navigator.credentials.create(
      createExtendedRequestFromJSON(requestJSON)
    );
    const json = createExtendedResponseToJSON(credential);
    json.response = makeCallable(
      json.response,
      credential.response
    );
    return json;
  });
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
function getExtended(requestJSON) {
  return __async(this, null, function* () {
    const credential = yield navigator.credentials.get(
      getExtendedRequestFromJSON(requestJSON)
    );
    return getExtendedResponseToJSON(credential);
  });
}
//# sourceMappingURL=webauthn-json.extended.cjs.map
