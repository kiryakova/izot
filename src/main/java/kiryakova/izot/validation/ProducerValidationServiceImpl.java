package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Producer;
import kiryakova.izot.domain.models.service.ProducerServiceModel;
import org.springframework.stereotype.Component;

@Component
public class ProducerValidationServiceImpl implements ProducerValidationService {
    @Override
    public boolean isValid(Producer producer) {
        return producer != null;
    }

    @Override
    public boolean isValid(ProducerServiceModel producerServiceModel) {
        return producerServiceModel != null;
    }
}
