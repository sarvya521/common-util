package com.backend.boilerplate.dto;

import com.backend.boilerplate.constant.Status;
import com.backend.boilerplate.exception.ErrorDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

// @formatter:off

/**
 * Wrapper of final API Response. All endpoints are required to return API Response in this format only.
 * <p>
 * There are 4 main sections of response.
 * <ul>
 *  <li>
 *   status = It will have value like SUCCESS, FAIL, and so on. This is very generic string representation of status
 *  </li>
 *  <li>
 *   code = It is actually a HttpStatus code for a request call.
 *  </li>
 *  <li>
 *   data = Response Payload. It can be anything based on API. Of course, this will be available when it is
 *   successful request.
 *   If call is not successful then "data" section will be completely absent from response. And we will show another
 *   section as error
 *  </li>
 *  <li>
 *   errors = Array of Error. Each error will contain "code" (service-error-code) and "message".
 *   We can add "target" property in error to specify the request param or part which causes the error (particularly
 *   in the case of validation errors)
 *  </li>
 * <ul>
 * <div>
 * <p>successful response sample</p>
 * <pre>{@code
 *
 *      {
 *        "status" : "SUCCESS",
 *        "code" : 200,
 *        "data" : { "id": 1234, "name": "demo name", "email": "demo@demo.com" }
 *      }
 *
 * }</pre>
 * </div>
 * <div>
 * <p>failure response sample 1</p>
 * <pre>{@code
 *
 *      {
 *        "status": "FAIL",
 *        "code" : 500,
 *        "errors": [
 *          { "code": 1000, "message": "demo message1" },
 *          { "code": 1001, "message": "demo message2" }
 *        ]
 *      }
 *
 * }</pre>
 * </div>
 * <div>
 * <p>failure response sample 2</p>
 * <pre>{@code
 *
 *      {
 *        "status": "FAIL",
 *        "code" : 500,
 *        "errors": [
 *          { "code": 1000, "message": "demo message1", "target": "patientIdentifier" },
 *          { "code": 1001, "message": "demo message2", "target": "patinetEmail" }
 *        ]
 *      }
 *
 * }</pre>
 * </div>
 *
 * @author sarvesh
 * @version 0.0.3
 * @since 0.0.3
 */
//@formatter:on
@JsonInclude(content = Include.NON_NULL)
@JsonPropertyOrder({"status", "code", "data", "errors"})
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "All details about the API Response.")
public class Response<T> {
    /**
     * status of Api Response.
     *
     * @see Status
     */
    @Schema(description = "API Status")
    private Status status;
    /**
     * HttpStatus Code of Api Response.
     *
     * @see org.springframework.http.HttpStatus
     */
    @Schema(description = "API Http Status Code")
    private Integer code;
    /**
     * Api Response Payload
     */
    @Schema(description = "Response Payload")
    private T data;
    /**
     * Error details when status is not {@link Status#SUCCESS}
     *
     * @see ErrorDetails
     */
    @Schema(description = "List of Errors")
    private List<ErrorDetails> errors;

    /**
     * @param status
     * @param code
     * @param data
     */
    public Response(@NonNull Status status, @NonNull Integer code, T data) {
        this.status = status;
        this.code = code;
        this.data = data;
    }

    /**
     * @param status
     * @param code
     * @param errors
     */
    public Response(@NonNull Status status, @NonNull Integer code, ErrorDetails... errors) {
        this.status = status;
        this.code = code;
        this.errors = List.of(errors);
    }
}
