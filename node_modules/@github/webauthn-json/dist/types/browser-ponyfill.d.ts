import { createRequestFromJSON as parseCreationOptionsFromJSON, getRequestFromJSON as parseRequestOptionsFromJSON } from "./basic/api";
import { supported } from "./basic/supported";
import { CredentialCreationOptionsJSON, CredentialRequestOptionsJSON, PublicKeyCredentialWithAssertionJSON as AuthenticationResponseJSON, PublicKeyCredentialWithAttestationJSON as RegistrationResponseJSON } from "./basic/json";
export { parseCreationOptionsFromJSON, parseRequestOptionsFromJSON, supported };
export type { CredentialCreationOptionsJSON, CredentialRequestOptionsJSON, AuthenticationResponseJSON, RegistrationResponseJSON, };
export interface RegistrationPublicKeyCredential extends PublicKeyCredential {
    toJSON(): RegistrationResponseJSON;
}
export declare function create(options: CredentialCreationOptions): Promise<RegistrationPublicKeyCredential>;
export interface AuthenticationPublicKeyCredential extends PublicKeyCredential {
    toJSON(): AuthenticationResponseJSON;
}
export declare function get(options: CredentialRequestOptions): Promise<AuthenticationPublicKeyCredential>;
