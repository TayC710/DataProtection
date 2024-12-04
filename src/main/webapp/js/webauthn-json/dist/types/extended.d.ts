export * from "./index";
export { createRequestFromJSON, createResponseToJSON, getRequestFromJSON, getResponseToJSON, } from "./basic/api";
export { createExtended, createExtendedRequestFromJSON, createExtendedResponseToJSON, getExtended, getExtendedRequestFromJSON, getExtendedResponseToJSON, } from "./extended/api";
export type { CredentialCreationOptionsExtendedJSON, CredentialRequestOptionsExtendedJSON, PublicKeyCredentialWithAssertionExtendedResultsJSON, PublicKeyCredentialWithAttestationExtendedResultsJSON, } from "./extended/json";
export { base64urlToBuffer, bufferToBase64url } from "./base64url";
export { convert } from "./convert";
export { schema } from "./basic/schema";
