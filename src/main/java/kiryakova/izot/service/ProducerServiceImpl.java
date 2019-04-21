package kiryakova.izot.service;

import kiryakova.izot.common.ConstantsDefinition;
import kiryakova.izot.domain.entities.Producer;
import kiryakova.izot.domain.models.service.ProducerServiceModel;
import kiryakova.izot.error.ProducerNotDeletedException;
import kiryakova.izot.error.ProducerNotFoundException;
import kiryakova.izot.error.ProducerNotSavedException;
import kiryakova.izot.repository.ProducerRepository;
import kiryakova.izot.validation.ProducerValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProducerServiceImpl implements ProducerService {
    private final ProducerRepository producerRepository;
    private final ProducerValidationService producerValidation;
    private final ModelMapper modelMapper;

    @Autowired
    public ProducerServiceImpl(ProducerRepository producerRepository,
                               ProducerValidationService producerValidation,
                               ModelMapper modelMapper) {
        this.producerRepository = producerRepository;
        this.producerValidation = producerValidation;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addProducer(ProducerServiceModel producerServiceModel) {
        if(!producerValidation.isValid(producerServiceModel)){
            throw new IllegalArgumentException();
        }

        Producer producer = this.modelMapper.map(producerServiceModel, Producer.class);

        try {
            this.producerRepository.save(producer);
        } catch (Exception ignored){
            throw new ProducerNotSavedException(
                    String.format(
                            ConstantsDefinition.ProducerConstants.UNSUCCESSFUL_SAVED_PRODUCER,
                            producer.getName())
            );
        }
    }

    @Override
    public void editProducer(String id,
                             ProducerServiceModel producerServiceModel) {
        Producer producer = this.producerRepository.findById(id).orElse(null);

        this.checkIfProducerFound(producer, producerServiceModel.getName());
        
        producer.setName(producerServiceModel.getName());
        producer.setPhone(producerServiceModel.getPhone());

        try {
            this.producerRepository.save(producer);
        } catch (Exception ignored){
            throw new ProducerNotSavedException(
                    String.format(
                            ConstantsDefinition.ProducerConstants.UNSUCCESSFUL_SAVED_PRODUCER,
                            producer.getName())
            );
        }
    }

    @Override
    public void deleteProducer(String id) {
        Producer producer = this.producerRepository
                .findById(id).orElse(null);

        this.checkIfProducerFound(producer);

        try {
            this.producerRepository.delete(producer);
        }catch (Exception ignored){
            throw new ProducerNotDeletedException(
                    String.format(
                            ConstantsDefinition.ProducerConstants.UNSUCCESSFUL_DELETE_PRODUCER,
                            producer.getName())
            );
        }
    }

    @Override
    public ProducerServiceModel findProducerById(String id) {
        Producer producer = this.producerRepository
                .findById(id).orElse(null);

        this.checkIfProducerFound(producer);

        return this.modelMapper.map(producer, ProducerServiceModel.class);
    }

    @Override
    public List<ProducerServiceModel> findAllProducers() {
        return this.producerRepository.findAll()
                .stream()
                .map(p -> this.modelMapper.map(p, ProducerServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkIfProducerNameAlreadyExists(String name) {
        Producer producer = this.producerRepository
                .findByName(name).orElse(null);

        if(producer == null) {
            return false;
        }

        return true;
    }

    private void checkIfProducerFound(Producer producer) {
        if(!producerValidation.isValid(producer)) {
            throw new ProducerNotFoundException(ConstantsDefinition
                    .ProducerConstants.NO_SUCH_PRODUCER);
        }
    }

    private void checkIfProducerFound(Producer producer, String name) {
        if(!producerValidation.isValid(producer)) {
            throw new ProducerNotFoundException(
                    String.format(
                            ConstantsDefinition.ProducerConstants.NO_PRODUCER_WITH_NAME,
                            name)
            );
        }
    }
}
