package model

data class Address(
  var street: String? = null,
  var houseNumber: Int? = null,
  var flatNumber: Int? = null,
  var province: String? = null, // wojewodztwo
  var district: String? = null,  // powiat
  var commune: String? = null,  // gmina
  var city: String? = null,
  var postalCode: String? = null,
  var post: String? = null
) {
  fun formatAddress() =
    "$street $houseNumber${if (flatNumber == null) "" else "/$flatNumber"},\n$postalCode $city"

  fun formatAddressJPK() =
    "$postalCode $city, $street $houseNumber${if (flatNumber == null) "" else "/$flatNumber"}"
}
