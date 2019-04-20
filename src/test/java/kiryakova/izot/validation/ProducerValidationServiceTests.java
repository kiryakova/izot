package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Producer;
import kiryakova.izot.domain.models.service.ProducerServiceModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProducerValidationServiceTests {
    private ProducerValidationService producerValidationService;

    @Before
    public void init() {
        producerValidationService = new ProducerValidationServiceImpl();
    }

    @Test
    public void isValidWithProducer_whenValid_true() {
        Producer producer = new Producer();
        boolean result = producerValidationService.isValid(producer);
        Assert.assertTrue(result);
    }

    @Test
    public void isValidWithProducer_whenNull_false() {
        Producer producer = null;
        boolean result = producerValidationService.isValid(producer);
        Assert.assertFalse(result);
    }

    @Test
    public void isValidWithProducerServiceModel_whenValid_false() {
        ProducerServiceModel producerServiceModel = new ProducerServiceModel();
        boolean result = producerValidationService.isValid(producerServiceModel);
        Assert.assertTrue(result);
    }

    @Test
    public void isValidWithProducerServiceModel_whenNull_false() {
        ProducerServiceModel producerServiceModel = null;
        boolean result = producerValidationService.isValid(producerServiceModel);
        Assert.assertFalse(result);
    }
}
