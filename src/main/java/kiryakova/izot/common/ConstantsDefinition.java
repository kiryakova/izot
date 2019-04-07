package kiryakova.izot.common;

public final class ConstantsDefinition {

    public final static class GlobalConstants {
        public static final String INCORRECT_ID= "Incorrect id!";
        public static final String NO_ENTITY_WITH_ID= "No %s with id: %s";
    }

    public final static class BindingModelConstants {
        public static final String NOT_NULL= "Can not be null!";
        public static final String NOT_EMPTY= "Cannot be empty!";
        //public static final String EMAIL_IS_NOT_CORRECT = "Email is not correct!";
    }

    public final static class UserConstants {
        public static final String NO_SUCH_USER = "Username not found!";
        public static final String INCORRECT_PASSWORD = "Incorrect password!";
        public static final String NO_ROLE_LOWER_THAN_USER = "There is no role, lower than USER";
    }
}

