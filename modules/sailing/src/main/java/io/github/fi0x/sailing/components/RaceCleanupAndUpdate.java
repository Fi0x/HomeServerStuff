package io.github.fi0x.sailing.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RaceCleanupAndUpdate
{
	//TODO: Add a scheduled cleanup job that removes results without races

	//TODO: Add the UI-option to add a race without results, that will get updated as soon as results are available
}
