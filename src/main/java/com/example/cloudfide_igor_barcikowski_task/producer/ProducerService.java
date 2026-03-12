package com.example.cloudfide_igor_barcikowski_task.producer;

import com.example.cloudfide_igor_barcikowski_task.producer.dto.ProducerPatchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.cloudfide_igor_barcikowski_task.producer.dto.ProducerCreateRequest;
import com.example.cloudfide_igor_barcikowski_task.producer.dto.ProducerResponse;
import com.example.cloudfide_igor_barcikowski_task.producer.dtomapper.ProducerMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class ProducerService {
    private final ProducerRepository producerRepository;
    private final ProducerMapper producerMapper;

    public ProducerService(ProducerRepository producerRepository, ProducerMapper producerMapper) {
        this.producerRepository = producerRepository;
        this.producerMapper = producerMapper;
    }



    @Transactional
    public ProducerResponse createProducer(ProducerCreateRequest producerCreateRequest) {
        if  (producerCreateRequest == null) {
            throw new IllegalArgumentException("producerCreateRequest cant be null");
        }

        if (producerRepository.existsByName(producerCreateRequest.name())) {
            throw new IllegalArgumentException("producer with this name already exists");
        }


        Producer producer = producerRepository.save(new Producer(
                producerCreateRequest.name(),
                producerCreateRequest.country(),
                producerCreateRequest.description()
        ));

        return producerMapper.producerToResponseDto(producer);
    }




    @Transactional(readOnly = true)
    public Page<ProducerResponse> getAllProducers(Pageable pageable) {
        Pageable safePageable = pageable;

        if (pageable.getPageSize() > 100) {
            safePageable = PageRequest.of(
                    pageable.getPageNumber(),
                    100
            );
        }

        return producerRepository.findAll(safePageable)
                .map(producerMapper::producerToResponseDto);
    }




    @Transactional
    public void deleteProducer(Long producerId) {
        Producer producer = producerRepository.findById(producerId)
                .orElseThrow(() -> new NoSuchElementException("Producer with this id does not exist"));

        producerRepository.delete(producer);
    }



    @Transactional
    public ProducerResponse patchProducer(ProducerPatchRequest producerPatchRequest, Long producerId) {
        if (producerPatchRequest == null || producerId == null) {
            throw new IllegalArgumentException("producerPatchRequest cant be null");
        }

        Producer producer = producerRepository.findById(producerId)
                .orElseThrow(() -> new NoSuchElementException("Producer with this id does not exist"));


        if (producerPatchRequest.name() != null && !producerPatchRequest.name().isBlank()) {producer.setName(producerPatchRequest.name());}
        if (producerPatchRequest.country() != null && !producerPatchRequest.country().isBlank()) {producer.setCountry(producerPatchRequest.country());}
        if (producerPatchRequest.description() != null && !producerPatchRequest.description().isBlank()) {producer.setDescription(producerPatchRequest.description());}

        return producerMapper.producerToResponseDto(producerRepository.save(producer));
    }

}
