import { CredentialCreationOptionsJSON, CredentialRequestOptionsJSON, PublicKeyCredentialWithAssertionJSON, PublicKeyCredentialWithAttestationJSON } from "./json";
export declare function createRequestFromJSON(requestJSON: CredentialCreationOptionsJSON): CredentialCreationOptions;
export declare function createResponseToJSON(credential: PublicKeyCredential): PublicKeyCredentialWithAttestationJSON;
export declare function create(requestJSON: CredentialCreationOptionsJSON): Promise<PublicKeyCredentialWithAttestationJSON>;
export declare function getRequestFromJSON(requestJSON: CredentialRequestOptionsJSON): CredentialRequestOptions;
export declare function getResponseToJSON(credential: PublicKeyCredential): PublicKeyCredentialWithAssertionJSON;
export declare function get(requestJSON: CredentialRequestOptionsJSON): Promise<PublicKeyCredentialWithAssertionJSON>;
declare global {
    interface Window {
        PublicKeyCredential: PublicKeyCredential | undefined;
    }
}
