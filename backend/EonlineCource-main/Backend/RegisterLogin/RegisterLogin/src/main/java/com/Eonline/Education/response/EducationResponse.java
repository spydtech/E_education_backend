package com.Eonline.Education.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.time.Year;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class EducationResponse {


    private String nameOfInstitution;

    private String degree;

    @JsonFormat(pattern="MM")
    private Month startMonth;
    @JsonFormat(pattern="yyyy")
    private Year startYear;
    @JsonFormat(pattern="MM")
    private Month  graduationMonth;
    @JsonFormat(pattern="yyyy")
    private Year graduationYear;

}
