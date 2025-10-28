
package com.cts.project.Travelling_package.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItineraryRequest {
    private int userId;
    private Long packageId;
}
