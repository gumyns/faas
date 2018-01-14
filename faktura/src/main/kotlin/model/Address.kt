package model

data class Address(
	var street: String? = null,
	var houseNumber: Int? = null,
	var flatNumber: Int? = null,
	var province: String? = null, // wojewodztwo
	var district: String? = null,  // powiat
	var commune: String? = null,  // gmina
	var city: String? = null,
	var postalCode: String? = null
) {
	fun formatAddress() =
		"$street ${if (flatNumber == null) "" else "$flatNumber/"}$houseNumber,\n$postalCode $city"

	fun formatAddressJPK() =
		"$postalCode $city, $street ${if (flatNumber == null) "" else "$flatNumber/"}$houseNumber"
}
