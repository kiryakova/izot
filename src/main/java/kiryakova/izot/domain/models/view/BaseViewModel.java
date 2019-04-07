package kiryakova.izot.domain.models.view;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseViewModel {

    private String id;

    protected BaseViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}