package com.example.cloudfide_igor_barcikowski_task.integrationtest;


import com.example.cloudfide_igor_barcikowski_task.producer.Producer;
import com.example.cloudfide_igor_barcikowski_task.producer.ProducerRepository;
import com.example.cloudfide_igor_barcikowski_task.producer.ProducerService;
import com.example.cloudfide_igor_barcikowski_task.producer.dto.ProducerCreateRequest;
import com.example.cloudfide_igor_barcikowski_task.producer.dto.ProducerPatchRequest;
import com.example.cloudfide_igor_barcikowski_task.producer.dto.ProducerResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class ProducerIntegrationTest {

    @Autowired private ProducerRepository producerRepository;
    @Autowired private ProducerService producerService;


    @Test
    void createProducerSuccessfully() {
        ProducerCreateRequest producerCreateRequest = new ProducerCreateRequest("igor", "poland", "producent telefonów");

        assertThat(producerRepository.findAll()).hasSize(0);

        producerService.createProducer(producerCreateRequest);

        List<Producer> producers = producerRepository.findAll();

        assertThat(producers).hasSize(1);
        assertThat(producers.getFirst().getName()).isEqualTo("igor");
    }


    @Test
    void patchProducerSuccessfully() {
        ProducerCreateRequest producerCreateRequest = new ProducerCreateRequest("igor", "poland", "producent telefonów");

        ProducerResponse producerResponse = producerService.createProducer(producerCreateRequest);

        ProducerPatchRequest producerPatchRequest = new ProducerPatchRequest("jurek", null, null);
        producerService.patchProducer(producerPatchRequest, producerResponse.id());

        Producer foundProducer = producerRepository.findById(producerResponse.id()).orElseThrow();
        Assertions.assertNotNull(foundProducer);
        assertThat(foundProducer.getName()).isEqualTo("jurek");
        assertThat(foundProducer.getCountry()).isEqualTo("poland");
        assertThat(foundProducer.getDescription()).isEqualTo("producent telefonów");
    }



    @Test
    void deleteProducerSuccessfully() {
        ProducerCreateRequest producerCreateRequest = new ProducerCreateRequest("igor", "poland", "producent telefonów");

        ProducerResponse producerResponse = producerService.createProducer(producerCreateRequest);

        assertThat(producerRepository.findAll()).hasSize(1);

        producerService.deleteProducer(producerResponse.id());

        assertThat(producerRepository.findAll()).hasSize(0);
    }




}
