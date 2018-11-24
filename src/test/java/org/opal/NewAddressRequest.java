package org.opal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class NewAddressRequest {
  @NotNull
  String addressLine1;
  @Size(min = 4)
  String addressLine2;
  String addressLine3;
  @NotNull
  String zip;
  @NotNull
  String city;
  @NotNull
  String province;
  @NotNull
  String country;
}
