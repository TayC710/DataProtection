import { createExtendedRequestFromJSON as parseExtendedCreationOptionsFromJSON, CredentialCreationOptionsExtendedJSON, CredentialRequestOptionsExtendedJSON, getExtendedRequestFromJSON as parseExtendedRequestOptionsFromJSON, PublicKeyCredentialWithAssertionExtendedResultsJSON as AuthenticationResponseExtendedJSON, PublicKeyCredentialWithAttestationExtendedResultsJSON as RegistrationResponseExtendedJSON, supported } from "./extended";
export { parseExtendedCreationOptionsFromJSON, parseExtendedRequestOptionsFromJSON, supported, };
export type { CredentialCreationOptionsExtendedJSON, CredentialRequestOptionsExtendedJSON, AuthenticationResponseExtendedJSON, RegistrationResponseExtendedJSON, };
export interface RegistrationPublicKeyCredential extends PublicKeyCredential {
    toJSON(): RegistrationResponseExtendedJSON;
}
export declare function createExtended(options: CredentialCreationOptions): Promise<RegistrationPublicKeyCredential>;
export interface AuthenticationPublicKeyCredential extends PublicKeyCredential {
    toJSON(): AuthenticationResponseExtendedJSON;
}
export declare function getExtended(options: CredentialRequestOptions): Promise<AuthenticationPublicKeyCredential>;
