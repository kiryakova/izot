package kiryakova.izot.service;

import kiryakova.izot.domain.entities.Producer;
import kiryakova.izot.domain.models.service.ProducerServiceModel;
import kiryakova.izot.repository.ProducerRepository;
import kiryakova.izot.validation.ProducerValidationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProducerServiceTests {
    @Autowired
    private ProducerRepository producerRepository;
    private ProducerService producerService;
    private ProducerValidationService producerValidationService;
    private ModelMapper modelMapper;
    private Producer producer;
    private MultipartFile multipartFile;
    List<Producer> producers;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
        this.producerService = new ProducerServiceImpl(this.producerRepository, this.producerValidationService, this.modelMapper);

        producer = new Producer();
        producer.setName("Producer1");
        producer.setPhone("111");

        producers = new ArrayList<>();
    }

    @Test
    public void getProducers_whenTwoProducers() {

        producerRepository.deleteAll();
        producerRepository.save(producer);
        Producer producer1 = new Producer();
        producer1.setName("producer2");
        producer1.setPhone("222");

        this.producerRepository.save(producer);
        this.producerRepository.save(producer1);

        List<ProducerServiceModel> categoriesFromDB = producerService.findAllProducers();

        Assert.assertEquals(categoriesFromDB.size(), 2);
    }

    @Test
    public void producerService_checkIfProducerNameAlreadyExists() {

        producer = producerRepository.saveAndFlush(producer);

        boolean exists = producerService.checkIfProducerNameAlreadyExists(producer.getName());

        Assert.assertTrue(exists);
    }

    @Test(expected = Exception.class)
    public void producerService_addProducer() {

        ProducerServiceModel toBeSaved = new ProducerServiceModel();
        toBeSaved.setName("Producer1");
        toBeSaved.setPhone("111");

        producerService.addProducer(toBeSaved);

        Producer actual = this.producerRepository.findByName(toBeSaved.getName()).orElse(null);
        Producer expected = this.producerRepository.findAll().get(0);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getPhone(), actual.getPhone());

    }

    @Test(expected = Exception.class)
    public void producerService_editProducer() {

        producer = this.producerRepository.save(producer);

        ProducerServiceModel toBeEdited = new ProducerServiceModel();
        toBeEdited.setId(producer.getId());
        toBeEdited.setName("Producer2");
        toBeEdited.setPhone("222");

        producerService.editProducer(producer.getId(), toBeEdited);

        Producer actual = this.producerRepository.findByName(producer.getName()).orElse(null);
        Producer expected = this.producerRepository.findAll().get(0);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getPhone(), actual.getPhone());

    }

    @Test(expected = Exception.class)
    public void producerService_deleteProducer(){

        producer = this.producerRepository.save(producer);

        producerService.deleteProducer(producer.getId());

        long expectedCount = 0;
        long actualCount = this.producerRepository.count();

        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test(expected = Exception.class)
    public void producerService_deleteProducerWithNullValues(){

        producer = this.producerRepository.save(producer);

        producerService.deleteProducer("InvalidId");

    }

    @Test(expected = Exception.class)
    public void producerService_findByIdProducer() {

        producer = this.producerRepository.saveAndFlush(producer);

        ProducerServiceModel actual = producerService.findProducerById(producer.getId());
        ProducerServiceModel expected = this.modelMapper.map(producer, ProducerServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getPhone(), actual.getPhone());
    }

    @Test(expected = Exception.class)
    public void producerService_findByIdProducerWithInValidId() {

        producer = this.producerRepository.saveAndFlush(producer);

        producerService.findProducerById("InvalidId");
    }
}
