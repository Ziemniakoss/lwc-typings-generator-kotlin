# LWC Typings generator

Program to generate:

- accurate schema imports(with fieldApiName and objectName fieds completion)
- SObject interfaces

SObject interfaces are used only to help developer to work with SObjects on frontend. Currently, generated interfaces types include:

- SObject interfaces: interface representing SObject in database with same name as sObject.
- Record type's dev names types: typescript string types accepting only correct developer names. Types have names generated with template `${sobjectName}__RecordType__DevName`
- picklist types: typescript string types accepting only correct picklist values. Types have name generated with template: `${sObjectName}__${picklistName}`

# Usage

Program must be executed in top folder of SF project. To generate types for Account, Contact and User, call

```
java -jar pathToJar.jar Account Contact User
```

After successful generation interfaces can be used in code like this:

```js
class Component {
	/** @type {{[key: string] : Account__RecordType__DevName}} */
	recordTypesDevNamesMap;

	/**
	 * @param {Account} account
	 * @param {Account__Industry} industryPicklistValue
	 */
	testFunction(account, industryPicklistValue) {
		if (industryPicklistValue === "Agriculture") {
			account.Active__c = 'No';
		}
		account.Industry = industryPicklistValue
		
		/* Works for relationships to! */
		for(const contact of account.Contacts) {
			console.log(contact.Name)
		}
	}
}
```

Types are generated incrementally, which means that you can generate types only for sObject that you are currently using and generate types for other sObject if you need later.

# TODO

- deeper nesting of schema (max 5, current 1)
- custom labels?
- more build in types and functions?