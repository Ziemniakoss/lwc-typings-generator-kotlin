package pl.ziemniakoss.lwc_typings_generator.metadata_types

enum class FieldType {
	string,
	boolean,
	int,
	double,
	date,
	datetime,
	base64,
	id,
	address,
	reference,
	currency,
	textarea,
	percent,
	phone,
	url,
	email,
	combobox,
	picklist,
	multipicklist,
	location;

	fun getJsType(): String {
		return when(this) {
			int -> "number"
			double -> "number"
			date -> "string | Date"
			datetime -> "string"
			base64 -> "string"
			id -> "string"
			address -> "any"
			currency -> "number"
			textarea -> "string"
			percent -> "number"
			phone -> "string"
			url -> "string"
			email -> "string"
			combobox -> "string"
			picklist -> "string"
			multipicklist -> "string"
			location -> "string" //TODO check
			else -> this.toString()
		}
	}
}
