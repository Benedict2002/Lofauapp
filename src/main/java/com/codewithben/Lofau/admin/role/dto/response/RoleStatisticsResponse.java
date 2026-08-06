package com.codewithben.Lofau.admin.role.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleStatisticsResponse {

    private long superAdmins;

    private long admins;

    private long contentModerators;

    private long supportAdmins;

    private long advertisementManagers;

    private long analyticsAdmins;

}