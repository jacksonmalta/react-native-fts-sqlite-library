import * as TableManager from "./TableManager";
import { NativeModules } from 'react-native';

const { RNFtsSqliteLibrary } = NativeModules;

export async function execSQL(sql) {
    await RNFtsSqliteLibrary.execSQL(sql);
}

export async function beginTransaction() {
    await RNFtsSqliteLibrary.beginTransactionDB();
}

export async function commit() {
    await RNFtsSqliteLibrary.commitDB();
}

export async function rollback() {
    await RNFtsSqliteLibrary.rollbackDB();
}

export async function close() {
    await RNFtsSqliteLibrary.closeClient();
}

export const tableManager = TableManager;