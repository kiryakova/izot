package kiryakova.izot.validation;

import kiryakova.izot.domain.entities.Producer;
import kiryakova.izot.domain.models.service.ProducerServiceModel;

public interface ProducerValidationService {
    boolean isValid(Producer producer);

    boolean isValid(ProducerServiceModel producerServiceModel);
}
