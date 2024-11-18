package io.github.fi0x.util.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This dto is used to send information about the current service to the hub.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDataDto
{
    private String name;
    private String protocol;
    private String ip;
    private Integer port;
}
