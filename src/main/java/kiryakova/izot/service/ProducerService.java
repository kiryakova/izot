package kiryakova.izot.service;

import kiryakova.izot.domain.models.service.ProducerServiceModel;

import java.util.List;

public interface ProducerService {
    void addProducer(ProducerServiceModel producerServiceModel);

    void editProducer(String id, ProducerServiceModel producerServiceModel);

    void deleteProducer(String id);

    ProducerServiceModel findProducerById(String id);

    List<ProducerServiceModel> findAllProducers();

    boolean checkIfProducerNameAlreadyExists(String name);
}
