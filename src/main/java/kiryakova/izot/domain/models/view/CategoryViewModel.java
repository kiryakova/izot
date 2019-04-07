package kiryakova.izot.domain.models.view;

public class CategoryViewModel extends BaseViewModel {
    private String name;
    private String imageUrl;

    public CategoryViewModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
