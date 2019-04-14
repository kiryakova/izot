package kiryakova.izot.common;

public final class ConstantsDefinition {

    public final static class GlobalConstants {
        public static final String NO_ENTITY_WITH_ID= "No %s with id: %s";
    }

    public final static class BindingModelConstants {
        public static final String NOT_NULL= "Не може да бъде null!";
        public static final String NOT_EMPTY= "Не може да бъде празно!";
        public static final String EMAIL_IS_NOT_CORRECT = "Email-а не е коректен!";
        public static final String PASSWORD_IS_NOT_CORRECT = "Паролата трябва да бъде от 3 до 18 символа!";
        public static final String USERNAME_IS_NOT_CORRECT = "Името трябва да бъде от 5 до 18 символа!";
        public static final String NAME_IS_NOT_CORRECT = "Името трябва да бъде от 5 до 25 символа!";
        public static final String CUSTOMER_NAME_IS_NOT_CORRECT = "Името трябва да бъде поне 2 символа!";
    }

    public final static class UserConstants {
        public static final String NO_SUCH_USER = "Не е намерен такъв потребител!";
        public static final String NO_USER_WITH_USERNAME = "Не е намерен потребител %s!";
        public static final String INCORRECT_PASSWORD = "Некоректна парола!";
        public static final String PASSWORDS_DO_NOT_MATCH = "Паролата не е потвърдена!";
        public static final String UNSUCCESSFUL_USER_REGISTRATION = "Регистриране на потребител %s неуспешно.";
        public static final String UNSUCCESSFUL_USER_EDITING = "Редактиране на потребител %s неуспешно.";
        public static final String USER_ALREADY_EXISTS = "Потребител с име %s вече съществува";
        public static final String EMAIL_ALREADY_EXISTS = "Потребител с email %s вече съществува";
    }

    public final static class ProductConstants {
        public static final String NO_SUCH_PRODUCT = "Не е намерен такъв продукт!";
        public static final String NO_PRODUCT_WITH_NAME = "Не е намерен продукт %s!";
        public static final String UNSUCCESSFUL_SAVED_PRODUCT = "Запис на продукт %s неуспешно!";
        public static final String UNSUCCESSFUL_DELETE_PRODUCT = "Неуспешно изтриване на продукт %s";
        public static final String PRODUCT_ALREADY_EXISTS = "Продукт с име %s вече съществува";
    }

    public final static class CategoryConstants {
        public static final String NO_SUCH_CATEGORY = "Не е намерена такава категория!";
        public static final String NO_CATEGORY_WITH_NAME = "Не е намерена категория %s!";
        public static final String UNSUCCESSFUL_SAVED_CATEGORY = "Запис на категория %s неуспешно!";
        public static final String UNSUCCESSFUL_DELETE_CATEGORY = "Неуспешно изтриване на категория %s";
        public static final String CATEGORY_ALREADY_EXISTS = "Категория с име %s вече съществува";
    }

    public final static class CustomerConstants {
        public static final String UNSUCCESSFUL_SAVED_CUSTOMER = "Запис на клиент %s неуспешно!";
    }

    public final static class OrderConstants {
        public static final String UNSUCCESSFUL_SAVED_ORDER = "Неуспешно създаване на поръчка!";
        public static final String UNSUCCESSFUL_DELETE_PRODUCT_BY_ORDER = "Неуспешно изтриване на заявен продукт!";
        public static final String NO_SUCH_ORDER = "Няма такава поръчка!";
        public static final String NO_SUCH_PRODUCT = "Няма такъв продукт!";
    }

    public final static class ProducerConstants {
        public static final String UNSUCCESSFUL_SAVED_PRODUCER = "Запис на производител %s неуспешно!";
        public static final String NO_SUCH_PRODUCER = "Няма такъв производител!";
        public static final String NO_PRODUCER_WITH_NAME = "Не е намерен производител %s!";
        public static final String UNSUCCESSFUL_DELETE_PRODUCER = "Неуспешно изтриване на производител %s";
        public static final String PRODUCER_ALREADY_EXISTS = "Производител с име %s вече съществува";
    }
}
