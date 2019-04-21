type updatedRows = string;
type deletedRows = string;
type rowId = string;

interface tableManager {
    beginTransaction(): Promise<void | never>;
    rollback(): Promise<void | never>;
    commit(): Promise<void | never>;
    insert(tableName: string, values: object): Promise<rowId | never>;
    querySearch(query: string, queryArgs?: string[]): Promise<object | never>;
    update(tableName: string, values: object, whereQuery: string, whereArgs?: string[]): Promise<updatedRows | never>;
    remove(tableName: string, whereQuery: string, whereArgs?: string[]): Promise<deletedRows | never>;
}

interface DBClient {
    beginTransaction(): Promise<void | never>;
    execSQL(sql: string): Promise<void | never>;
    rollback(): Promise<void | never>;
    commit(): Promise<void | never>;
    close(): Promise<void | never>;
    tableManager: tableManager;
}

interface NewClientDBInput {
    dbName: string,
    dbVersion: number
}

export default class RNJackSqliteLibrary {
    static newClientDB(newClientDBInput: NewClientDBInput): Promise<DBClient | never>;
}