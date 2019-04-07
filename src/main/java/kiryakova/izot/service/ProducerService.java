package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.ProducerServiceModel;

import java.util.List;

public interface ProducerService {
    ProducerServiceModel addProducer(ProducerServiceModel producerServiceModel);

    ProducerServiceModel editProducer(String id, ProducerServiceModel producerServiceModel);

    ProducerServiceModel deleteProducer(String id);

    ProducerServiceModel findProducerById(String id);

    List<ProducerServiceModel> findAllProducers();
}
