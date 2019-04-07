package kiryakova.izot.service;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.entities.Producer;
import kiryakova.izot.domain.models.service.ProducerServiceModel;
import kiryakova.izot.repository.ProducerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProducerServiceImpl implements ProducerService {
    private final ProducerRepository producerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProducerServiceImpl(ProducerRepository producerRepository, ModelMapper modelMapper) {
        this.producerRepository = producerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProducerServiceModel addProducer(ProducerServiceModel producerServiceModel) {
        Producer producer = this.modelMapper.map(producerServiceModel, Producer.class);
        this.producerRepository.saveAndFlush(producer);

        return this.modelMapper.map(producer, ProducerServiceModel.class);
    }

    @Override
    public ProducerServiceModel editProducer(String id, ProducerServiceModel producerServiceModel) {
        Producer producer = this.producerRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format(ConstantsDefinition.GlobalConstants.NO_ENTITY_WITH_ID, Producer.class.getClass(), id))
                );
        
        producer.setName(producerServiceModel.getName());
        producer.setPhone(producerServiceModel.getPhone());

        producer = this.producerRepository.saveAndFlush(producer);
        return this.modelMapper.map(producer, ProducerServiceModel.class);
    }

    @Override
    public ProducerServiceModel deleteProducer(String id) {
        Producer producer = this.producerRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format(ConstantsDefinition.GlobalConstants.NO_ENTITY_WITH_ID, Producer.class.getClass(), id))
                );

        this.producerRepository.delete(producer);

        return this.modelMapper.map(producer, ProducerServiceModel.class);
    }

    @Override
    public ProducerServiceModel findProducerById(String id) {
        Producer producer = this.producerRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format(ConstantsDefinition.GlobalConstants.NO_ENTITY_WITH_ID, Producer.class.getClass(), id))
                );

        return this.modelMapper.map(producer, ProducerServiceModel.class);
    }

    @Override
    public List<ProducerServiceModel> findAllProducers() {
        return this.producerRepository.findAll()
                .stream()
                .map(p -> this.modelMapper.map(p, ProducerServiceModel.class))
                .collect(Collectors.toList());
    }

}
