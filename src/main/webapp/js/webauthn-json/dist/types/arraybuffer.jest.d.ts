/// <reference types="jest" />
declare global {
    namespace jest {
        interface Matchers<R, T> {
            toEqualBuffer(observed: ArrayBuffer): CustomMatcherResult;
        }
    }
}
export {};
