import { NativeModules } from 'react-native';
import * as DBClient from './DBClient';

const { RNFtsSqliteLibrary } = NativeModules;

export async function newClientDB(config) {
    await RNFtsSqliteLibrary.client(config.dbName, config.dbVersion)
        .catch(err => { throw err });

    return DBClient;
}