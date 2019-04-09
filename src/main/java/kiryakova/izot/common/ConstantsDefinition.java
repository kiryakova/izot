package kiryakova.izot.common;

public final class ConstantsDefinition {

    public final static class GlobalConstants {
        public static final String INCORRECT_ID= "Incorrect id!";
        public static final String NO_ENTITY_WITH_ID= "No %s with id: %s";
    }

    public final static class BindingModelConstants {
        public static final String NOT_NULL= "Не може да бъде null!";
        public static final String NOT_EMPTY= "Не може да бъде празно!";
        public static final String EMAIL_IS_NOT_CORRECT = "Email-а не е коректен!";
        public static final String PASSWORD_IS_NOT_CORRECT = "Паролата трябва да бъде от 3 до 18 символа!";
        public static final String USERNAME_IS_NOT_CORRECT = "Името трябва да бъде от 5 до 18 символа!";
    }

    public final static class UserConstants {
        public static final String NO_SUCH_USER = "Не е намерен такъв потребител!";
        public static final String INCORRECT_PASSWORD = "Некоректна парола!";
        public static final String PASSWORDS_DO_NOT_MATCH = "Паролата не е потвърдена!";
        public static final String UNSUCCESSFUL_USER_REGISTRATION = "Регистриране на потребител %s неуспешно.";
        public static final String UNSUCCESSFUL_USER_EDITING = "Редактиране на потребител %s неуспешно.";
    }
}

