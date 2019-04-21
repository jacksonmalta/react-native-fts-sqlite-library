
# react-native-fts-sqlite-library

## Typescript Support

## Getting started

`$ npm install react-native-fts-sqlite-library --save`

### Mostly automatic installation

`$ react-native link react-native-fts-sqlite-library`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.fts.RNFtsSqliteLibraryPackage;` to the imports at the top of the file
  - Add `new RNFtsSqliteLibraryPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-fts-sqlite-library'
  	project(':react-native-fts-sqlite-library').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-fts-sqlite-library/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-fts-sqlite-library')
  	```

## Usage

#### Create Table

```javascript

import RNFtsSqliteLibrary from 'react-native-fts-sqlite-librfts';

let error = null;

let DBClient = null;

try {
	DBClient = await RNFtsSqliteLibrary.newClientDB({
		dbName: 'CarShop.db',
		dbVersion: 1
	});
} catch(err) {
	error = err;
}

try {
	await DBClient.beginTransaction();
} catch(err) {
	error = err;
}

try {
	await DBClient.execSQL('CREATE TABLE IF NOT EXISTS car (' + 
	'car_id INTEGER PRIMARY KEY AUTOINCREMENT, model VARCHAR NOT NULL, ' + 
	'brand VARCHAR NOT NULL, year DATE NOT NULL, created_at DATE NOT NULL' + 
	')');
} catch(err) {
	error = err;
}

try {
	await DBClient.execSQL('CREATE TABLE IF NOT EXISTS inventory (' + 
	'inventory_id INTEGER PRIMARY KEY AUTOINCREMENT, total INTEGER NOT NULL, car_id INTEGER, ' + 
	'FOREIGN KEY (car_id) REFERENCES car(car_id)' +
	')');
} catch(err) {
	error = err;
}

if (error) {
	try {
		await DBClient.rollback();
		console.error(error.message);
	} catch(err) {
		console.error(err.message);
	}

	try {
		await DBClient.close();
	} catch(err) {
		console.error(err.message);
	}

	return;
}

try {
	await DBClient.commit();
} catch(err) {
	console.error(err.message);
}

try {
	await DBClient.close();
} catch(err) {
	console.error(err.message);
}

```

### Insert

```javascript

import RNFtsSqliteLibrary from 'react-native-fts-sqlite-librfts';

let error = null;

let DBClient = null;

try {
	DBClient = await RNFtsSqliteLibrary.newClientDB({
		dbName: 'CarShop.db',
		dbVersion: 1
	});
} catch(err) {
	error = err;
}

const tableManager = DBClient.tableManager;

try {
	await tableManager.beginTransaction();
} catch(err) {
	error = err;
}

const createdAt = moment().format('YYYY-MM-DD HH:mm:ss.SSS');

const car1ToSave = {
	model: 'Volkswagen', 
	brand: 'Jetta GLI', 
	year: {type: 'date', value: '2018', format: 'YYYY'}, //moment format
	created_at: {type: 'date', value: createdAt, format: 'YYYY-MM-DD HH:mm:ss.SSS'}, //moment format
};

let car1Id;

try {
	car1Id = await tableManager.insert('car', car1ToSave);
		
	if (car1Id && Number(car1Id) < 1) {
		error = "insert fail";
	} 
} catch(err) {
	error = err;
}

const inventory1ToSave = {
	total: 290,
	car_id: car1Id
};

try {
	let inventory1Id = await tableManager.insert('inventory', inventory1ToSave);
		
	if (inventory1Id && Number(inventory1Id) < 1) {
		error = "insert fail";
	} 
} catch(err) {
	error = err;
}

const car2ToSave = {
	model: 'Mercedes', 
	brand: 'A 200', 
	year: {type: 'date', value: '2019', format: 'YYYY'}, //moment format
	created_at: {type: 'date', value: createdAt, format: 'YYYY-MM-DD HH:mm:ss.SSS'}, //moment format
};

let car2Id;

try {
	car2Id = await tableManager.insert('car', car2ToSave);
	
	if (car2Id && Number(car2Id) < 1) {
		error = "insert fail";
	} 
} catch(err) {
	error = err;
}

const inventory2ToSave = {
	total: 94,
	car_id: car2Id
};

try {
	let inventory2Id = await tableManager.insert('inventory', inventory2ToSave);
		
	if (inventory2Id && Number(inventory2Id) < 1) {
		error = "insert fail";
	} 
} catch(err) {
	error = err;
}

if (error) {
	try {
		await tableManager.rollback();
		console.error(error.message);
	} catch(err) {
		console.error(err.message);
	}

	try {
		await DBClient.close();
	} catch(err) {
		console.error(err.message);
	}

	return;
}

