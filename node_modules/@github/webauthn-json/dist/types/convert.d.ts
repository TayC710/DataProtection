import { Schema, SchemaProperty } from "./schema-format";
export declare const copyValue = "copy";
export declare const convertValue = "convert";
export declare function convert<From, To>(conversionFn: (v: From) => To, schema: Schema, input: any): any;
export declare function derived(schema: Schema, derive: (v: any) => any): SchemaProperty;
export declare function required(schema: Schema): SchemaProperty;
export declare function optional(schema: Schema): SchemaProperty;
