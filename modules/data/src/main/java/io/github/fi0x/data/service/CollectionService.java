package io.github.fi0x.data.service;

import io.github.fi0x.data.db.DataRepo;
import io.github.fi0x.data.db.DataTagRepo;
import io.github.fi0x.data.db.TagRepo;
import io.github.fi0x.data.db.entities.DataEntity;
import io.github.fi0x.data.db.entities.DataTagEntity;
import io.github.fi0x.data.db.entities.TagEntity;
import io.github.fi0x.data.logic.converter.DataConverter;
import io.github.fi0x.data.logic.dto.DataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectionService
{
	private final DataRepo dataRepo;
	private final DataTagRepo dataTagRepo;
	private final TagRepo tagRepo;

	public void addData(String address, DataDto data)
	{
		log.trace("addData() called from address {}", address);

		DataEntity dataEntity = DataConverter.toEntity(data);
		dataEntity.setId(dataRepo.getHighestId().orElse(-1L) + 1);
		dataRepo.save(dataEntity);

		data.getTags().forEach(tag -> {
			Optional<TagEntity> tagEntity = tagRepo.findByTag(tag);
			long id;
			if(tagEntity.isEmpty())
			{
				TagEntity newEntity = TagEntity.builder().id(tagRepo.getHighestId().orElse(-1L) + 1).tag(tag).build();
				tagRepo.save(newEntity);
				id = newEntity.getId();
			}
			else
				id = tagEntity.get().getId();

			DataTagEntity dataTagEntity = DataTagEntity.builder().dataId(dataEntity.getId()).tagId(id).build();
			dataTagRepo.save(dataTagEntity);
		});
	}
}
