"use strict";
(() => {
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
  function convert(conversionFn, schema, input) {
    if (schema === copyValue) {
      return input;
    }
    if (schema === convertValue) {
      return conversionFn(input);
    }
    if (schema instanceof Array) {
      return input.map((v) => convert(conversionFn, schema[0], v));
    }
    if (schema instanceof Object) {
      const output = {};
      for (const [key, schemaField] of Object.entries(schema)) {
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
  function derived(schema, derive) {
    return {
      required: true,
      schema,
      derive
    };
  }
  function required(schema) {
    return {
      required: true,
      schema
    };
  }
  function optional(schema) {
    return {
      required: false,
      schema
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
          var _a3;
          return ((_a3 = response.getTransports) == null ? void 0 : _a3.call(response)) || [];
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

  // src/dev/inspector/inspector.css.ts
  var inspectorCSS = `:host {
  position: absolute;
  top: 2em;
  left: 2em;
  right: 2em;
  bottom: 2em;
  box-sizing: border-box;
  font-family: sans-serif;
  box-shadow: 0 0 1em 2em rgba(0, 0, 0, 0.2);
  overflow: hidden;
  resize: both;

  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(0.5em);
  -webkit-backdrop-filter: blur(4px);
  z-index: 1000000;
}

.wrapper {
  width: 100%;
  height: 100%;
  padding: 1em;
  box-sizing: border-box;
  display: grid;
  grid-template-rows: auto auto 1fr;
}

.header {
  text-align: center;
}

textarea {
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.8);
}

.success textarea {
  background: rgba(0, 255, 0, 0.5);
}

.failure textarea {
  background: rgba(255, 0, 0, 0.25);
}

.controls {
  text-align: center;
  align-items: center;
  margin: 0.5em;
  gap: 0.5em;
}

button {
  margin-right: 0.5em;
}
`;

  // src/dev/inspector/inspector.ts
  var _a;
  var originalCreate = (_a = navigator.credentials) == null ? void 0 : _a.create.bind(
    navigator.credentials
  );
  var _a2;
  var originalGet = (_a2 = navigator.credentials) == null ? void 0 : _a2.get.bind(navigator.credentials);
  if (!navigator.credentials) {
    navigator.credentials = {};
  }
  var WebAuthnInspector = class extends HTMLElement {
    constructor() {
      super();
      this.shadow = this.attachShadow({ mode: "closed" });
      const cssElem = document.createElement("style");
      cssElem.textContent = inspectorCSS;
      this.shadow.appendChild(cssElem);
      this.contentWrapper = document.createElement("div");
      this.contentWrapper.classList.add("wrapper");
      this.shadow.appendChild(this.contentWrapper);
      this.header = document.createElement("div");
      this.header.textContent = "WebAuthn Request";
      this.header.classList.add("header");
      this.contentWrapper.appendChild(this.header);
      this.controls = document.createElement("div");
      this.controls.classList.add("controls");
      this.contentWrapper.appendChild(this.controls);
      this.textareaWrapper = document.createElement("div");
      this.textareaWrapper.classList.add("textarea-wrapper");
      this.contentWrapper.appendChild(this.textareaWrapper);
      this.textarea = document.createElement("textarea");
      this.textareaWrapper.appendChild(this.textarea);
      this.closeButton = document.createElement("button");
      this.closeButton.textContent = "Close";
      this.closeButton.addEventListener("click", () => {
        document.body.removeChild(this);
        if (this.close) {
          this.close();
        }
      });
      this.controls.appendChild(this.closeButton);
      const copyButton = document.createElement("button");
      copyButton.textContent = "Copy JSON";
      copyButton.addEventListener("click", () => {
        navigator.clipboard.writeText(this.textarea.value);
        copyButton.textContent = "Copied!";
        setTimeout(() => {
          copyButton.textContent = "Copy JSON";
        }, 1e3);
      });
      this.controls.appendChild(copyButton);
      document.body.appendChild(this);
    }
    create(options) {
      return __async(this, null, function* () {
        return new Promise((resolve, reject) => {
          this.close = () => {
            reject("WebAuthn inspector closed");
          };
          const json = convert(
            bufferToBase64url,
            credentialCreationOptionsExtended,
            options
          );
          this.header.textContent = "WebAuthn Create Request";
          this.textarea.value = JSON.stringify(json, null, "  ");
          this.opButton = document.createElement("button");
          this.opButton.textContent = "Create";
          this.opButton.addEventListener("click", () => __async(this, null, function* () {
            try {
              const requestJSON = JSON.parse(this.textarea.value);
              const request = createRequestFromJSON(requestJSON);
              const response = yield originalCreate(request);
              this.success("Create", createResponseToJSON(response));
              this.closeButton.textContent = "Respond";
              this.close = () => {
                resolve(response);
              };
            } catch (e) {
              this.failure("Create", e);
              this.close = () => {
                reject(e);
              };
            }
          }));
          this.controls.appendChild(this.opButton);
        });
      });
    }
    get(options) {
      return __async(this, null, function* () {
        return new Promise((resolve, reject) => {
          this.close = () => {
            reject("WebAuthn inspector closed");
          };
          const json = convert(
            bufferToBase64url,
            credentialRequestOptionsExtended,
            options
          );
          this.header.textContent = "WebAuthn Get Request";
          this.textarea.value = JSON.stringify(json, null, "  ");
          this.opButton = document.createElement("button");
          this.opButton.textContent = "Get";
          this.opButton.addEventListener("click", () => __async(this, null, function* () {
            try {
              const requestJSON = JSON.parse(this.textarea.value);
              const request = getRequestFromJSON(requestJSON);
              console.log(request);
              const response = yield originalGet(request);
              this.success("Get", getResponseToJSON(response));
              this.closeButton.textContent = "Respond";
              this.close = () => {
                resolve(response);
              };
            } catch (e) {
              this.failure("Get", e);
              this.close = () => {
                reject(e);
              };
            }
          }));
          this.controls.appendChild(this.opButton);
        });
      });
    }
    success(op, responseJSON) {
      this.controls.removeChild(this.opButton);
      this.header.textContent = `\u2705 WebAuthn ${op} Response`;
      this.contentWrapper.classList.add("success");
      this.textarea.value = JSON.stringify(responseJSON, null, "  ");
    }
    failure(op, e) {
      this.controls.removeChild(this.opButton);
      this.header.textContent = `\u274C WebAuthn ${op} Response`;
      this.contentWrapper.classList.add("failure");
      this.textarea.value = e.toString();
    }
  };
  customElements.define("webauthn-inspector", WebAuthnInspector);
  navigator.credentials.create = function(options) {
    return __async(this, null, function* () {
      console.log(options);
      const interceptor = new WebAuthnInspector();
      return yield interceptor.create(options);
    });
  };
  navigator.credentials.get = function(options) {
    return __async(this, null, function* () {
      const interceptor = new WebAuthnInspector();
      return yield interceptor.get(options);
    });
  };
  console.log("WebAuthn Inspector is active!");
})();
//# sourceMappingURL=inspector.js.map