try {
	await tableManager.commit();
} catch(err) {
	console.error(err.message);
}

try {
	await DBClient.close();
} catch(err) {
	console.error(err.message);
}

```

### Search Query

```javascript

import RNFtsSqliteLibrary from 'react-native-fts-sqlite-library';

let DBClient = null;

try {
	DBClient = await RNFtsSqliteLibrary.newClientDB({
		dbName: 'CarShop.db',
		dbVersion: 1
	});
} catch(err) {
	error = err;
}

const tableManager = DBClient.tableManager;

let resultSearch1;
let resultSearch2;

try {
	resultSearch1 = await tableManager.querySearch('SELECT * FROM inventory LEFT JOIN car ON car.car_id = inventory.inventory_id');
	resultSearch2 = await tableManager.querySearch(`SELECT STRFTIME('%Y', year) AS yearModel FROM car WHERE yearModel = ?`, ["2018"]);
} catch(err) {
	console.error(err.message);
}

console.info('result1: ', resultSearch1.totalHits);
console.info('result2: ', resultSearch2.totalHits);

if (Number(resultSearch1.totalHits) > 0) {
	resultSearch1.hits.map(obj => console.info(obj));
}

if (Number(resultSearch2.totalHits) > 0) {
	resultSearch2.hits.map(obj => console.info(obj));
}

try {
	await DBClient.close();
} catch(err) {
	console.error(err.message);
}

```

### Search Criteria
```javascript

//in progress

```

### Update

```javascript
import RNFtsSqliteLibrary from 'react-native-fts-sqlite-librfts';

let error = null;

let DBClient = null;

try {
	DBClient = await RNFtsSqliteLibrary.newClientDB({
		dbName: 'CarShop.db',
		dbVersion: 1
	});
} catch(err) {
	error = err;
}

const tableManager = DBClient.tableManager;

try {
	await tableManager.beginTransaction();
} catch(err) {
	error = err;
}

const inventoryUpdate = {
	total: 1000
};

try {
	let updatedInventory1Rows = await tableManager.update('inventory', inventoryUpdate, 'car_id = ?', [1]);
	
	if (updatedInventory1Rows && Number(updatedInventory1Rows) < 1) {
		error = "insert fail";
	}
} catch(err) {
	error = err;
}

const carUpdate = {
	year: {type: 'date', value: '2020', format: 'YYYY'}, //moment format
}

try {
	let updatedCar1Rows = await tableManager.update('car', carUpdate, `STRFTIME('%Y', year) = ?`, ['2019']);

	if (updatedCar1Rows && Number(updatedCar1Rows) < 1) {
		error = "insert fail";
	}
} catch(err) {
	error = err;
}

if (error) {
	try {
		await tableManager.rollback();
		console.error(error.message);
	} catch(err) {
		console.error(err.message);
	}

	try {
		await DBClient.close();
	} catch(err) {
		console.error(err.message);
	}

	return;
}

try {
	await tableManager.commit();
} catch(err) {
	console.error(err.message);
}

try {
	await DBClient.close();
} catch(err) {
	console.error(err.message);
}

```

### Delete

```javascript

import RNFtsSqliteLibrary from 'react-native-fts-sqlite-librfts';

let error = null;

let DBClient = null;

try {
	DBClient = await RNFtsSqliteLibrary.newClientDB({
		dbName: 'CarShop.db',
		dbVersion: 1
	});
} catch(err) {
	error = err;
}

const tableManager = DBClient.tableManager;

try {
	await tableManager.beginTransaction();
} catch(err) {
	error = err;
}

try {
	let deletedInventory1Rows = await tableManager.remove('inventory', 'car_id = ?', [1]);

	if (deletedInventory1Rows && Number(deletedInventory1Rows) < 1) {
		error = "insert fail";
	} 
} catch(err) {
	error = err;
}

try {
	let deletedCar1Rows = await tableManager.remove('car', `STRFTIME('%Y', year) = ?`, ['2018']);

	if (deletedCar1Rows && Number(deletedCar1Rows) < 1) {
		error = "insert fail";
	} 
} catch(err) {
	error = err;
}

if (error) {
	try {
		await tableManager.rollback();
		console.error(error.message);
	} catch(err) {
		console.error(err.message);
	}

	try {
		await DBClient.close();
	} catch(err) {
		console.error(err.message);
	}

	return;
}

try {
	await tableManager.commit();
} catch(err) {
	console.error(err.message);
}

try {
	await DBClient.close();
} catch(err) {
	console.error(err.message);
}

```