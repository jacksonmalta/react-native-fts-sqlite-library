import { NativeModules } from 'react-native';
import moment from 'moment';

const { RNFtsSqliteLibrary } = NativeModules;

export async function insert(tableName, obj) {
    Object.keys(obj)
        .map(k => {
            if (obj[k].type === 'date') {
                obj[k] = moment(obj[k].value, obj[k].format).format("YYYY-MM-DD HH:mm:ss.SSS");
            }
        });
    return await RNFtsSqliteLibrary.insert(tableName, JSON.stringify(obj));
}

export async function beginTransaction() {
    await RNFtsSqliteLibrary.beginTransaction();
}

export async function commit() {
    await RNFtsSqliteLibrary.commit();
}

export async function rollback() {
    await RNFtsSqliteLibrary.rollback();
}

export async function update(tableName, obj, whereQuery, args) {
    Object.keys(obj)
        .map(k => {
            if (obj[k].type === 'date') {
                obj[k] = moment(obj[k].value, obj[k].format).format("YYYY-MM-DD HH:mm:ss.SSS");
            }
        });

    let argsJoin = "";

    if (args && args.length >= 1) {
        argsJoin = args.join(",");
    }

    return await RNFtsSqliteLibrary.update(tableName, JSON.stringify(obj), whereQuery, argsJoin);
}

export async function remove(tableName, query, args) {
    let argsJoin = "";

    if (args && args.length >= 1) {
        argsJoin = args.join(",");
    }

    return await RNFtsSqliteLibrary.delete(tableName, query, argsJoin);
}

export async function querySearch(query, args) {
    let argsJoin = "";

    if (args && args.length >= 1) {
        argsJoin = args.join(",");
    }

    let result = await RNFtsSqliteLibrary.query(query, argsJoin);
    return JSON.parse(result);
}