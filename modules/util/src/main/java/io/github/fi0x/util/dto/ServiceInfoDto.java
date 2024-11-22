package io.github.fi0x.util.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This dto is used to provide detailed information about a service.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInfoDto
{
	private String name;
	private String protocol;
	private String ip;
	private Integer port;

	private Boolean loginDisabled;
	private Boolean isHub;
}
