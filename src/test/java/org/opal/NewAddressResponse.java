package org.opal;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class NewAddressResponse {
  UUID id;
  String addressLine1;
  String addressLine2;
  String addressLine3;
  String zip;
  String city;
  String province;
  String country;
}
