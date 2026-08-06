package com.codewithben.Lofau.admin.audit.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditStatisticsResponse {

    /*
     * Total Logs
     */
    private Long totalLogs;

    /*
     * Today's Activity
     */
    private Long todayLogs;

    /*
     * User Actions
     */
    private Long userActions;

    /*
     * Role Actions
     */
    private Long roleActions;

    /*
     * Post Actions
     */
    private Long postActions;

    /*
     * Advertisement Actions
     */
    private Long advertisementActions;

    /*
     * Report Actions
     */
    private Long reportActions;

    /*
     * Group Actions
     */
    private Long groupActions;

}