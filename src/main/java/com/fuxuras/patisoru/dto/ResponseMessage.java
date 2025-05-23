package com.fuxuras.patisoru.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  A DTO (Data Transfer Object) that represents the response
 *  object for POST methods.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {
    private String message;

    /**
     * A numeric code indicating the outcome of the operation.
     * <p>Possible values:</p>
     * <ul>
     *     <li><b>1:</b> Success</li>
     *     <li><b>-1:</b> Failure</li>
     * </ul>
     */
    private int code;
}
